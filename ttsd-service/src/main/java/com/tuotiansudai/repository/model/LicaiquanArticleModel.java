package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LiCaiQuanArticleDto;

import java.io.Serializable;
import java.util.Date;

public class LicaiquanArticleModel implements Serializable {

    private static final long serialVersionUID = 8615696510049372781l;
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
    private String creatorLoginName;
    /**
     * 文章审核人
     */
    private String checkerLoginName;
    /**
     * 文章作者
     */
    private String author;
    /**
     * 文章所属栏目
     */
    private ArticleSectionType section;
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
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 是否已删除
     */
    private boolean deleted;

    public LicaiquanArticleModel() {
    }

    public LicaiquanArticleModel(long id, String title, String creatorLoginName, String checkerLoginName, String author, ArticleSectionType section, String source, boolean carousel, String thumb, String showPicture, String content, Date createdTime) {
        this.id = id;
        this.title = title;
        this.creatorLoginName = creatorLoginName;
        this.checkerLoginName = checkerLoginName;
        this.author = author;
        this.section = section;
        this.source = source;
        this.carousel = carousel;
        this.thumbnail = thumb;
        this.showPicture = showPicture;
        this.content = content;
        this.createdTime = createdTime;
        this.updatedTime = createdTime;
        this.deleted = false;
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

    public String getCreatorLoginName() {
        return creatorLoginName;
    }

    public void setCreatorLoginName(String creatorLoginName) {
        this.creatorLoginName = creatorLoginName;
    }

    public String getCheckerLoginName() {
        return checkerLoginName;
    }

    public void setCheckerLoginName(String checkerLoginName) {
        this.checkerLoginName = checkerLoginName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArticleSectionType getSection() {
        return section;
    }

    public void setSection(ArticleSectionType section) {
        this.section = section;
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

    public void setThumbnail(String thumb) {
        this.thumbnail = thumb;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
