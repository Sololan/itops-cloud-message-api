package com.wejoyclass.itops.cloud.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wejoyclass.core.util.DefaultLifecycle;
import com.wejoyclass.core.util.RedisUtil;
import com.wejoyclass.itops.cloud.entity.MessageEntity;
import com.wejoyclass.itops.cloud.handler.CallHandler;
import com.wejoyclass.itops.cloud.handler.EmailHandler;
import com.wejoyclass.itops.cloud.handler.SmsHandler;
import com.wejoyclass.itops.cloud.handler.WechatHandler;
import com.wejoyclass.itops.cloud.service.Impl.ReceiveServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;

@Slf4j
@Configuration
public class WebConfig {
    /**
     * 定义Spring容器加载完成后执行的任务
     */
    @Bean
    public DefaultLifecycle defaultLifecycle() {
        return  new DefaultLifecycle(this::subscribe);
    }

    @Autowired
    SmsHandler smsHandler;

    @Autowired
    CallHandler callHandler;

    @Autowired
    EmailHandler emailHandler;

    @Autowired
    WechatHandler wechatHandler;

    private void subscribe() {
        String smsChannel = "SmsChannel";
        String callChannel = "CallChannel";
        String emailChannel = "EmailChannel";
        String weChatChannel = "WechatChannel";

        RedisUtil.subscribe((Message message, byte[] pattern) -> {
            String warningJSON = new String(message.getBody());
            MessageEntity sms = JSON.parseObject(warningJSON, MessageEntity.class);
            smsHandler.handler(sms);
        }, smsChannel);

        RedisUtil.subscribe((Message message, byte[] pattern) -> {
            String warningJSON = new String(message.getBody());
            MessageEntity call = JSON.parseObject(warningJSON, MessageEntity.class);
            callHandler.handler(call);
        }, callChannel);

        RedisUtil.subscribe((Message message, byte[] pattern) -> {
            String warningJSON = new String(message.getBody());
            MessageEntity email = JSON.parseObject(warningJSON, MessageEntity.class);
            emailHandler.handler(email);
        }, emailChannel);

        RedisUtil.subscribe((Message message, byte[] pattern) -> {
            String warningJSON = new String(message.getBody());
            MessageEntity weChat = JSON.parseObject(warningJSON, MessageEntity.class);
            wechatHandler.handler(weChat);
        }, weChatChannel);

        // 消息延时发送定时器
        RedisUtil.subscribeExpired((message, pattern) -> {
            String warningJSON = new String(message.getBody());
            MessageEntity messageEntity = JSONObject.parseObject(warningJSON, MessageEntity.class);
            ReceiveServiceImpl.distributeMessage(messageEntity);
        });
    }
}
