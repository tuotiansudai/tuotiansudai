package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class PointExchangeListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;

    public List<PointExchangeRecordResponseDataDto> getPointExchange() {
        return pointExchange;
    }

    public void setPointExchange(List<PointExchangeRecordResponseDataDto> pointExchange) {
        this.pointExchange = pointExchange;
    }

    private List<PointExchangeRecordResponseDataDto> pointExchange;

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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }




}
