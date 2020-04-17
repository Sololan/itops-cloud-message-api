package com.wejoyclass.itops.cloud.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wejoyclass.core.util.RedisUtil;
import com.wejoyclass.itops.cloud.constant.Constant;
import com.wejoyclass.itops.cloud.entity.MessageEntity;
import com.wejoyclass.itops.cloud.service.ReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReceiveServiceImpl implements ReceiveService {

    @Override
    public void receiveMessage(List<MessageEntity> messageEntityList) {
        // 遍历传过来的实体
        for (MessageEntity message:
             messageEntityList) {
            // 判断是不是即时发送
            if(message.getDate() == null || message.getDate().equals("")){
                distributeMessage(message);
            }
            // 如果不是即时发送，则要判断什么时候发送，计算多久以后发送，并且把ttl设置为这个值
            else {
                // 当前时间
                Date now = new Date();
                // 过期时间
                Long ttl = (message.getDate().getTime() - now.getTime())/1000L;
                // 如果过期时间小于0那么久即时发送
                if(ttl < 0){
                    distributeMessage(message);
                }
                // 如果大于0，放入定时器
                else {
                    RedisUtil.put(JSON.toJSONString(message),"",ttl);
                }
            }
        }
    }

    public static void distributeMessage(MessageEntity message){
        if(message.getType().equals(Constant.MESSAGE_TYPE_CALL)){
            RedisUtil.publish("CallChannel", JSONObject.toJSONString(message));
        }
        if(message.getType().equals(Constant.MESSAGE_TYPE_SMS)){
            RedisUtil.publish("SmsChannel", JSONObject.toJSONString(message));
        }
        if(message.getType().equals(Constant.MESSAGE_TYPE_WECHAT)){
            RedisUtil.publish("WechatChannel", JSONObject.toJSONString(message));
        }
        if(message.getType().equals(Constant.MESSAGE_TYPE_EMAIL)){
            RedisUtil.publish("EmailChannel", JSONObject.toJSONString(message));
        }
    }
}
