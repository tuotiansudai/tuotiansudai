package com.tuotiansudai.api.dto;

public class BaseParamDto {
    private Integer index;
    private Integer pageSize;
    private BaseParam baseParam;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public BaseParam getBaseParam() {
        return baseParam;
    }

    public void setBaseParam(BaseParam baseParam) {
        this.baseParam = baseParam;
    }
}
