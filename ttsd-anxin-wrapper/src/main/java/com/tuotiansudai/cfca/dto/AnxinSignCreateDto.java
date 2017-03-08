package com.tuotiansudai.cfca.dto;


import com.tuotiansudai.repository.model.AnxinContractType;

public class AnxinSignCreateDto {

    private Long businessId;
    private AnxinContractType anxinContractType;

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
