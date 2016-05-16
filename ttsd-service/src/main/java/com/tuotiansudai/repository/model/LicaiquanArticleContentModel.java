package com.tuotiansudai.repository.model;

import java.util.Date;

/**
 * Created by huoxuanbo on 16/5/16.
 */
public class LicaiquanArticleContentModel {
    /**
     * 文章ID
     */
    private long id;
    /**
     * 文章标题
     */
    private String title;
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
    private String thumbnail;
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

    public LicaiquanArticleContentModel() {
    }

    public LicaiquanArticleContentModel(long id, String title, String author, ArticleSectionType articleSection, String source, boolean carousel, String thumbnail, String showPicture, String content, Date createTime, Date updateTime) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.articleSection = articleSection;
        this.source = source;
        this.carousel = carousel;
        this.thumbnail = thumbnail;
        this.showPicture = showPicture;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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
}
