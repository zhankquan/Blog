package com.zkq.blog.dao;

import com.zkq.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog>{

    @Query("select b from Blog b where b.recommend = true ")
    List<Blog> findTop(Pageable pageable);

    //select * from t_blog where title like '% %'
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);


    @Transactional
    @Modifying
    @Query("update  Blog  set views=views+1 where id=?1")
    int updateViews(Long id);


    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog  b where b.published=true group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    @Query("select b from  Blog b where function('date_format',b.updateTime,'%Y') = ?1 ")
    List<Blog> findByYear(String year);



    @Query("select b from Blog b where b.published=true ")
    Page<Blog> findAllAndPublishedIsFalse(Pageable pageable);


    Long countByPublishedIsTrue();


}
//@Query("update Blog b set b.views=b.views+1 where b.id=?1")