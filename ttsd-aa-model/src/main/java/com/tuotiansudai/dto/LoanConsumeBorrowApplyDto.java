package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.Marriage;

import java.util.ArrayList;
import java.util.List;

public class LoanConsumeBorrowApplyDto extends LoanApplicationDto{

    private List<String> identityProveUrls = new ArrayList<>();
    private List<String> incomeProveUrls = new ArrayList<>();
    private List<String> creditProveUrls = new ArrayList<>();
    private List<String> marriageProveUrls = new ArrayList<>();
    private List<String> propertyProveUrls = new ArrayList<>();
    private List<String> togetherProveUrls = new ArrayList<>();
    private List<String> driversLicense = new ArrayList<>();

    public List<String> getIdentityProveUrls() {
        return identityProveUrls;
    }

    public void setIdentityProveUrls(List<String> identityProveUrls) {
        this.identityProveUrls = identityProveUrls;
    }

    public List<String> getIncomeProveUrls() {
        return incomeProveUrls;
    }

    public void setIncomeProveUrls(List<String> incomeProveUrls) {
        this.incomeProveUrls = incomeProveUrls;
    }

    public List<String> getCreditProveUrls() {
        return creditProveUrls;
    }

    public void setCreditProveUrls(List<String> creditProveUrls) {
        this.creditProveUrls = creditProveUrls;
    }

    public List<String> getMarriageProveUrls() {
        return marriageProveUrls;
    }

    public void setMarriageProveUrls(List<String> marriageProveUrls) {
        this.marriageProveUrls = marriageProveUrls;
    }

    public List<String> getPropertyProveUrls() {
        return propertyProveUrls;
    }

    public void setPropertyProveUrls(List<String> propertyProveUrls) {
        this.propertyProveUrls = propertyProveUrls;
    }

    public List<String> getTogetherProveUrls() {
        return togetherProveUrls;
    }

    public void setTogetherProveUrls(List<String> togetherProveUrls) {
        this.togetherProveUrls = togetherProveUrls;
    }

    public List<String> getDriversLicense() {
        return driversLicense;
    }

    public void setDriversLicense(List<String> driversLicense) {
        this.driversLicense = driversLicense;
    }
}
