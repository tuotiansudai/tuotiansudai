package com.tuotiansudai.dto;

import java.util.List;

public class InvestPaginationDataDto extends BasePaginationDataDto<InvestPaginationItemDataDto>{
    private long sumAmount;

    public InvestPaginationDataDto(int index, int pageSize, long count, List<InvestPaginationItemDataDto> records) {
        super(index, pageSize, count, records);
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
    }
}
