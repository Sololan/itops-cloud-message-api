package com.wejoyclass.itops.cloud.handler;

import com.wejoyclass.itops.cloud.entity.MessageEntity;

public interface CallHandler {
    Boolean handler(MessageEntity call);
}
