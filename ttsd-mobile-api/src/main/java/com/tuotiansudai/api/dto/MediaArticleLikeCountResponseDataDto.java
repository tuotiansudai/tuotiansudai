package com.tuotiansudai.api.dto;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;

import java.io.Serializable;

public class MediaArticleLikeCountResponseDataDto extends BaseResponseDataDto implements Serializable {
    private Long articleId;
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
