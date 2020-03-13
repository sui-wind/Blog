package com.tlc.blog.service;

import com.tlc.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTag(Integer size);

    List<Tag> listTag(String id);

    Tag updateTag(Long id, Tag Tag);

    void deleteTag(Long id);
}
