package com.tuotiansudai.api.dto;

import java.util.List;

public class PointBillResponseDataDto extends BaseResponseDataDto {

    private Integer index;
    private Integer pageSize;
    private Long totalCount;
    private List<PointBillRecordResponseDataDto> pointBills;

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

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<PointBillRecordResponseDataDto> getPointBills() {
        return pointBills;
    }

    public void setPointBills(List<PointBillRecordResponseDataDto> pointBills) {
        this.pointBills = pointBills;
    }
}
