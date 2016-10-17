package com.tuotiansudai.service;

import com.tuotiansudai.dto.ArticlePaginationDataDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.ArticleStatus;

import java.util.Map;

public interface LiCaiQuanArticleService {
    void retrace(long articleId);

    void createAndEditArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    LiCaiQuanArticleDto obtainEditArticleDto(long articleId);

    LiCaiQuanArticleDto getArticleContent(long articleId);

    BaseDto<BaseDataDto> checkArticleOnStatus(long articleId, String checkerLoginName);

    void rejectArticle(long articleId, String comment);

    Map<String, String> getAllComments(long articleId);

    ArticlePaginationDataDto findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType, ArticleStatus articleStatus, int size, int index);

    long getLikeCount(long articleId);

    long getReadCount(long articleId);

    void updateLikeCount(long articleId);

    void updateReadCount(long articleId);

    void checkPassAndCreateArticle(long articleId,String checkName);

    void deleteArticle(long articleId);

    LiCaiQuanArticleDto getArticleContentByDataBase(long articleId);
}
