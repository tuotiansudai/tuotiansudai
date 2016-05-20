package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;

import java.util.List;

public interface LiCaiQuanArticleService {
    BaseDto<PayDataDto> retrace(long articleId);

    void createArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    ArticlePaginationDataDto findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType,int size, int index);

}
