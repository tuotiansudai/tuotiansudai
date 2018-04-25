package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanerEnterpriseInfoModel;
import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreateLoanerEnterpriseInfoDto {
    @NotEmpty
    private String companyName;

    @NotEmpty
    private String address;

    @NotEmpty
    private String purpose;

    @NotEmpty
    private String source;

    private String factoringCompanyName;

    private String factoringCompanyDesc;

    public LoanCreateLoanerEnterpriseInfoDto() {
    }

    public LoanCreateLoanerEnterpriseInfoDto(LoanerEnterpriseInfoModel loanerEnterpriseInfoModel) {
        this.companyName = loanerEnterpriseInfoModel.getCompanyName();
        this.address = loanerEnterpriseInfoModel.getAddress();
        this.purpose = loanerEnterpriseInfoModel.getPurpose();
        this.source = loanerEnterpriseInfoModel.getSource();
        this.factoringCompanyName = loanerEnterpriseInfoModel.getFactoringCompanyName();
        this.factoringCompanyDesc = loanerEnterpriseInfoModel.getFactoringCompanyDesc();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
