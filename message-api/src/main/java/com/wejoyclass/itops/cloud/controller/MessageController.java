package com.wejoyclass.itops.cloud.controller;

import com.wejoyclass.core.util.CtrlUtil;
import com.wejoyclass.core.util.RespEntity;
import com.wejoyclass.itops.cloud.entity.MessageEntity;
import com.wejoyclass.itops.cloud.service.ReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "接收消息Controller")
public class MessageController {
    @Autowired
    ReceiveService receiveService;

    @ApiOperation("接收消息")
    @PostMapping("/receive")
    public RespEntity receiveMessage(@RequestBody List<MessageEntity> messageEntityList){
        return CtrlUtil.exe(r -> {
            receiveService.receiveMessage(messageEntityList);
        });
    }
}
