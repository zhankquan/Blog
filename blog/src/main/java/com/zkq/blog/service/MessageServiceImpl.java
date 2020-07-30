package com.zkq.blog.service;
import com.zkq.blog.dao.MessageRepository;
import com.zkq.blog.po.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> listMessage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<Message> messages=messageRepository.findByParentMessageIsNull(sort);
        return eachMessage(messages);
    }

    @Override
    public Message saveMessage(Message message) {
        Long parentMessageId=message.getParentMessage().getId();
        if(parentMessageId!=-1){
            message.setParentMessage(messageRepository.getOne(parentMessageId));
        }else {
            message.setParentMessage(null);
        }
        message.setCreateTime(new Date());
        return messageRepository.save(message);
    }



    /*
       循环每个顶级的节点
        */
    private List<Message> eachMessage(List<Message> messages){
        List<Message> messagesView=new ArrayList<>();
        for (Message message : messages){
            Message m=new Message();
            BeanUtils.copyProperties(message,m);
            messagesView.add(m);
        }
        //合并评论的各层子代到第一级子代集合中
        combinChildren(messagesView);
        return messagesView;
    }

    /*
    Messages root根节点，blog不为空的对象集合
     */
    private void combinChildren(List<Message> messages){

        for (Message message : messages){
            List<Message> replys1=message.getReplyMessages();
            for (Message reply1 : replys1){
                //循环迭代，找出子代，存放在temReply中
                recurdively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            message.setReplyMessages(temReplys);
            //清除临时存放区
            temReplys=new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Message> temReplys=new ArrayList<>();

    /*
    递归迭代，剥洋葱
    message 被迭代的对象
     */
    private void recurdively(Message message){
        temReplys.add(message);//顶节点添加到临时集合
        if(message.getReplyMessages().size()>0){
            List<Message> replys=message.getReplyMessages();
            for(Message reply : replys){
                temReplys.add(reply);
                if(reply.getReplyMessages().size()>0){
                    recurdively(reply);
                }
            }
        }
    }
}
