package com.zkq.blog.dao;
import com.zkq.blog.po.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public  interface NewsRepository extends JpaRepository<News,Long> {


    List<News> findAllByFlagTrue();

    List<News> findAllByFlagFalse();

    News findNewsById(Long id);

    void deleteById(int id);

    void deleteByFlagTrue();
}
