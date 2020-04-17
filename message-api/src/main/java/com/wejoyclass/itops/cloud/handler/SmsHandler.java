package com.wejoyclass.itops.cloud.handler;

import com.wejoyclass.itops.cloud.entity.MessageEntity;

public interface SmsHandler {
    Boolean handler(MessageEntity sms);
}
