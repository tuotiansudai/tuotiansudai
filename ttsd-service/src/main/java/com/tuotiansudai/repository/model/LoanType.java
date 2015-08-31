package com.tuotiansudai.repository.model;

/**
 * Created by tuotian on 15/8/18.
 */
public enum LoanType {
    /***默认值***/
    LOAN_TYPE_1("loan_type_1","interest_begin_on_invest","day","先付利息后还本金，按天计息，即投即生息","1","month","rfcl","先付利息后还本金，按天计息，即投即生息"),
    LOAN_TYPE_2("loan_type_2","interest_begin_on_loan","month","等额本息，按月计息，按月还款，放款后生息","1","month","cpm","等额本息，按月计息，按月还款，放款后生息"),
    LOAN_TYPE_3("loan_type_3","interest_begin_on_invest","day","付利息后还本金，按天计息，放款后生息","1","month","rfcl","付利息后还本金，按天计息，放款后生息"),
    LOAN_TYPE_4("loan_type_4","interest_begin_on_invest","day","到期还本付息，按天计息，放款后生息","1","day","rlio","到期还本付息，按天计息，放款后生息"),
    LOAN_TYPE_5("loan_type_5","interest_begin_on_invest","day","到期还本付息，按天计息，即投即生息","1","day","rlio","到期还本付息，按天计息，即投即生息");

    private String id;
    private String interestPoint;
    private String interestType;
    private String name;
    private String repayTimePeriod;
    private String repayTimeUnit;
    private String repayType;
    private String description;

    LoanType(String id,String interestPoint,
             String interestType,String name,
             String repayTimePeriod,String repayTimeUnit,
             String repayType,String description){
        this.id = id;
        this.interestPoint = interestPoint;
        this.interestType = interestType;
        this.name = name;
        this.repayTimePeriod = repayTimePeriod;
        this.repayTimeUnit = repayTimeUnit;
        this.repayType = repayType;
        this.description = description;
    }

    public static LoanType getLoantype(String id){
        for(LoanType loanType:LoanType.values()){
            if (loanType.id.equalsIgnoreCase(id)){
                return loanType;
            }
        }
        return null;

    }

    public String getId() {
        return id;
    }

    public String getInterestPoint() {
        return interestPoint;
    }

    public String getInterestType() {
        return interestType;
    }

    public String getName() {
        return name;
    }

    public String getRepayTimePeriod() {
        return repayTimePeriod;
    }

    public String getRepayTimeUnit() {
        return repayTimeUnit;
    }

    public String getRepayType() {
        return repayType;
    }

    public String getDescription() {
        return description;
    }
}
