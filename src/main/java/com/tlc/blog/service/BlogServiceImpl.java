package com.tlc.blog.service;

import com.tlc.blog.NotFoundException;
import com.tlc.blog.dao.BlogRepository;
import com.tlc.blog.po.Blog;
import com.tlc.blog.po.Type;
import com.tlc.blog.util.MarkdownUtils;
import com.tlc.blog.util.MyBeanUtils;
import com.tlc.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    private BlogRepository blogRepository;

    /**
     * 构造注入
     */
    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository) {
         this.blogRepository = blogRepository;
    }

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        // 将markdown格式转换为html格式
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        // 更新浏览次数
        blogRepository.updateViews(id);
        return b;
    }

    /**
     * 统计博客数目
     * @return
     */
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    /**
     * 对文档进行年份分类归档
     * @return
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        // 查询得到多有文档的年份
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * 根据 tag 的 id 查询博客
     * @param tagId 查询的 tag 的 id
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // 关联查询
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    /**
     * 根据条件查询博客
     * @param query 查询条件，根据标题和内容查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    /**
     * 根据 updateTime 查询博客前size项
     * @param size 要查询博客的数量
     * @return
     */
    @Override
    public List<Blog> listRecommendBlog(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    /**
     * 分页动态查询
     * 可根据 标题、类型、是否推荐 查询
     * @param pageable
     * @param blogQuery 查询对象
     * @return
     */
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blogQuery.getTitle() + "%"));
                }
                if (blogQuery.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blogQuery.getTypeId()));
                }
                if (blogQuery.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blogQuery.isRecommend()));
                }
                CriteriaQuery<?> where = criteriaQuery.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    /**
     * 更新博客
     * @param id 被更新博客的 id
     * @param blog 博客更新的内容
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findOne(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.delete(id);
    }
}
