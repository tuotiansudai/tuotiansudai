package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleListItemModel;

import java.io.Serializable;
import java.util.Date;

public class LiCaiQuanArticleDto implements Serializable{
    private long id;
    private String title;
    private String author;
    private String thumbPicture;
    private String showPicture;
    private boolean carousel;
    private ArticleSectionType section;
    private String content;
    private Date createTime;
    private ArticleStatus articleStatus;

    LiCaiQuanArticleDto(){}

    LiCaiQuanArticleDto(LicaiquanArticleListItemModel model){
        this.id = model.getId();
        this.title = model.getTitle();
        this.author = model.getCreator();
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


}
