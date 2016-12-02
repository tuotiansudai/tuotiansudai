package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RechargeListResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;

    @ApiModelProperty(value = "充值记录列表", example = "list")
    private List<RechargeDetailResponseDataDto> rechargeList;

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

    public List<RechargeDetailResponseDataDto> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<RechargeDetailResponseDataDto> rechargeList) {
        this.rechargeList = rechargeList;
    }
}
