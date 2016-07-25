package com.tuotiansudai.api.dto.v1_0;


public class MembershipRequestDto extends BaseParamDto {
    private int index = 1;
    private int pageSize = 10;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
