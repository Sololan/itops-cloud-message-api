package com.wejoyclass.itops.cloud.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ToString
public class MessageEntity {
    // 消息类型
    private Integer type;
    // 消息内容
    private String content;
    // 消息发送人
    private String to;
    // 消息发送时间
    private Date date;
    // 参数内容
    private Map<String, String> param;
    // 短信模板code
    private String code;
}
