package com.tuotiansudai.api.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.model.ArticleSectionType;

import java.util.List;

public interface MobileAppMediaCenterService {

    List<LiCaiQuanArticleDto> obtainCarouselArticle();

    BaseDto<BasePaginationDataDto> obtainArticleList(ArticleSectionType articleSectionType,int index,int pageSize);

    LiCaiQuanArticleDto obtainArticleContent(long articleId);

}
