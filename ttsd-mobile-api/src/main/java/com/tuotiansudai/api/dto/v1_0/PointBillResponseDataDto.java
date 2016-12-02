package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PointBillResponseDataDto extends BaseResponseDataDto {

    private Integer index;
    private Integer pageSize;
    private Long totalCount;

    @ApiModelProperty(value = "积分记录", example = "list")
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
