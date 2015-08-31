package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class InvestRecordRequestDto {
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private Integer index;
    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private Integer pageSize;
    @NotEmpty
    private String loanId;

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

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
