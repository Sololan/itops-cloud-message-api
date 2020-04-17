package com.wejoyclass.itops.cloud.service;

public interface WeChatService {

    void setAccessToken();

    Integer sendMessage(String content, String openId);
}
