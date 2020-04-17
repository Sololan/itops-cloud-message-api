package com.wejoyclass.itops.cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMessaageRequest {

    private String touser;

    private String msgtype; // : "text"

    private MessageContent text;
}
