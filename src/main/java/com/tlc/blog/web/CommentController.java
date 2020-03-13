package com.tlc.blog.web;

import com.tlc.blog.po.Comment;
import com.tlc.blog.po.User;
import com.tlc.blog.service.BlogService;
import com.tlc.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 更新评论区域
     * @param blogId 发布评论所在博客 id
     * @param model
     * @return 博客详情页面
     */
    @GetMapping("/comment/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        // 更新部分区域
        return "blog :: commentList";
    }

    /**
     * 发布评论
     * @param comment 发布的评论信息
     * @return 更新博客评论
     */
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            // comment.setNickname(user.getNickname());
        } else {
            comment.setAvatar(avatar);
        }
        comment.setBlog(blogService.getBlog(blogId));
        commentService.saveComment(comment);
        return "redirect:/comment/" + comment.getBlog().getId();
    }
}
