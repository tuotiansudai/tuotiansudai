package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class TransferApplicationResponseDataDto extends BaseResponseDataDto {
    private Integer index;
    private Integer pageSize;
    private Integer totalCount;

    @ApiModelProperty(value = "债权转让记录", example = "list")
    private List<TransferApplicationRecordResponseDataDto> transferApplication;

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

    public List<TransferApplicationRecordResponseDataDto> getTransferApplication() {
        return transferApplication;
    }

    public void setTransferApplication(List<TransferApplicationRecordResponseDataDto> transferApplication) {
        this.transferApplication = transferApplication;
    }
}
