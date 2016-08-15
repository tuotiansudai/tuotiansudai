package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class InvestRepayDataDto extends BaseDataDto {

    private String sumActualInterest;

    private String expectedFee;

    private String redInterest;

    private List<InvestRepayDataItemDto> records = Lists.newArrayList();

    public List<InvestRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<InvestRepayDataItemDto> records) {
        this.records = records;
    }
}
