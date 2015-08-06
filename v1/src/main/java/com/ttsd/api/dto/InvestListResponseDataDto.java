package com.ttsd.api.dto;

import java.util.List;

public class InvestListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<InvestRecordDto> investRecord;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<InvestRecordDto> getInvestRecord() {
        return investRecord;
    }

    public void setInvestRecord(List<InvestRecordDto> investRecord) {
        this.investRecord = investRecord;
    }
}
