package com.tuotiansudai.api.dto;

import java.util.List;

public class InvestRepayListResponseDataDto extends BaseResponseDataDto {

    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<InvestRepayRecordResponseDataDto> recordList;

    public List<InvestRepayRecordResponseDataDto> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<InvestRepayRecordResponseDataDto> recordList) {
        this.recordList = recordList;
    }

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

}
