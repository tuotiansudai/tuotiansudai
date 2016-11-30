package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class LoanListResponseDataDto extends BaseResponseDataDto{
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;
    @ApiModelProperty(value = "标的列表", example = "list")
    private List<LoanResponseDataDto> loanList;

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

    public List<LoanResponseDataDto> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<LoanResponseDataDto> loanList) {
        this.loanList = loanList;
    }
}
