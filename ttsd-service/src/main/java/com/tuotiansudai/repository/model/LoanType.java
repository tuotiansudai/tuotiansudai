package com.tuotiansudai.repository.model;

public enum LoanType {
    LOAN_TYPE_1("interest_begin_on_invest","day","先付利息后还本金，按天计息，即投即生息","1","month","rfcl","先付利息后还本金，按天计息，即投即生息"),
    LOAN_TYPE_3("interest_begin_on_loan","day","先付利息后还本金，按天计息，放款后生息","1","month","rfcl","先付利息后还本金，按天计息，放款后生息"),
    LOAN_TYPE_4("interest_begin_on_loan","day","到期还本付息，按天计息，放款后生息","1","day","rlio","到期还本付息，按天计息，放款后生息"),
    LOAN_TYPE_5("interest_begin_on_invest","day","到期还本付息，按天计息，即投即生息","1","day","rlio","到期还本付息，按天计息，即投即生息");

    private String interestPoint;
    private String interestType;
    private String name;
    private String repayTimePeriod;
    private String repayTimeUnit;
    private String repayType;
    private String description;

    LoanType(String interestPoint,
             String interestType,String name,
             String repayTimePeriod,String repayTimeUnit,
             String repayType,String description){
        this.interestPoint = interestPoint;
        this.interestType = interestType;
        this.name = name;
        this.repayTimePeriod = repayTimePeriod;
        this.repayTimeUnit = repayTimeUnit;
        this.repayType = repayType;
        this.description = description;
    }
    public String getInterestPoint() {
        return interestPoint;
    }

    public void setInterestPoint(String interestPoint) {
        this.interestPoint = interestPoint;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepayTimePeriod() {
        return repayTimePeriod;
    }

    public void setRepayTimePeriod(String repayTimePeriod) {
        this.repayTimePeriod = repayTimePeriod;
    }

    public String getRepayTimeUnit() {
        return repayTimeUnit;
    }

    public void setRepayTimeUnit(String repayTimeUnit) {
        this.repayTimeUnit = repayTimeUnit;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
