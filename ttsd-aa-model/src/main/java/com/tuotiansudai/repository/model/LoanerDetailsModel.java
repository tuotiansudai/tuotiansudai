package com.tuotiansudai.repository.model;


import com.tuotiansudai.dto.LoanCreateLoanerDetailsRequestDto;

import java.io.Serializable;

public class LoanerDetailsModel implements Serializable {
    private long id;
    private long loanId;
    private String loginName;
    private String userName;
    private Gender gender;
    private int age;
    private String identityNumber;
    private Marriage marriage;
    private String region;
    private String source;
    private String income;
    private String employmentStatus;
    private String purpose;

    public LoanerDetailsModel() {
    }

    public LoanerDetailsModel(long loanId, String loginName, String userName, Gender gender, int age, String identityNumber, Marriage marriage, String region, String source, String income, String employmentStatus, String purpose) {
        this.loanId = loanId;
        this.loginName = loginName;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.identityNumber = identityNumber;
        this.marriage = marriage;
        this.region = region;
        this.source = source;
        this.income = income;
        this.employmentStatus = employmentStatus;
        this.purpose = purpose;
    }

    public LoanerDetailsModel(long loanId, LoanCreateLoanerDetailsRequestDto loanerDetails) {
        this.loanId = loanId;
        this.loginName = "";
        this.userName = loanerDetails.getUserName();
        this.gender = loanerDetails.getGender();
        this.age = loanerDetails.getAge();
        this.identityNumber = loanerDetails.getIdentityNumber();
        this.marriage = loanerDetails.getMarriage();
        this.region = loanerDetails.getRegion();
        this.source = loanerDetails.getSource();
        this.income = loanerDetails.getIncome();
        this.employmentStatus = loanerDetails.getEmploymentStatus();
        this.purpose = loanerDetails.getPurpose();
    }

    public LoanerDetailsModel(LoanApplicationModel model) {
        this.loginName = model.getLoginName();
        this.userName = model.getUserName();
        this.gender = model.getSex().equals("MALE") ? Gender.MALE : Gender.FEMALE;
        this.age = model.getAge();
        this.identityNumber = model.getIdentityNumber();
        this.marriage = model.getMarriage();
        this.region = model.getAddress();
        this.source = Source.WEB.name();
        this.income = String.valueOf(model.getHomeIncome() * 10000);
        this.employmentStatus = model.getWorkPosition();
        this.purpose = model.getLoanUsage();
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
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
