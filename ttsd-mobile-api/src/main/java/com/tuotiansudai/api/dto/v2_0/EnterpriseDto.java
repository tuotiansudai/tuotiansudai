package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.repository.model.LoanerEnterpriseDetailsModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class EnterpriseDto implements Serializable {

    @ApiModelProperty(value = "借款人", example = "wangtuotian")
    private String juristicPerson;

    @ApiModelProperty(value = "地址", example = "beijing")
    private String address;

    @ApiModelProperty(value = "目的", example = "盖房子")
    private String purpose;

    public EnterpriseDto(){}

    public EnterpriseDto(LoanerEnterpriseDetailsModel loanerEnterpriseDetailsModel){
        this.juristicPerson = loanerEnterpriseDetailsModel.getJuristicPerson();
        this.address = loanerEnterpriseDetailsModel.getAddress();
        this.purpose = loanerEnterpriseDetailsModel.getPurpose();
    }

    public String getJuristicPerson() {
        return juristicPerson;
    }

    public void setJuristicPerson(String juristicPerson) {
        this.juristicPerson = juristicPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
