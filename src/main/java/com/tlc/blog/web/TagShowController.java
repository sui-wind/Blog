package com.tlc.blog.web;

import com.tlc.blog.po.Tag;
import com.tlc.blog.service.BlogService;
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

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    /**
     * 根据分类查看博客
     * @param pageable
     * @param model
     * @param id 类型 id
     * @return
     */
    @GetMapping("/tags/{id}")
    public String types(@PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @PathVariable Long id) {
        List<Tag> tagList = tagService.listTag(10000);
        // id = -1 代表直接从导航点击跳转
        if (id == -1) {
            id = tagList.get(0).getId();
        }
        model.addAttribute("tags", tagList);
        model.addAttribute("page", blogService.listBlog(id, pageable));
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
