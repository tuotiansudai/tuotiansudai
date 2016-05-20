package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class InvestRepayDataDto extends BaseDataDto {

    private List<InvestRepayDataItemDto> records = Lists.newArrayList();

    private List<String> couponDescriptionList;

    private String expectedInterest;

    private String actualInterest;

    public List<InvestRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<InvestRepayDataItemDto> records) {
        this.records = records;
    }

    public List<String> getCouponDescriptionList() { return couponDescriptionList; }

    public void setCouponDescriptionList(List<String> couponDescriptionList) { this.couponDescriptionList = couponDescriptionList; }

    public String getExpectedInterest() { return expectedInterest; }

    public void setExpectedInterest(String expectedInterest) { this.expectedInterest = expectedInterest; }

    public String getActualInterest() { return actualInterest; }

    public void setActualInterest(String actualInterest) { this.actualInterest = actualInterest; }
}
