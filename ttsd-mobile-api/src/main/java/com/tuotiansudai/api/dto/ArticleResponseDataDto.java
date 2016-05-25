package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;

import java.io.Serializable;
import java.util.Date;

public class ArticleResponseDataDto extends BaseResponseDataDto implements Serializable {
    private Long articleId;
    private String title;
    private String author;
    private String thumbPicture;
    private String showPicture;
    private ArticleSectionType section;
    private String content;
    private Date createTime;
    private String source;
    private long likeCount;
    private long readCount;

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


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public ArticleResponseDataDto(){}

    public ArticleResponseDataDto(LicaiquanArticleModel model){
        this.articleId = model.getId();
        this.title = model.getTitle();
        this.author = model.getAuthor();
        this.thumbPicture = model.getThumb();
        this.showPicture = model.getShowPicture();
        this.section = model.getSection();
        this.content = model.getContent();
        this.createTime = model.getCreatedTime();
        this.source = model.getSource();
    }
}
