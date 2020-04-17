package com.wejoyclass.itops.cloud.service.Impl;

import com.wejoyclass.core.util.RedisUtil;
import com.wejoyclass.itops.cloud.dto.MessageContent;
import com.wejoyclass.itops.cloud.dto.SendMessaageRequest;
import com.wejoyclass.itops.cloud.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService {

    @Value("${weChat.appId}")
    private String appId;

    @Value("${weChat.appSecret}")
    private String appSecret;

    private static final String tokenKey = "WeChatAccessToken";

    private static final String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token" +
            "?grant_type=client_credential&appid={1}&secret={2}";

    private static final String messageUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send" +
            "?access_token={1}";

    @Scheduled(fixedRate = 5400000) // 1h30min
    public void setAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap> response = restTemplate.getForEntity(tokenUrl, HashMap.class, appId, appSecret);
        log.info("token response: {}", response);

        HttpStatus code = response.getStatusCode();
        Map body = response.getBody();
        if (code == HttpStatus.OK && body.containsKey("access_token")) {
            RedisUtil.put(tokenKey, body.get("access_token").toString());
        } else {
            log.warn("fail to set access_token: code: {}, body: {}", code, body);
        }
    }

    public Integer sendMessage(String content, String openId) {
        RestTemplate restTemplate = new RestTemplate();

        SendMessaageRequest message = SendMessaageRequest.builder()
                .touser(openId)
                .msgtype("text")
                .text(new MessageContent(content))
                .build();
        HttpEntity<SendMessaageRequest> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.postForEntity(messageUrl, request, String.class, RedisUtil.get(tokenKey).toString());
        log.info("msg response: {}", response);
        String body = response.getBody();
        HttpStatus code = response.getStatusCode();
        if (code == HttpStatus.OK) {
            return 1;
        } else {
            log.warn("fail to send message: code: {}, body: {}", code, body);
            return 0;
        }
    }

}
