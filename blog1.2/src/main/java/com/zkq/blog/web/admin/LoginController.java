package com.zkq.blog.web.admin;

import com.zkq.blog.po.News;
import com.zkq.blog.po.Message;
import com.zkq.blog.po.User;
import com.zkq.blog.service.NewsService;
import com.zkq.blog.service.UserService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session,Model model,
                        RedirectAttributes attributes){
        User user=userService.checkUser(username,password);
        if(user!=null){
            user.setPassword(null);
            session.setAttribute("user",user);

            ArrayList<News> messages=(ArrayList)newsService.getMessage();
            ArrayList<News> comments=(ArrayList)newsService.getComment();

//            Message message = (Message) rabbitTemplate.receiveAndConvert("blog.message");
//            News comment2 = (News) rabbitTemplate.receiveAndConvert("blog.comment");
            model.addAttribute("messages",messages);
            model.addAttribute("comments",comments);
            return "admin/index";
        }else{
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
