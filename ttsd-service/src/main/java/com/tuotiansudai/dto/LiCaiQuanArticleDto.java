package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;

import java.io.Serializable;
import java.util.Date;

public class LiCaiQuanArticleDto implements Serializable{
    private static final long serialVersionUID = 5982862020562402898l;
    private Long articleId;
    private String title;
    private String author;
    private String thumbPicture;
    private String showPicture;
    private boolean carousel;
    private ArticleSectionType section;
    private String content;
    private Date createTime;
    private ArticleStatus articleStatus;
    private String source;
    private Date updateTime;
    private String checker;
    private long likeCount;
    private long readCount;
    private String creator;
    private boolean original = false;

    public LiCaiQuanArticleDto(){}

    public LiCaiQuanArticleDto(LicaiquanArticleModel model){
        this.articleId = model.getId();
        this.title = model.getTitle();
        this.author = model.getAuthor();
        this.thumbPicture = model.getThumb();
        this.showPicture = model.getShowPicture();
        this.carousel = model.isCarousel();
        this.section = model.getSection();
        this.content = model.getContent();
        this.createTime = model.getCreatedTime();
        this.articleStatus = ArticleStatus.PUBLISH;
        this.source = model.getSource();
        this.updateTime = model.getUpdatedTime();
        this.checker = model.getCheckerLoginName();
        this.creator = model.getCreatorLoginName();
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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

    public String getThumbPicture() {
        return thumbPicture;
    }

    public void setThumbPicture(String thumbPicture) {
        this.thumbPicture = thumbPicture;
    }

    public String getShowPicture() {
        return showPicture;
    }

    public void setShowPicture(String showPicture) {
        this.showPicture = showPicture;
    }

    public boolean isCarousel() {
        return carousel;
    }

    public void setCarousel(boolean carousel) {
        this.carousel = carousel;
    }

    public ArticleSectionType getSection() {
        return section;
    }

    public void setSection(ArticleSectionType section) {
        this.section = section;
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

    public ArticleStatus getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(ArticleStatus articleStatus) {
        this.articleStatus = articleStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public String getCreator() { return creator; }

    public void setCreator(String creator) { this.creator = creator; }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }
}
