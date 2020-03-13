package com.tlc.blog.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_blog")
public class Blog {

    @Id
    @GeneratedValue
    private Long id;
    // 标题
    private String title;
    // 内容
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    // 首图地址
    private String firstPicture;
    // 原创、转载、翻译等
    private String flag;
    // 浏览次数
    private Integer views;
    // 赞赏是否开启
    private boolean appreciation;
    // 分享状态
    private boolean shareStatement;
    // 是否可以评论
    private boolean comment;
    // 是否发布
    private boolean published;
    // 是否推荐
    private boolean recommend;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    // 博客描述
    private String description;
    // 分类
    @ManyToOne
    private Type type;
    // 标签
    // CascadeType.PERSIST 级联新增
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();
    // 标签 id
    // 不会录入数据库
    @Transient
    private String tagIds;
    // 用户
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    public Blog() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    /**
     * 将博客标签进行转换
     * 变换位 1，2，3，4 类似模式
     * @param tagList 博客标签数组
     * @return 博客标签字符串
     */
    private String tagsToIds(List<Tag> tagList) {
        if (!tagList.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tagList) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag);
            }
        }
        return tagIds;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", comment=" + comment +
                ", published=" + published +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", description='" + description + '\'' +
                ", tagIds='" + tagIds + '\'' +
                ", comments=" + comments +
                '}';
    }
}
