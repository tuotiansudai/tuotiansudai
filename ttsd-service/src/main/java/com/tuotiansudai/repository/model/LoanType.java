package com.tuotiansudai.repository.model;

public enum LoanType {
    LOAN_TYPE_1("先付利息后还本金，按天计息，即投即生息", InterestInitiateType.INTEREST_START_AT_INVEST, LoanPeriodUnit.MONTH),

    LOAN_TYPE_3("先付利息后还本金，按天计息，放款后生息", InterestInitiateType.INTEREST_START_AT_LOAN, LoanPeriodUnit.MONTH),

    LOAN_TYPE_4("到期还本付息，按天计息，放款后生息", InterestInitiateType.INTEREST_START_AT_LOAN, LoanPeriodUnit.DAY),

    LOAN_TYPE_5("到期还本付息，按天计息，即投即生息", InterestInitiateType.INTEREST_START_AT_INVEST, LoanPeriodUnit.DAY);

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

    public InterestInitiateType getInterestInitiateType() {
        return interestInitiateType;
    }

    public LoanPeriodUnit getLoanPeriodUnit() {
        return loanPeriodUnit;
    }
}
