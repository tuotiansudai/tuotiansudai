package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;

import java.util.Map;

public interface LiCaiQuanArticleService {
    BaseDto<PayDataDto> retrace(long articleId);

    void createArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    LiCaiQuanArticleDto getArticleContent(long articleId);

    BaseDto<BaseDataDto> checkArticleOnStatus(long articleId);

    void rejectArticle(long articleId, String comment);

    Map<String, String> getAllComments(long articleId);

    long getLikeCount(long articleId);

    long getReadCount(long articleId);

    void updateLikeCount(long articleId);

    void updateReadCount(long articleId);
}
