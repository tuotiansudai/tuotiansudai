package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class WithdrawListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Long totalCount;
    private List<WithdrawDetailResponseDataDto> withdrawList;

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

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<WithdrawDetailResponseDataDto> getWithdrawList() {
        return withdrawList;
    }

    public void setWithdrawList(List<WithdrawDetailResponseDataDto> withdrawList) {
        this.withdrawList = withdrawList;
    }
}

