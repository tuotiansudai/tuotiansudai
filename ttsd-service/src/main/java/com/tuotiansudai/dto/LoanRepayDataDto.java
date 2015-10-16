package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.List;

public class LoanRepayDataDto extends BaseDataDto {

    private String expectedAdvanceRepayAmount;

    private boolean hasConfirmingLoanRepay;

    private List<LoanRepayDataItemDto> records = Lists.newArrayList();

    public String getExpectedAdvanceRepayAmount() {
        return expectedAdvanceRepayAmount;
    }

    public void setExpectedAdvanceRepayAmount(String expectedAdvanceRepayAmount) {
        this.expectedAdvanceRepayAmount = expectedAdvanceRepayAmount;
    }

    @JsonProperty(value = "hasConfirmingLoanRepay")
    public boolean isHasConfirmingLoanRepay() {
        return hasConfirmingLoanRepay;
    }

    public void setHasConfirmingLoanRepay(boolean hasConfirmingLoanRepay) {
        this.hasConfirmingLoanRepay = hasConfirmingLoanRepay;
    }

    public List<LoanRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<LoanRepayDataItemDto> records) {
        this.records = records;
    }
}
