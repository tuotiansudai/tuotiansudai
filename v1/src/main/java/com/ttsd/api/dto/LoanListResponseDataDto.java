package com.ttsd.api.dto;

import java.util.List;

public class LoanListResponseDataDto extends BaseResponseDataDto{
    private Integer index;
    private Integer pageSize;
    private boolean hasNextPage;
    private List<LoanDto> loanList;

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

    public boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public List<LoanDto> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<LoanDto> loanList) {
        this.loanList = loanList;
    }
}
