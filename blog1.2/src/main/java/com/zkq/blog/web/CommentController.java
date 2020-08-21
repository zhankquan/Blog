package com.zkq.blog.web;

import com.zkq.blog.po.Comment;
import com.zkq.blog.po.User;
import com.zkq.blog.service.BlogService;
import com.zkq.blog.service.CommentService;
import com.zkq.blog.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sun.net.www.content.image.png;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;


    @Autowired
    private MailService mailService;

    private String avatar="https://picsum.photos/id/10/400/400";
    private String avatar2="/images/Beau.jpg";

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return  "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId=comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user=(User)session.getAttribute("user");
        if(user!=null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            comment.setAvatar(avatar);
        }

        commentService.saveComment(comment,session);

        if(comment.getParentComment()!=null){
            Comment comment1 = commentService.findCommentById(comment.getParentComment().getId());
                mailService.contextLoads(comment1.getEmail(),comment.getContent(),"http://hellokq.top/blog/"+blogId);

        }

        return "redirect:/comments/" + blogId;
    }
}
