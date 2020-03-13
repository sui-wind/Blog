package com.tlc.blog.web.admin;

import com.tlc.blog.po.Tag;
import com.tlc.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService TagService;

    @GetMapping("/tags")
    public String list(@PageableDefault(size = 5, sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", TagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", TagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag Tag, BindingResult bindingResult, RedirectAttributes attributes) {

        Tag tag1 = TagService.getTagByName(Tag.getName());
        if(tag1 != null) {
            bindingResult.rejectValue("name", "nameError", "该标签名称已存在");
        }
        if (bindingResult.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t =TagService.saveTag(Tag);
        if (t == null) {
            // 未保存成功
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag Tag, BindingResult bindingResult, @PathVariable Long id, RedirectAttributes attributes) {

        Tag Tag1 = TagService.getTagByName(Tag.getName());
        if(Tag1 != null) {
            bindingResult.rejectValue("name", "nameError", "该标签名称已存在");
        }
        if (bindingResult.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t =TagService.updateTag(id, Tag);
        if (t == null) {
            // 未保存成功
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        TagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
