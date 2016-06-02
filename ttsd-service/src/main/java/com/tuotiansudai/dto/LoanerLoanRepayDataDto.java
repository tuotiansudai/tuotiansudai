package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.List;

public class LoanerLoanRepayDataDto extends BaseDataDto {

    private long loanId;

    private String loanerBalance = "0";

    private boolean isNormalRepayEnabled;

    private boolean isAdvanceRepayEnabled;

    private String normalRepayAmount = "0";

    private String advanceRepayAmount = "0";

    private List<LoanerLoanRepayDataItemDto> records = Lists.newArrayList();

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoanerBalance() {
        return loanerBalance;
    }

    public void setLoanerBalance(String loanerBalance) {
        this.loanerBalance = loanerBalance;
    }

    @JsonProperty(value = "isNormalRepayEnabled")
    public boolean isNormalRepayEnabled() {
        return isNormalRepayEnabled;
    }

    public void setNormalRepayEnabled(boolean normalRepayEnabled) {
        isNormalRepayEnabled = normalRepayEnabled;
    }

    @JsonProperty(value = "isAdvanceRepayEnabled")
    public boolean isAdvanceRepayEnabled() {
        return isAdvanceRepayEnabled;
    }

    public void setAdvanceRepayEnabled(boolean advanceRepayEnabled) {
        isAdvanceRepayEnabled = advanceRepayEnabled;
    }

    public String getNormalRepayAmount() {
        return normalRepayAmount;
    }

    public void setNormalRepayAmount(String normalRepayAmount) {
        this.normalRepayAmount = normalRepayAmount;
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
