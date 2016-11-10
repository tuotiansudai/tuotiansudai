package com.tuotiansudai.dto.sms;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class GenerateContractErrorNotifyDto implements Serializable {

    @NotEmpty
    private List<String> mobiles;

    @NotEmpty
    private long businessId;

    public GenerateContractErrorNotifyDto() {
    }

    public GenerateContractErrorNotifyDto(List<String> mobiles, long businessId) {
        this.mobiles = mobiles;
        this.businessId = businessId;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

}
