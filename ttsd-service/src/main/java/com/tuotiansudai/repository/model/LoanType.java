package com.tuotiansudai.repository.model;

public enum LoanType {

    // LOAN_TYPE_1
    INVEST_INTEREST_MONTHLY_REPAY("先付收益后还投资本金，按天计息，即投即生息", InterestInitiateType.INTEREST_START_AT_INVEST, LoanPeriodUnit.MONTH),

    // LOAN_TYPE_3
    LOAN_INTEREST_MONTHLY_REPAY("先付收益后还投资本金，按天计息，放款后生息", InterestInitiateType.INTEREST_START_AT_LOAN, LoanPeriodUnit.MONTH),

    // LOAN_TYPE_5
    INVEST_INTEREST_LUMP_SUM_REPAY("到期还本付息，按天计息，即投即生息", InterestInitiateType.INTEREST_START_AT_INVEST, LoanPeriodUnit.DAY),

    // LOAN_TYPE_4
    LOAN_INTEREST_LUMP_SUM_REPAY("到期还本付息，按天计息，放款后生息", InterestInitiateType.INTEREST_START_AT_LOAN, LoanPeriodUnit.DAY);

    private InterestInitiateType interestInitiateType;
    private String name;
    private LoanPeriodUnit loanPeriodUnit;

    LoanType(String name, InterestInitiateType interestInitiateType, LoanPeriodUnit loanPeriodUnit) {
        this.name = name;
        this.interestInitiateType = interestInitiateType;
        this.loanPeriodUnit = loanPeriodUnit;
    }

    public String getName() {
        return name;
    }

    public String getInterestPointName(){
        return this.name.substring(name.indexOf("，") + 1);
    }
    public String getRepayType(){
        return this.name.substring(0,name.indexOf("，"));
    }

    public InterestInitiateType getInterestInitiateType() {
        return interestInitiateType;
    }

    public LoanPeriodUnit getLoanPeriodUnit() {
        return loanPeriodUnit;
    }
}
