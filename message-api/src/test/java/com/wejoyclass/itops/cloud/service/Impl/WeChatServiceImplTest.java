package com.wejoyclass.itops.cloud.service.Impl;

import com.wejoyclass.itops.cloud.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
class WeChatServiceImplTest {

    @Autowired
    WeChatService weChatService;

    @Test
    void setAccessToken() {
//        weChatService.test("");
    }

    @Test
    void sendMessage() {
        String token = "31_lWJaWqLPbaB5Q49lGlsupqRMWpuSERzsgLPuz19LkabyCY50eACQwjY0-taQ8rXjQm53qG4cXRZZOT9tCqNRTb9BWRme7A1RvyYsA76pd-sdQHMmYbLSzoK_Pof8575ZRPe7T3BuiIFyYXlLRCMaACAGRB";
        String openId = "olxuM0hop9RSd9dnV6y_iQL7XOg0";
        String content = "content";

//        weChatService.test(token);
        weChatService.sendMessage(content, openId);
    }
}