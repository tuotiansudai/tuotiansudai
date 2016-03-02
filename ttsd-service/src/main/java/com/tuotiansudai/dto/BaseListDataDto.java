package com.tuotiansudai.dto;

import java.util.List;

public class BaseListDataDto<T> extends BaseDataDto {

    protected List<T> records;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
