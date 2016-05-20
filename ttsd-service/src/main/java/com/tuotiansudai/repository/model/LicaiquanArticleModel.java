package com.tuotiansudai.repository.model;

import java.util.Date;

/**
 * Created by huoxuanbo on 16/5/13.
 */
public class LicaiquanArticleModel {
    /**
     * 文章ID, 主键
     */
    private long id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章创建人
     */
    private String creator;
    /**
     * 文章审核人
     */
    private String checker;
    /**
     * 文章作者
     */
    private String author;
    /**
     * 文章所属栏目
     */
    private ArticleSectionType articleSection;
    /**
     * 文章来源
     */
    private String source;
    /**
     * 是否轮播
     */
    private boolean carousel;
    /**
     * 缩略图
     */
    private String thumb;
    /**
     * 展示图
     */
    private String showPicture;
    /**
     * 文章内容，html5格式
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否已删除
     */
    private boolean isDeleted;

    public LicaiquanArticleModel() {
    }

    public LicaiquanArticleModel(long id, String title, String creator, String checker, String author, ArticleSectionType articleSection, String source, boolean carousel, String thumb, String showPicture, String content, Date createTime) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.checker = checker;
        this.author = author;
        this.articleSection = articleSection;
        this.source = source;
        this.carousel = carousel;
        this.thumb = thumb;
        this.showPicture = showPicture;
        this.content = content;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArticleSectionType getArticleSection() {
        return articleSection;
    }

    public void setArticleSection(ArticleSectionType articleSection) {
        this.articleSection = articleSection;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isCarousel() {
        return carousel;
    }

    public void setCarousel(boolean carousel) {
        this.carousel = carousel;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getShowPicture() {
        return showPicture;
    }

    public void setShowPicture(String showPicture) {
        this.showPicture = showPicture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
