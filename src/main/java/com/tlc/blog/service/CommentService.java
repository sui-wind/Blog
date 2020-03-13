package com.tlc.blog.service;

import com.tlc.blog.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long id);

    Comment saveComment(Comment comment);
}
