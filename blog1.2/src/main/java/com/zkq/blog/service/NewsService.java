package com.zkq.blog.service;

import com.zkq.blog.po.News;

import java.util.List;


public interface NewsService {
    public void receiveComment(News news);

    public void receiveMessage(News news);

    public News findNews(Long id);

    public List<News> getComment();

    public List<News> getMessage();

    public void delete(Long id);

//    public void deleteAll();
}
