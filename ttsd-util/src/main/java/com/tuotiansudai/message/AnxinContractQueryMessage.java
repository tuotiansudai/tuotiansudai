package com.tuotiansudai.message;

import java.io.Serializable;
import java.util.List;

public class AnxinContractQueryMessage implements Serializable{
    private long businessId;
    private List<String> batchNoList;
    private String anxinContractType;

    public AnxinContractQueryMessage() {
    }

    public AnxinContractQueryMessage(long businessId, List<String> batchNoList, String anxinContractType) {
        this.businessId = businessId;
        this.batchNoList = batchNoList;
        this.anxinContractType = anxinContractType;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public List<String> getBatchNoList() {
        return batchNoList;
    }

    public void setBatchNoList(List<String> batchNoList) {
        this.batchNoList = batchNoList;
    }

    public String getAnxinContractType() {
        return anxinContractType;
    }

    public void setAnxinContractType(String anxinContractType) {
        this.anxinContractType = anxinContractType;
    }
}
