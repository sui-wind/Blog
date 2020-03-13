package com.tlc.blog.service;

import com.tlc.blog.NotFoundException;
import com.tlc.blog.dao.TagRepository;
import com.tlc.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {

    // 不建议直接进行注入（接口注入）， 建议构造注入或set注入
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag Tag) {
        return tagRepository.save(Tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }


    /**
     * 获取 id 中包含的标签
     * id 格式 1,2,3,4,5,6
     * @param id 标签 id
     * @return
     */
    @Override
    public List<Tag> listTag(String id) {
        return tagRepository.findAll(convertToList(id));
    }

    /**
     * 获取 size 个标签
     * @param size 需要获取的标签数量
     * @return
     */
    @Override
    public List<Tag> listTag(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return tagRepository.findTop(pageable);
    }

    /**
     * 将 ids 的格式进行转换
     * @param ids
     * @return
     */
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++) {
                list.add(new Long(idArray[i]));
            }
        }
        return list;
    }

    /**
     * 获取所有标签
     * @param pageable
     * @return
     */
    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    /**
     * 更新标签
     * @param id 标签 id
     * @param Tag 标签内容
     * @return
     */
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag Tag) {
        Tag t = tagRepository.findOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(Tag, t);
        return tagRepository.save(t);
    }

    /**
     * 删除标签
     * @param id
     */
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.delete(id);
    }
}
