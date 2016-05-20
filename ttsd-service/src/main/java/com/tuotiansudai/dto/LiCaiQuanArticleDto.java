package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;

import java.io.Serializable;
import java.util.Date;

public class LiCaiQuanArticleDto implements Serializable{
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
    private Date modifyTime;
    private String checker;

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

    public LiCaiQuanArticleDto(){};

    public LiCaiQuanArticleDto(LicaiquanArticleModel licaiquanArticleModel){
        this.articleId = licaiquanArticleModel.getId();
        this.title = licaiquanArticleModel.getTitle();
        this.author = licaiquanArticleModel.getAuthor();
        this.thumbPicture = licaiquanArticleModel.getThumb();
        this.showPicture = licaiquanArticleModel.getShowPicture();
        this.carousel = licaiquanArticleModel.isCarousel();
        this.section = licaiquanArticleModel.getArticleSection();
        this.content = licaiquanArticleModel.getContent();
        this.source = licaiquanArticleModel.getSource();
        this.modifyTime = licaiquanArticleModel.getUpdateTime();
        this.checker = licaiquanArticleModel.getChecker();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }
}
