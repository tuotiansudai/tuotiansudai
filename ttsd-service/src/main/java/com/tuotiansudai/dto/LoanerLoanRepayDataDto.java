package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.List;

public class LoanerLoanRepayDataDto extends BaseDataDto {

    private long loanId;

    private boolean isLoanRepaying;

    private boolean isWaitPayLoanRepayExist;

    private String balance;

    private boolean isAdvanceRepayEnabled;

    private String advanceRepayAmount;

    private List<LoanerLoanRepayDataItemDto> records = Lists.newArrayList();

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    @JsonProperty(value = "isLoanRepaying")
    public boolean isLoanRepaying() {
        return isLoanRepaying;
    }

    public void setLoanRepaying(boolean loanRepaying) {
        isLoanRepaying = loanRepaying;
    }

    @JsonProperty(value = "isWaitPayLoanRepayExist")
    public boolean isWaitPayLoanRepayExist() {
        return isWaitPayLoanRepayExist;
    }

    public void setWaitPayLoanRepayExist(boolean isWaitPayLoanRepayExist) {
        this.isWaitPayLoanRepayExist = isWaitPayLoanRepayExist;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @JsonProperty(value = "isAdvanceRepayEnabled")
    public boolean isAdvanceRepayEnabled() {
        return isAdvanceRepayEnabled;
    }

    public void setAdvanceRepayEnabled(boolean advanceRepayEnabled) {
        isAdvanceRepayEnabled = advanceRepayEnabled;
    }

    public String getAdvanceRepayAmount() {
        return advanceRepayAmount;
    }

    public void setAdvanceRepayAmount(String advanceRepayAmount) {
        this.advanceRepayAmount = advanceRepayAmount;
    }

    public List<LoanerLoanRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<LoanerLoanRepayDataItemDto> records) {
        this.records = records;
    }
}
