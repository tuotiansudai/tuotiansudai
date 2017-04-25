package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.repository.model.LoanerEnterpriseInfoModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class EnterpriseInfoDto implements Serializable {

    @ApiModelProperty(value = "借款企业名称", example = "wangtuotian")
    private String companyName;

    @ApiModelProperty(value = "保理公司名称", example = "wangtuotian")
    private String factoringCompanyName;

    @ApiModelProperty(value = "保理公司简介", example = "wangtuotian")
    private String factoringCompanyDesc;

    @ApiModelProperty(value = "借款人营业地址", example = "beijing")
    private String address;

    @ApiModelProperty(value = "借款用途", example = "盖房子")
    private String purpose;

    public EnterpriseInfoDto(){}

    public EnterpriseInfoDto(LoanerEnterpriseInfoModel loanerEnterpriseInfoModel){
        this.companyName = loanerEnterpriseInfoModel.getCompanyName();
        this.factoringCompanyName = loanerEnterpriseInfoModel.getFactoringCompanyName();
        this.factoringCompanyDesc = loanerEnterpriseInfoModel.getFactoringCompanyDesc();
        this.address = loanerEnterpriseInfoModel.getAddress();
        this.purpose = loanerEnterpriseInfoModel.getPurpose();
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFactoringCompanyName() {
        return factoringCompanyName;
    }

    public void setFactoringCompanyName(String factoringCompanyName) {
        this.factoringCompanyName = factoringCompanyName;
    }

    public String getFactoringCompanyDesc() {
        return factoringCompanyDesc;
    }

    public void setFactoringCompanyDesc(String factoringCompanyDesc) {
        this.factoringCompanyDesc = factoringCompanyDesc;
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
