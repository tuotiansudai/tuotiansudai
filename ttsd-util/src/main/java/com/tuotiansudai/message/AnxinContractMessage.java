package com.tuotiansudai.message;

import java.io.Serializable;
import java.util.List;

public class AnxinContractMessage implements Serializable{
    private long businessId;
    private String anxinContractType;

    public AnxinContractMessage() {
    }

    public AnxinContractMessage(long businessId, String anxinContractType) {
        this.businessId = businessId;
        this.anxinContractType = anxinContractType;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public String getAnxinContractType() {
        return anxinContractType;
    }

    public void setAnxinContractType(String anxinContractType) {
        this.anxinContractType = anxinContractType;
    }
}
