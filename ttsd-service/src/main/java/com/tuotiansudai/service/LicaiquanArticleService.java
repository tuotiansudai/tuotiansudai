package com.tuotiansudai.service;

import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;

import java.util.List;

public interface LiCaiQuanArticleService {
    BaseDto<PayDataDto> retrace(long articleId);

    void createArticle(LiCaiQuanArticleDto liCaiQuanArticleDto);

    List<LicaiquanArticleModel> findLicaiquanArticleModel(String title,String section);
}
