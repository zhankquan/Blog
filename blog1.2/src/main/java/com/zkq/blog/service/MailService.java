package com.zkq.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    public void contextLoads(String To,String content,String url) {
        SimpleMailMessage message = new SimpleMailMessage();
//        邮件设置
        message.setSubject("您在kqBlog的留言有新回复");
        message.setText("回复内容:"+content+"        -->"+"去看看:"+url);

        message.setTo(To);
        message.setFrom("2780296704@qq.com");
        javaMailSender.send(message);
    }
}
