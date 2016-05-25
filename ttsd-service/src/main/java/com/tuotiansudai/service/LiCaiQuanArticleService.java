package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.ArticleSectionType;

import java.util.Map;

public interface LiCaiQuanArticleService {
    BaseDto<PayDataDto> retrace(long articleId);

    LiCaiQuanArticleDto getArticleContent(long articleId);

    BaseDto<BaseDataDto> checkArticleOnStatus(long articleId);

    void rejectArticle(long articleId, String comment);

    Map<String, String> getAllComments(long articleId);

    ArticlePaginationDataDto findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType, int size, int index);

    long getLikeCount(long articleId);

    long getReadCount(long articleId);

    void updateLikeCount(long articleId);

    void updateReadCount(long articleId);

    void createAndEditArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    LiCaiQuanArticleDto obtainEditArticleDto(long articleId);

    void checkPassAndCreateArticle(long articleId,String checkName);

    void deleteArticle(long articleId);

    LiCaiQuanArticleDto getArticleContentByDataBase(long articleId);
}
