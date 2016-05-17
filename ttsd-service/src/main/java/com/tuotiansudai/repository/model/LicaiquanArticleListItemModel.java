package com.tuotiansudai.repository.model;

import java.util.Date;

/**
 * Created by huoxuanbo on 16/5/16.
 */
public class LicaiquanArticleListItemModel {
    /**
     * 主键
     */
    private long id;
    /**
     * 文章ID
     */
    private long articleId;
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
     * 文章所属栏目
     */
    private ArticleSectionType articleSection;
    /**
     * 点赞数
     */
    private int likeCount;
    /**
     * 阅读数
     */
    private int readCount;
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

    public LicaiquanArticleListItemModel() {
    }

    public LicaiquanArticleListItemModel(long id, long articleId, String title, String creator, String checker, ArticleSectionType articleSection, int likeCount, int readCount, Date createTime, Date updateTime, boolean isDeleted) {
        this.id = id;
        this.articleId = articleId;
        this.title = title;
        this.creator = creator;
        this.checker = checker;
        this.articleSection = articleSection;
        this.likeCount = likeCount;
        this.readCount = readCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
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

    public ArticleSectionType getArticleSection() {
        return articleSection;
    }

    public void setArticleSection(ArticleSectionType articleSection) {
        this.articleSection = articleSection;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
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
