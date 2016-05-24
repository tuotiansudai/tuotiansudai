package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LicaiquanArticleModel;

import java.util.List;


public class ArticlePaginationDataDto extends BasePaginationDataDto<LiCaiQuanArticleDto>{
    private long sumAmount;

    public ArticlePaginationDataDto(int index, int pageSize, long count, List<LiCaiQuanArticleDto> records) {
        super(index, pageSize, count, records);
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
    }
}
