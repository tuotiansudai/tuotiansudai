package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class InvestRepayDataDto extends BaseDataDto {

    private List<InvestRepayDataItemDto> records = Lists.newArrayList();

    private String couponDescription;

    public List<InvestRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<InvestRepayDataItemDto> records) {
        this.records = records;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }
}
