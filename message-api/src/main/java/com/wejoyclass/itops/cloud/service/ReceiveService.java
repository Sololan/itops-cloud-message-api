package com.wejoyclass.itops.cloud.service;

import com.wejoyclass.itops.cloud.entity.MessageEntity;

import java.util.List;

public interface ReceiveService {
    void receiveMessage(List<MessageEntity> messageEntityList);
}
