package com.tuotiansudai.repository.model;

import java.util.Date;

public class LicaiquanArticleCommentModel {
    private long id;
    private long articleId;
    private String comment;
    private Date createTime;

    public LicaiquanArticleCommentModel() {
    }

    public LicaiquanArticleCommentModel(long articleId, String comment, Date createTime) {
        this.articleId = articleId;
        this.comment = comment;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
