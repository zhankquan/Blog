package com.zkq.blog.web.admin;

import com.zkq.blog.po.News;
import com.zkq.blog.service.BlogService;
import com.zkq.blog.service.MessageService;
import com.zkq.blog.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/commentNews/{id}")
    public String comment(@PathVariable Long id, Model model){
        News news = newsService.findNews(id);
        model.addAttribute("blog",blogService.getAndConvert(news.getUrl()));
        newsService.delete(id);
        return "blog";
    }

    @GetMapping("/messageNews/{id}")
    public String message(@PathVariable Long id, Model model){
//        model.addAttribute("messages",messageService.listMessage());
        newsService.delete(id);
        return  "redirect:/Messages";
    }
}
