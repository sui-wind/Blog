package com.tlc.blog.web;

import com.tlc.blog.po.Type;
import com.tlc.blog.service.BlogService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    /**
     * 根据分类查看博客
     * @param pageable
     * @param model
     * @param id 类型 id
     * @return
     */
    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @PathVariable Long id) {
        List<Type> typeList = typeService.listType(10000);
        // id = -1 代表直接从导航点击跳转
        if (id == -1) {
            id = typeList.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", typeList);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
