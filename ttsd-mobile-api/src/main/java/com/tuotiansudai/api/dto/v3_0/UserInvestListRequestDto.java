package com.tuotiansudai.api.dto.v3_0;


import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.repository.model.LoanStatus;
import io.swagger.annotations.ApiModelProperty;

public class UserInvestListRequestDto extends BaseParamDto {

    private Integer index;

    private Integer pageSize;

    @ApiModelProperty(value = "标的状态", example = "REPAYING(收款中),RAISING(投标中),COMPLETE(已完成)")
    private LoanStatus status;

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

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
