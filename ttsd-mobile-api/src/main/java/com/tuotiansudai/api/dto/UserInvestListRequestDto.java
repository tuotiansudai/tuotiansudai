package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.TransferStatus;

import java.util.List;

public class UserInvestListRequestDto extends BaseParamDto {
    private Integer index;
    private Integer pageSize;

    private List<TransferStatus> transferStatuses;

    public Integer  getIndex() {
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

    public List<TransferStatus> getTransferStatuses() {
        return transferStatuses;
    }

    public void setTransferStatuses(List<TransferStatus> transferStatuses) {
        this.transferStatuses = transferStatuses;
    }
}
