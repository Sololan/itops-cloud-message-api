package com.wejoyclass.itops.cloud;

import org.junit.Test;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class test {
    @Test
    public void test(){
        Mailer mailer = MailerBuilder.withSMTPServer("smtp.163.com", 994, "wejoy_technology@163.com", "liuzt#2019").buildMailer();
        Email email = EmailBuilder.startingBlank()
                .from("liuzt","liu951129@vip.qq.com")
                .to("liu951129@vip.qq.com")
                .withSubject("subject")
                .withPlainText("content")
                .buildEmail();
        mailer.sendMail(email);
    }
}
