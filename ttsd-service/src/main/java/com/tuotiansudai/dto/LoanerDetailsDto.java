package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Gender;
import com.tuotiansudai.repository.model.Marriage;

public class LoanerDetailsDto {
    private long loanId;
    private String loanerLoginName;
    private String loanerUserName;
    private Gender loanerGender;
    private int loanerAge;
    private String loanerIdentityNumber;
    private Marriage loanerMarriage;
    private String loanerRegion;
    private String loanerIncome;
    private String loanerEmploymentStatus;

    public LoanerDetailsDto() {
    }

    public LoanerDetailsDto(long loanId, String loanerLoginName, String loanerUserName, Gender loanerGender, int loanerAge, String loanerIdentityNumber, Marriage loanerMarriage, String loanerRegion, String loanerIncome, String loanerEmploymentStatus) {
        this.loanId = loanId;
        this.loanerLoginName = loanerLoginName;
        this.loanerUserName = loanerUserName;
        this.loanerGender = loanerGender;
        this.loanerAge = loanerAge;
        this.loanerIdentityNumber = loanerIdentityNumber;
        this.loanerMarriage = loanerMarriage;
        this.loanerRegion = loanerRegion;
        this.loanerIncome = loanerIncome;
        this.loanerEmploymentStatus = loanerEmploymentStatus;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoanerLoginName() {
        return loanerLoginName;
    }

    public void setLoanerLoginName(String loanerLoginName) {
        this.loanerLoginName = loanerLoginName;
    }

    public String getLoanerUserName() {
        return loanerUserName;
    }

    public void setLoanerUserName(String loanerUserName) {
        this.loanerUserName = loanerUserName;
    }

    public Gender getLoanerGender() {
        return loanerGender;
    }

    public void setLoanerGender(Gender loanerGender) {
        this.loanerGender = loanerGender;
    }

    public int getLoanerAge() {
        return loanerAge;
    }

    public void setLoanerAge(int loanerAge) {
        this.loanerAge = loanerAge;
    }

    public String getLoanerIdentityNumber() {
        return loanerIdentityNumber;
    }

    public void setLoanerIdentityNumber(String loanerIdentityNumber) {
        this.loanerIdentityNumber = loanerIdentityNumber;
    }

    public Marriage getLoanerMarriage() {
        return loanerMarriage;
    }

    public void setLoanerMarriage(Marriage loanerMarriage) {
        this.loanerMarriage = loanerMarriage;
    }

    public String getLoanerRegion() {
        return loanerRegion;
    }

    public void setLoanerRegion(String loanerRegion) {
        this.loanerRegion = loanerRegion;
    }

    public String getLoanerIncome() {
        return loanerIncome;
    }

    public void setLoanerIncome(String loanerIncome) {
        this.loanerIncome = loanerIncome;
    }

    public String getLoanerEmploymentStatus() {
        return loanerEmploymentStatus;
    }

    public void setLoanerEmploymentStatus(String loanerEmploymentStatus) {
        this.loanerEmploymentStatus = loanerEmploymentStatus;
    }
}
