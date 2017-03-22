package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.EnterpriseInfoType;
import com.tuotiansudai.repository.model.LoanerEnterpriseInfoModel;
import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreateLoanerEnterpriseInfoDto {
    @NotEmpty
    private String companyName;

    @NotEmpty
    private EnterpriseInfoType enterpriseType;

    @NotEmpty
    private String address;

    @NotEmpty
    private String purpose;

    private String factoringCompanyName;

    private String factoringCompanyDesc;

    public LoanCreateLoanerEnterpriseInfoDto() {
    }

    public LoanCreateLoanerEnterpriseInfoDto(LoanerEnterpriseInfoModel loanerEnterpriseInfoModel) {
        this.companyName = loanerEnterpriseInfoModel.getCompanyName();
        this.enterpriseType = loanerEnterpriseInfoModel.getEnterpriseType();
        this.address = loanerEnterpriseInfoModel.getAddress();
        this.purpose = loanerEnterpriseInfoModel.getPurpose();
        this.factoringCompanyName = loanerEnterpriseInfoModel.getFactoringCompanyName();
        this.factoringCompanyDesc = loanerEnterpriseInfoModel.getFactoringCompanyDesc();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public EnterpriseInfoType getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(EnterpriseInfoType enterpriseType) {
        this.enterpriseType = enterpriseType;
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
}
