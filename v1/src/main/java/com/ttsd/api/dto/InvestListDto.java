package com.ttsd.api.dto;

import java.util.Set;

public class InvestListDto<T extends InvestDto> {
    private Integer index;
    private Integer pageSize;
    private boolean hasNextPage;
    private Set<T> loanList;

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

    public Set<T> getLoanList() {
        return loanList;
    }

    public void setLoanList(Set<T> loanList) {
        this.loanList = loanList;
    }
}
