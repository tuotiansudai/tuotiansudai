package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.ArticleSectionType;

import java.util.List;

public interface LiCaiQuanArticleService {
    BaseDto<PayDataDto> retrace(long articleId);

    void createArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    List<LiCaiQuanArticleDto> findLiCaiQuanArticleDto(String title, ArticleSectionType articleSectionType,long startId, int size);

}
