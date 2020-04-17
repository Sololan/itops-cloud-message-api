package com.wejoyclass.itops.cloud.handler.Impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.wejoyclass.itops.cloud.entity.MessageEntity;
import com.wejoyclass.itops.cloud.handler.CallHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DefaultCallHandler implements CallHandler {
    @Value("${aliyun.accessKeyId}")
    String accessKeyId;

    @Value("${aliyun.accessSecret}")
    String accessSecret;

    @Value("${aliyun.call.template}")
    String callTemplate;

    @Value("${aliyun.call.number}")
    String callNumber;

    IAcsClient acsClient;

    SingleCallByTtsRequest request;

    public Boolean handler(MessageEntity call) {
        this.build();
        // 判断有没有发送成功
        Boolean flag = false;
        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = null;
        //必填-被叫号码
        request.setCalledNumber(call.getTo());
        Map<String, Object> msg = new HashMap<>();
        msg.put("content",call.getContent());
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam(JSONObject.toJSONString(msg));
        try {

            singleCallByTtsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if(singleCallByTtsResponse.getCode() != null && singleCallByTtsResponse.getCode().equals("OK")) {
            //请求成功
            log.info("语音文本外呼---------------");
            log.info("RequestId=" + singleCallByTtsResponse.getRequestId());
            log.info("Message=" + singleCallByTtsResponse.getMessage());
            if(singleCallByTtsResponse.getCode().equals("OK")){
                flag = true;
            }
        }
        return flag;
    }

    private void build(){
        //设置访问超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient 暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dyvmsapi", "dyvmsapi.aliyuncs.com");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        acsClient = new DefaultAcsClient(profile);
        request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber(callNumber);
        //必填-Tts模板ID
        request.setTtsCode(callTemplate);

        //可选-音量 取值范围 0--200
        request.setVolume(100);
        //可选-播放次数
        request.setPlayTimes(3);
    }
}
