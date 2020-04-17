package com.wejoyclass.itops.cloud.handler;

import com.wejoyclass.itops.cloud.entity.MessageEntity;

import java.util.List;

public interface WechatHandler {

    Boolean handler(MessageEntity weChat);

}
