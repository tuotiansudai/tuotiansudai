package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class UserMessageResponseDataDto extends BaseResponseDataDto {
    private long index;
    private long pageSize;
    private long totalCount;
    private List<UserMessageDto> data;

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<UserMessageDto> getData() {
        return data;
    }

    public void setData(List<UserMessageDto> data) {
        this.data = data;
    }
}
