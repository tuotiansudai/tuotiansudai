package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class LoanerDetailsModel implements Serializable{
    private long id;
    private long loanId;
    private String loginName;
    private String userName;
    private Gender gender;
    private int age;
    private String identifyNumber;
    private Marriage marriage;
    private String region;
    private String income;
    private String employmentStatus;

    public LoanerDetailsModel() {
    }

    public LoanerDetailsModel(long loanId, String loginName, String userName, Gender gender, int age, String identifyNumber, Marriage marriage, String region, String income, String employmentStatus) {
        this.loanId = loanId;
        this.loginName = loginName;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.identifyNumber = identifyNumber;
        this.marriage = marriage;
        this.region = region;
        this.income = income;
        this.employmentStatus = employmentStatus;
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

    public String getIdentifyNumber() {
        return identifyNumber;
    }

    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
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
}
