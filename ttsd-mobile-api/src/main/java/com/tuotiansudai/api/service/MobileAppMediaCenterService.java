package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.ArticleListResponseDataDto;
import com.tuotiansudai.api.dto.ArticleResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.repository.model.ArticleSectionType;


public interface MobileAppMediaCenterService {

    BaseResponseDto<ArticleListResponseDataDto> obtainCarouselArticle();

    BaseResponseDto<ArticleListResponseDataDto> obtainArticleList(ArticleSectionType articleSectionType,int index,int pageSize);

    BaseResponseDto<ArticleResponseDataDto> obtainArticleContent(long articleId);

}
