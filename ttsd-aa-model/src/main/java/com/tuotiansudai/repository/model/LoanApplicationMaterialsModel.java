package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanConsumeBorrowApplyDto;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;

public class LoanApplicationMaterialsModel implements Serializable {

    private long id;
    private long loanApplicationId;
    private String identityProveUrls;
    private String incomeProveUrls;
    private String creditProveUrls;
    private String marriageProveUrls;
    private String propertyProveUrls;
    private String togetherProveUrls;
    private String driversLicense;

    public LoanApplicationMaterialsModel() {
    }

    public LoanApplicationMaterialsModel(long loanApplicationId, LoanConsumeBorrowApplyDto loanConsumeBorrowApplyDto) {
        this.loanApplicationId = loanApplicationId;
        this.identityProveUrls = String.join(",", loanConsumeBorrowApplyDto.getIdentityProveUrls());
        this.incomeProveUrls = String.join(",", loanConsumeBorrowApplyDto.getIncomeProveUrls());
        this.creditProveUrls = String.join(",", loanConsumeBorrowApplyDto.getCreditProveUrls());
        this.marriageProveUrls = String.join(",", loanConsumeBorrowApplyDto.getMarriageProveUrls());
        this.propertyProveUrls = String.join(",", loanConsumeBorrowApplyDto.getPropertyProveUrls());
        this.togetherProveUrls = String.join(",", loanConsumeBorrowApplyDto.getTogetherProveUrls());
        this.driversLicense = String.join(",", loanConsumeBorrowApplyDto.getDriversLicense());
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
