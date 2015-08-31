package com.tuotiansudai.dto;

import java.util.List;

public class InvestRecordResponseDto extends BaseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<InvestRecordDto> investRecordDtoList;

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

    public List<InvestRecordDto> getInvestRecordDtoList() {
        return investRecordDtoList;
    }

    public void setInvestRecordDtoList(List<InvestRecordDto> investRecordDtoList) {
        this.investRecordDtoList = investRecordDtoList;
    }
}
