package com.wejoyclass.itops.cloud.handler.Impl;

import com.wejoyclass.itops.cloud.entity.MessageEntity;
import com.wejoyclass.itops.cloud.handler.EmailHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Component
public class DefaultEmailHandler implements EmailHandler {
    @Value("${email.hostServer}")
    String hostServer;

    @Value("${email.port}")
    Integer port;

    @Value("${email.username}")
    String username;

    @Value("${email.password}")
    String password;

    public void handler(MessageEntity emailMsg){
        DefaultEmailHandler.sendEmail(emailMsg.getTo(),username,username,password,"迎嘉消息平台",emailMsg.getContent());
    }

    public static void sendEmail(String toEmail,String fromEmail,
                                 final String authEmail,final String authPaw,
                                 String title,String text){
        log.info("接受到一条邮件报警 -----【toEmail:"+toEmail+"】 【fromEmail:"+fromEmail+"】 【title:"+title+"】 【text:"+text+"】");
        // 收件人电子邮箱
        String to = toEmail;

        // 发件人电子邮箱
        String from = fromEmail;

        // 指定发送邮件的主机为 localhost
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");

        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        // 获取默认session对象
        Session session = Session.getInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(authEmail, authPaw); //发件人邮件用户名、授权码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            //message.setSubject("I love u");
            message.setSubject(title);

            // 设置消息体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送 HTML 消息, 可以插入html标签
            // 发送消息
            Transport.send(message);
            log.info("Sent email successfully...");
        }catch (MessagingException mex) {
            log.info(mex.toString());
            mex.printStackTrace();
        }
    }
}
