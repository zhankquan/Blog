package com.zkq.blog.web;

import com.zkq.blog.po.Message;
import com.zkq.blog.po.User;
import com.zkq.blog.service.MailService;
import com.zkq.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MailService mailService;

    private String avatar="https://picsum.photos/id/305/400/400";



    @GetMapping("/Messages")
    public String Messages( Model model){
        model.addAttribute("messages",messageService.listMessage());
        return  "message_board";
    }


    @GetMapping("/messages")
    public String messages( Model model){
        model.addAttribute("messages",messageService.listMessage());
        return  "message_board :: messageList";
    }

    @PostMapping("/message")
    public String post(Message message, HttpSession session){
        System.out.println(message+"123123123123123123--------------------------------------123123132123321312");
        User user=(User)session.getAttribute("user");
        if(user!=null){
            message.setAvatar(user.getAvatar());
            message.setAdminMessage(true);
        }else {
            message.setAvatar(avatar);
        }
        message.setCreateTime(new Date());

        messageService.saveMessage(message,session);

        if(message.getParentMessage()!=null){
            Message message1 = messageService.findMessageById(message.getParentMessage().getId());
                mailService.contextLoads(message1.getEmail(),message.getContent(),"http://hellokq.top/Messages");

        }
        return "redirect:/messages";
    }
}
