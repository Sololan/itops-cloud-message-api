package com.wejoyclass.itops.cloud.handler.Impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wejoyclass.itops.cloud.entity.MessageEntity;
import com.wejoyclass.itops.cloud.handler.SmsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DefaultSmsHandler implements SmsHandler {
    @Value("${aliyun.accessKeyId}")
    String accessKeyId;

    @Value("${aliyun.accessSecret}")
    String accessSecret;

    @Value("${aliyun.sms.openTemplate}")
    String smsOpenTemplate;

    @Value("${aliyun.sms.closeTemplate}")
    String smsCloseTemplate;

    @Value("${aliyun.sms.signName}")
    String smsSignName;

    IAcsClient client;

    CommonRequest request;

    public Boolean handler(MessageEntity sms){
        this.build();
        // 判断有没有发送成功
        Boolean flag = false;
        Map<String, Object> msg = new HashMap<>();
        // 信息获取
        String content = sms.getContent();
        log.info(content);
        // 阿里云配置的参数
        String equipmentCode;
        String monitorName;
        String problem;
        // 如果是产生的
        if(sms.getContent().contains("问题，请及时处理")){
            request.putQueryParameter("TemplateCode", smsOpenTemplate);
            // 获取截取的index
            int a = content.indexOf("您");
            int b = content.indexOf("设备上的");
            int c = content.indexOf("监控，出现");
            int d = content.indexOf("问题，请及时处理");
            // 截取
            equipmentCode = content.substring(a+1, b);
            monitorName = content.substring(b+4, c);
            // 大于20字符就截掉
            if(equipmentCode.length() > 20){
                equipmentCode = equipmentCode.substring(0,16) + "...";
            }
            if(monitorName.length() > 20){
                monitorName = monitorName.substring(0,16) + "...";
            }
        }else {
            request.putQueryParameter("TemplateCode", smsCloseTemplate);
            // 获取截取的index
            int a = content.indexOf("您");
            int b = content.indexOf("设备上的");
            int c = content.indexOf("监控，");
            int d = content.indexOf("问题已恢复");
            // 截取
            equipmentCode = content.substring(a+1, b);
            monitorName = content.substring(b+4, c);
            // 大于20字符就截掉
            if(equipmentCode.length() > 20){
                equipmentCode = equipmentCode.substring(0,16) + "...";
            }
            if(monitorName.length() > 20){
                monitorName = monitorName.substring(0,16) + "...";
            }
        }
        // 设置参数
        msg.put("equipmentCode",equipmentCode);
        msg.put("monitorName",monitorName);
        // 构建信息模板及发送人电话
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(msg));
        request.putQueryParameter("PhoneNumbers", sms.getTo());
        try {
            // 发送消息
            CommonResponse response = client.getCommonResponse(request);
            if(JSONObject.parseObject(response.getData()).get("Code").toString().equals("OK")){
                flag = true;
            }
            log.info(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private void build() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        client = new DefaultAcsClient(profile);
        request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("SignName", smsSignName);
    }
}
