package com.tlc.blog.web;


import com.google.protobuf.BlockingService;
import com.tlc.blog.NotFoundException;
import com.tlc.blog.service.BlogService;
import com.tlc.blog.service.CommentService;
import com.tlc.blog.service.TagService;
import com.tlc.blog.service.TypeService;
import com.tlc.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    /**
     * 查看博客首页
     * @param pageable 分页
     * @param model 向前端传输信息
     * @return 页面
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,  Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listType(6));
        model.addAttribute("tags", tagService.listTag(10));
        // 博客推荐
        model.addAttribute("recommendBlogs", blogService.listRecommendBlog(8));
        return "index";
    }

    /**
     * 搜索博客
     * @param pageable 分页
     * @param model 向前端传输信息
     * @param query 查询信息
     * @return 查询到的结果页面
     */
    @GetMapping("/search")
    public String search(@PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         Model model, @RequestParam String query) {
        model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "/search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        model.addAttribute("comments", commentService.listCommentByBlogId(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newBlogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlog(3));
        return "_fragments :: newBlogList";
    }
}