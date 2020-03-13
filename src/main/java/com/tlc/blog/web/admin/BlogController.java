package com.tlc.blog.web.admin;

import com.tlc.blog.po.Blog;
import com.tlc.blog.po.User;
import com.tlc.blog.service.BlogServiceImpl;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    public static final  String LIST =  "admin/blogs";
    public static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 初始化博客页面
     * @param pageable 分页
     * @param blogQuery 根据查询条件过滤博客
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        return LIST;
    }

    /**
     * 对查询后的表格进行部分更改
     * @param pageable
     * @param blogQuery
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        return "admin/blogs :: blogList";
    }

    /**
     * 新增博客
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());
        // 初始化标签与分类
        setTypeAndTag(model);
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    /**
     * 编辑博客
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        // 初始化标签与分类
        setTypeAndTag(model);
        model.addAttribute("blog", blogService.getBlog(id));
        Blog blog = blogService.getBlog(id);
        blog.init();
        return INPUT;
    }


    /**
     * 保存和发布博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b=blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null){
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }

    /**
     * 删除博客
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
