package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Gender;
import com.tuotiansudai.repository.model.LoanerDetailsModel;
import com.tuotiansudai.repository.model.Marriage;
import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreateLoanerDetailsRequestDto {

    @NotEmpty
    private String userName;

    @NotEmpty
    private String identityNumber;

    @NotEmpty
    private Gender gender;

    @NotEmpty
    private int age;

    @NotEmpty
    private Marriage marriage;

    @NotEmpty
    private String region;

    @NotEmpty
    private String source;

    @NotEmpty
    private String income;

    @NotEmpty
    private String employmentStatus;

    @NotEmpty
    private String purpose;

    public LoanCreateLoanerDetailsRequestDto() {
    }

    public LoanCreateLoanerDetailsRequestDto(LoanerDetailsModel loanerDetailsModel) {
        this.userName = loanerDetailsModel.getUserName();
        this.identityNumber = loanerDetailsModel.getIdentityNumber();
        this.gender = loanerDetailsModel.getGender();
        this.age = loanerDetailsModel.getAge();
        this.marriage = loanerDetailsModel.getMarriage();
        this.region = loanerDetailsModel.getRegion();
        this.income = loanerDetailsModel.getIncome();
        this.employmentStatus = loanerDetailsModel.getEmploymentStatus();
        this.purpose = loanerDetailsModel.getPurpose();
        this.source = loanerDetailsModel.getSource();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Marriage getMarriage() {
        return marriage;
    }

    public void setMarriage(Marriage marriage) {
        this.marriage = marriage;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
