package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class TransferTransfereeRequestDto extends BaseParamDto {

    private Integer index;

    private Integer pageSize;

    @ApiModelProperty(value = "债权转让ID", example = "1001")
    private Long transferApplicationId;

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

    public Long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(Long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

}
