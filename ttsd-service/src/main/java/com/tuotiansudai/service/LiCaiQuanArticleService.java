package com.tuotiansudai.service;

import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.dto.PayDataDto;

import java.util.Map;

public interface LiCaiQuanArticleService {
    BaseDto<PayDataDto> retrace(long articleId);

    void createAndEditArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    LiCaiQuanArticleDto obtainEditArticleDto(long articleId);

    LiCaiQuanArticleDto getArticleContent(long articleId);

    void rejectArticle(long articleId, String comment);

    Map<String, String> getAllComments(long articleId);

    void updateLikeCount(long articleId);

    void updateReadCount(long articleId);

    Map<String, Integer> getLikeAndReadCount(long articleId);

    void changeArticleStatus(long articleId, ArticleStatus articleStatus);

    

}
