package com.tuotiansudai.repository.model;

import java.util.Date;

/**
 * Created by huoxuanbo on 16/5/16.
 */
public class LicaiquanArticleContentModel {
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
    private String show_picture;
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
}
