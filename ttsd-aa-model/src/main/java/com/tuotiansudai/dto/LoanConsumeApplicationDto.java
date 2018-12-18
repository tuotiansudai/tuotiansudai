package com.tuotiansudai.dto;


public class LoanConsumeApplicationDto extends LoanApplicationDto{

    private int marriageState;
    private String identityProveUrls;
    private String incomeProveUrls;
    private String creditProveUrls;
    private String marriageProveUrls;
    private String propertyProveUrls;
    private String togetherLoaner;
    private String togetherProveUrls;
    private String driversLicense;

    public int getMarriageState() {
        return marriageState;
    }

    public void setMarriageState(int marriageState) {
        this.marriageState = marriageState;
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
