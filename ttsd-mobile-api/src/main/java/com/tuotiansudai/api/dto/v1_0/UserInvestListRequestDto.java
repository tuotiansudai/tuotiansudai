package com.tuotiansudai.api.dto.v1_0;

public class UserInvestListRequestDto extends BaseParamDto {
    private Integer index;
    private Integer pageSize;

    public Integer  getIndex() {
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
}
