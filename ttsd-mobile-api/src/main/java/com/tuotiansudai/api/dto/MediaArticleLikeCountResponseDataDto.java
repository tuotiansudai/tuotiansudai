package com.tuotiansudai.api.dto;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MediaArticleLikeCountResponseDataDto extends BaseResponseDataDto implements Serializable {

    @ApiModelProperty(value = "文章ID", example = "1")
    private Long articleId;

    @ApiModelProperty(value = "点赞", example = "1")
    private long likeCount;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }
}
