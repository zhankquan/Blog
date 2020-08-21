package com.zkq.blog.service;

import com.zkq.blog.dao.CommentRepository;
import com.zkq.blog.po.Comment;
import com.zkq.blog.po.News;
import com.zkq.blog.po.User;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        List<Comment> comments=commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    @Override
    public Comment findCommentById(Long CommentId) {
        Comment comment = commentRepository.findCommentById(CommentId);
        return comment;
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment, HttpSession session) {
        Long parentCommentId=comment.getParentComment().getId();
        if(parentCommentId!=-1){
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());

        try{
            User user=(User)session.getAttribute("user");
            if(user==null){
                News n = new News(comment.getNickname(),comment.getEmail(),comment.getContent(),
                        new Date(),comment.getBlog().getId(),false);
                rabbitTemplate.convertAndSend("exchange.direct.blog","comment",n);
            }
        }catch (Exception e){
            System.out.println(e);
        }


        return commentRepository.save(comment);
    }

    /*
    循环每个顶级的节点
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView=new ArrayList<>();
        for (Comment comment : comments){
            Comment c=new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combinChildren(commentsView);
        return commentsView;
    }

    /*
    Comments root根节点，blog不为空的对象集合
     */
    private void combinChildren(List<Comment> comments){

        for (Comment comment : comments){
            List<Comment> replys1=comment.getReplyComments();
            for (Comment reply1 : replys1){
                //循环迭代，找出子代，存放在temReply中
                recurdively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(temReplys);
            //清除临时存放区
            temReplys=new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> temReplys=new ArrayList<>();

    /*
    递归迭代，剥洋葱
    comment 被迭代的对象
     */
    private void recurdively(Comment comment){
        temReplys.add(comment);//顶节点添加到临时集合
        if(comment.getReplyComments().size()>0){
            List<Comment> replys=comment.getReplyComments();
            for(Comment reply : replys){
                temReplys.add(reply);
                if(reply.getReplyComments().size()>0){
                    recurdively(reply);
                }
            }
        }
    }
}
