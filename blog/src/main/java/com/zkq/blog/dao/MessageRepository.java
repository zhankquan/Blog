package com.zkq.blog.dao;

import com.zkq.blog.po.Comment;
import com.zkq.blog.po.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {


    List<Message> findByParentMessageIsNull(Sort sort);

}
