package com.tuotiansudai.api.dto.v3_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserInvestListResponseDataDto extends BaseResponseDataDto{

    private Integer index;

    private Integer pageSize;

    private Integer totalCount;

    @ApiModelProperty(value = "用户投资列表(包含直投项目和债券转让项目)", example = "list")
    private List<UserInvestRecordResponseDataDto> investList;

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

    public List<UserInvestRecordResponseDataDto> getInvestList() {
        return investList;
    }

    public void setInvestList(List<UserInvestRecordResponseDataDto> investList) {
        this.investList = investList;
    }

}
