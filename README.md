# itops-cloud-message-api

message platform

just one api

/receive

receive a entity list include 4 parameter

    // 消息类型 1.call 2,sms 3.email 4.wechat
    private Integer type;
    // 消息内容
    private String content;
    // 消息发送人
    private String to;
    // 消息发送时间
    private Date date;
   