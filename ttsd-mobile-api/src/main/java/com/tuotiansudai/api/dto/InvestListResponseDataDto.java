package com.tuotiansudai.api.dto;

import java.util.List;

public class InvestListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<InvestRecordResponseDataDto> investRecord;

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

    public List<InvestRecordResponseDataDto> getInvestRecord() {
        return investRecord;
    }

    public void setInvestRecord(List<InvestRecordResponseDataDto> investRecord) {
        this.investRecord = investRecord;
    }


}
