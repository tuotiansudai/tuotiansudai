package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.TransferStatus;

import java.util.List;

public class TransferApplicationListRequestDto extends BaseParamDto {
    private Integer index;
    private Integer pageSize;
    private String rateLower;
    private String rateUpper;
    private TransferStatus transferStatus;

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

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getRateUpper() { return rateUpper; }

    public void setRateUpper(String rateUpper) { this.rateUpper = rateUpper; }

    public String getRateLower() { return rateLower; }

    public void setRateLower(String rateLower) { this.rateLower = rateLower; }

}

