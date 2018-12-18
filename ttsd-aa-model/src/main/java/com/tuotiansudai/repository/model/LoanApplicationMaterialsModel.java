package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanConsumeApplicationDto;

import java.io.Serializable;

public class LoanApplicationMaterialsModel implements Serializable {

    private long id;
    private long loanApplicationId;
    private String identityProveUrls;
    private String incomeProveUrls;
    private String creditProveUrls;
    private String marriageProveUrls;
    private String propertyProveUrls;
    private String togetherLoaner;
    private String togetherProveUrls;
    private String driversLicense;

    public LoanApplicationMaterialsModel() {
    }

    public LoanApplicationMaterialsModel(long loanApplicationId, LoanConsumeApplicationDto loanConsumeApplicationDto) {
        this.loanApplicationId = loanApplicationId;
        this.identityProveUrls = loanConsumeApplicationDto.getIdentityProveUrls();
        this.incomeProveUrls = loanConsumeApplicationDto.getIncomeProveUrls();
        this.creditProveUrls = loanConsumeApplicationDto.getCreditProveUrls();
        this.marriageProveUrls = loanConsumeApplicationDto.getMarriageProveUrls();
        this.propertyProveUrls = loanConsumeApplicationDto.getPropertyProveUrls();
        this.togetherLoaner = loanConsumeApplicationDto.getTogetherLoaner();
        this.togetherProveUrls = loanConsumeApplicationDto.getTogetherProveUrls();
        this.driversLicense = loanConsumeApplicationDto.getDriversLicense();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getIdentityProveUrls() {
        return identityProveUrls;
    }

    public void setIdentityProveUrls(String identityProveUrls) {
        this.identityProveUrls = identityProveUrls;
    }

    public String getIncomeProveUrls() {
        return incomeProveUrls;
    }

    public void setIncomeProveUrls(String incomeProveUrls) {
        this.incomeProveUrls = incomeProveUrls;
    }

    public String getCreditProveUrls() {
        return creditProveUrls;
    }

    public void setCreditProveUrls(String creditProveUrls) {
        this.creditProveUrls = creditProveUrls;
    }

    public String getMarriageProveUrls() {
        return marriageProveUrls;
    }

    public void setMarriageProveUrls(String marriageProveUrls) {
        this.marriageProveUrls = marriageProveUrls;
    }

    public String getPropertyProveUrls() {
        return propertyProveUrls;
    }

    public void setPropertyProveUrls(String propertyProveUrls) {
        this.propertyProveUrls = propertyProveUrls;
    }

    public String getTogetherLoaner() {
        return togetherLoaner;
    }

    public void setTogetherLoaner(String togetherLoaner) {
        this.togetherLoaner = togetherLoaner;
    }

    public String getTogetherProveUrls() {
        return togetherProveUrls;
    }

    public void setTogetherProveUrls(String togetherProveUrls) {
        this.togetherProveUrls = togetherProveUrls;
    }

    public String getDriversLicense() {
        return driversLicense;
    }

    public void setDriversLicense(String driversLicense) {
        this.driversLicense = driversLicense;
    }
}
