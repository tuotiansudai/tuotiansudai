package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.repository.model.LoanStatus;

public class UserInvestListRequestDto extends BaseParamDto {

    private Integer index;

    private Integer pageSize;

    private LoanStatus status;

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

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
