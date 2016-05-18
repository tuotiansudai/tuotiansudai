package com.tuotiansudai.service;

import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;

public interface LiCaiQuanArticleService {
    BaseDto<PayDataDto> retrace(long articleId);

    void createArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    LiCaiQuanArticleDto obtainEditArticleDto(long articleId);
}
