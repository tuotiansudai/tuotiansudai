package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AnxinContractType;

import java.io.Serializable;

public class AnxinQueryContractDto implements Serializable{

    private Long businessId;
    private AnxinContractType anxinContractType;

    public AnxinQueryContractDto() {
    }

    public AnxinQueryContractDto(Long businessId, AnxinContractType anxinContractType) {
        this.businessId = businessId;
        this.anxinContractType = anxinContractType;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public AnxinContractType getAnxinContractType() {
        return anxinContractType;
    }

    public void setAnxinContractType(AnxinContractType anxinContractType) {
        this.anxinContractType = anxinContractType;
    }
}
