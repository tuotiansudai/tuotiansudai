package com.tuotiansudai.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class MediaArticleResponseDataDto extends BaseResponseDataDto implements Serializable {

    @ApiModelProperty(value = "文章ID", example = "111")
    private Long articleId;

    @ApiModelProperty(value = "文章标题", example = "拓天速贷第二期全国排行活动正式启动")
    private String title;

    @ApiModelProperty(value = "作者", example = "张三")
    private String author;

    @ApiModelProperty(value = "缩略图", example = "url")
    private String thumbPicture;

    @ApiModelProperty(value = "展示图", example = "url")
    private String showPicture;

    @ApiModelProperty(value = "栏目", example = "ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯")
    private ArticleSectionType section;

    @ApiModelProperty(value = "文章内容", example = "拓天速贷第二期全国排行活动正式启动")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间", example = "2016-05-06 12:32:58")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间", example = "2016-05-06 12:32:58")
    private Date updatedTime;

    @ApiModelProperty(value = "文章来源", example = "baidu.com")
    private String source;

    @ApiModelProperty(value = "点赞数", example = "100")
    private long likeCount;

    @ApiModelProperty(value = "阅读数", example = "150")
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

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
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

    public MediaArticleResponseDataDto(){}

    public MediaArticleResponseDataDto(LicaiquanArticleModel model){
        this.articleId = model.getId();
        this.title = model.getTitle();
        this.author = model.getAuthor();
        this.thumbPicture = model.getThumb();
        this.showPicture = model.getShowPicture();
        this.section = model.getSection();
        this.content = model.getContent();
        this.createTime = model.getCreatedTime();
        this.updatedTime = model.getUpdatedTime();
        this.source = model.getSource();
    }
}
