package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class InvestRepayDataDto extends BaseDataDto {

    private String sumActualInterest;

    private String sumExpectedInterest;

    private String redInterest;

    private String level;

    private String fee;

    private String couponMessage;

    private List<InvestRepayDataItemDto> records = Lists.newArrayList();

    public List<InvestRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<InvestRepayDataItemDto> records) {
        this.records = records;
    }

    public String getSumActualInterest() {
        return sumActualInterest;
    }

    public void setSumActualInterest(String sumActualInterest) {
        this.sumActualInterest = sumActualInterest;
    }

    public String getRedInterest() {
        return redInterest;
    }

    public void setRedInterest(String redInterest) {
        this.redInterest = redInterest;
    }

    public String getSumExpectedInterest() {
        return sumExpectedInterest;
    }

    public void setSumExpectedInterest(String sumExpectedInterest) {
        this.sumExpectedInterest = sumExpectedInterest;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }
}
