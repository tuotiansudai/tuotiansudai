package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanCreateLoanerEnterpriseInfoDto;

import java.io.Serializable;

public class LoanerEnterpriseInfoModel implements Serializable {
    private long id;
    private long loanId;
    private String companyName;
    private String address;
    private String purpose;
    private String factoringCompanyName;
    private String factoringCompanyDesc;

    public LoanerEnterpriseInfoModel() {
    }

    public LoanerEnterpriseInfoModel(long loanId, LoanCreateLoanerEnterpriseInfoDto dto) {
        this.loanId = loanId;
        this.companyName = dto.getCompanyName();
        this.factoringCompanyName = dto.getFactoringCompanyName();
        this.factoringCompanyDesc = dto.getFactoringCompanyDesc();
        this.address = dto.getAddress();
        this.purpose = dto.getPurpose();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
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
