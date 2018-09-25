package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.MediaArticleListResponseDataDto;
import com.tuotiansudai.api.dto.MediaArticleResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.SubArticleSectionType;


public interface MobileAppMediaCenterService {

    BaseResponseDto<MediaArticleListResponseDataDto> obtainCarouselArticle();

    BaseResponseDto<MediaArticleListResponseDataDto> obtainArticleList(ArticleSectionType articleSectionType, SubArticleSectionType subSection,int index, int pageSize);

    BaseResponseDto<MediaArticleResponseDataDto> obtainArticleContent(long articleId);

}
