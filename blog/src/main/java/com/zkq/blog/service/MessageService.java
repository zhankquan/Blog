package com.zkq.blog.service;
import com.zkq.blog.po.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {


    List<Message> listMessage();

    Message saveMessage(Message message);
}
