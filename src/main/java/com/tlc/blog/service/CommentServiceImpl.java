package com.tlc.blog.service;

import com.tlc.blog.dao.CommentRepository;
import com.tlc.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long id) {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        // 找到顶级节点
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(id, sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findOne(parentCommentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        // 合并评论的各层子代到第一子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    // 存放迭代找出的所有子代的集合
    private List<Comment> temReplys = new ArrayList<>();

    /**
     * 合并评论的各层子代到第一子代集合中
     * @param comments root 根节点 blog 不为空的对象集合
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1 : replys1) {
                // 循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            // 修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(temReplys);
            // 清除临时存放区
            temReplys = new ArrayList<>();
        }
    }

    /**
     * 递归迭代
     * @param comment 被迭代对象
     */
    private void recursively(Comment comment) {
        // 顶节点添加到临时存放集合
        temReplys.add(comment);
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                temReplys.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }
}
