package com.wejoyclass.itops.cloud.handler.Impl;

import com.wejoyclass.itops.cloud.entity.MessageEntity;
import com.wejoyclass.itops.cloud.handler.WechatHandler;
import com.wejoyclass.itops.cloud.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultWechatHandler implements WechatHandler {

    @Autowired
    WeChatService weChatService;

    @Override
    public Boolean handler(MessageEntity weChat) {
        //todo assert getTo is openId or openIds
        Integer count = weChatService.sendMessage(weChat.getContent(), weChat.getTo());
        return count > 0;
    }
}
