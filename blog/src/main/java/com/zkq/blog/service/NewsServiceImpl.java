package com.zkq.blog.service;
import com.zkq.blog.dao.NewsRepository;
import com.zkq.blog.po.News;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    NewsRepository newsRepository;

    @RabbitListener(queues = "blog.comment")
    public void receiveComment(News news){
        System.out.println("news = " + news);
        newsRepository.save(news);
    }

    @RabbitListener(queues = "blog.message")
    public void receiveMessage(News news) {
        System.out.println("news = " + news);
        newsRepository.save(news);
    }

    @Override
    public News findNews(Long id) {
        News news=newsRepository.findNewsById(id);
        return news;
    }

    @Override
    public List<News> getComment() {
        return newsRepository.findAllByFlagFalse();
    }

    @Override
    public List<News> getMessage() {
        return newsRepository.findAllByFlagTrue();
    }

    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
