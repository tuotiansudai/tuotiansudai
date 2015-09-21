package com.tuotiansudai.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class LoanRepayDataDto extends BaseDataDto {

    private List<LoanRepayDataItemDto> records = Lists.newArrayList();

    public List<LoanRepayDataItemDto> getRecords() {
        return records;
    }

    public void setRecords(List<LoanRepayDataItemDto> records) {
        this.records = records;
    }
}
