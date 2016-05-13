package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class InvestRepayDataDto extends BaseDataDto {

    private List<InvestRepayDataItemDto> records = Lists.newArrayList();

    private List<String> couponDescriptionList;

    public List<InvestRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<InvestRepayDataItemDto> records) {
        this.records = records;
    }

    public List<String> getCouponDescriptionList() { return couponDescriptionList; }

    public void setCouponDescriptionList(List<String> couponDescriptionList) { this.couponDescriptionList = couponDescriptionList; }
}
