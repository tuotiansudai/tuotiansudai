package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.MediaArticleListResponseDataDto;
import com.tuotiansudai.api.dto.MediaArticleResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.repository.model.ArticleSectionType;


public interface MobileAppMediaCenterService {

    BaseResponseDto<MediaArticleListResponseDataDto> obtainCarouselArticle();

    BaseResponseDto<MediaArticleListResponseDataDto> obtainArticleList(ArticleSectionType articleSectionType,int index,int pageSize);

    BaseResponseDto<MediaArticleResponseDataDto> obtainArticleContent(long articleId);

}
