package com.tuotiansudai.api.dto;

import java.util.List;

public class UserInvestListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    private List<UserInvestRecordResponseDataDto> investList;

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

    public List<UserInvestRecordResponseDataDto> getInvestList() {
        return investList;
    }

    public void setInvestList(List<UserInvestRecordResponseDataDto> investList) {
        this.investList = investList;
    }
}
