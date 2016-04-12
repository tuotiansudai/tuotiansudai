package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

public class LoanRepayDataDto extends BaseDataDto {

    private long loanAgentBalance;

    private long expectedNormalRepayAmount;

    private long expectedAdvanceRepayAmount;

    private boolean hasWaitPayLoanRepay;

    private List<LoanRepayDataItemDto> records = Lists.newArrayList();

    public long getLoanAgentBalance() {
        return loanAgentBalance;
    }

    public void setLoanAgentBalance(long loanAgentBalance) {
        this.loanAgentBalance = loanAgentBalance;
    }

    public long getExpectedNormalRepayAmount() {
        return expectedNormalRepayAmount;
    }

    public void setExpectedNormalRepayAmount(long expectedNormalRepayAmount) {
        this.expectedNormalRepayAmount = expectedNormalRepayAmount;
    }

    public long getExpectedAdvanceRepayAmount() {
        return expectedAdvanceRepayAmount;
    }

    public void setExpectedAdvanceRepayAmount(long expectedAdvanceRepayAmount) {
        this.expectedAdvanceRepayAmount = expectedAdvanceRepayAmount;
    }

    @JsonProperty(value = "hasWaitPayLoanRepay")
    public boolean isHasWaitPayLoanRepay() {
        return hasWaitPayLoanRepay;
    }

    public void setHasWaitPayLoanRepay(boolean hasWaitPayLoanRepay) {
        this.hasWaitPayLoanRepay = hasWaitPayLoanRepay;
    }

    public List<LoanRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<LoanRepayDataItemDto> records) {
        this.records = records;
    }


}
