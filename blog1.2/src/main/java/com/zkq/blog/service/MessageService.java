package com.zkq.blog.service;
import com.zkq.blog.po.Message;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MessageService {


    List<Message> listMessage();

    Message findMessageById(Long id);

    Message saveMessage(Message message, HttpSession session);
}
