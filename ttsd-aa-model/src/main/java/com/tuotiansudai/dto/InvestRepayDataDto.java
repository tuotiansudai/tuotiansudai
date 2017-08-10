package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class InvestRepayDataDto extends BaseDataDto {

    private String sumActualInterest;

    private String sumExpectedInterest;

    private String redInterest;

    private String levelMessage;

    private String couponMessage;

    private List<InvestRepayDataItemDto> records = Lists.newArrayList();

    public String getSumActualInterest() {
        return sumActualInterest;
    }

    public void setSumActualInterest(String sumActualInterest) {
        this.sumActualInterest = sumActualInterest;
    }

    public String getSumExpectedInterest() {
        return sumExpectedInterest;
    }

    public void setSumExpectedInterest(String sumExpectedInterest) {
        this.sumExpectedInterest = sumExpectedInterest;
    }

    public String getRedInterest() {
        return redInterest;
    }

    public void setRedInterest(String redInterest) {
        this.redInterest = redInterest;
    }

    public String getLevelMessage() {
        return levelMessage;
    }

    public void setLevelMessage(String levelMessage) {
        this.levelMessage = levelMessage;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }

    public List<InvestRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<InvestRepayDataItemDto> records) {
        this.records = records;
    }
}
