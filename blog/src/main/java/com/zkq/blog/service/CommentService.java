package com.zkq.blog.service;

import com.zkq.blog.po.Comment;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment findCommentById(Long CommentId);

    Comment saveComment(Comment comment, HttpSession session);
}
