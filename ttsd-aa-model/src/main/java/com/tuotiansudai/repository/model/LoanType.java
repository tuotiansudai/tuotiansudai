package com.tuotiansudai.repository.model;

import com.google.common.base.Splitter;

public enum LoanType {

    INVEST_INTEREST_MONTHLY_REPAY("先付收益后还本金，按天计息，即投即生息", InterestInitiateType.INTEREST_START_AT_INVEST, LoanPeriodUnit.MONTH),

    INVEST_INTEREST_LUMP_SUM_REPAY("到期还本付息，按天计息，即投即生息", InterestInitiateType.INTEREST_START_AT_INVEST, LoanPeriodUnit.DAY),

    //Deprecated
    LOAN_INTEREST_LUMP_SUM_REPAY("到期还本付息，按天计息，放款后生息", InterestInitiateType.INTEREST_START_AT_LOAN, LoanPeriodUnit.DAY),

    //Deprecated
    LOAN_INTEREST_MONTHLY_REPAY("先付收益后还本金，按天计息，放款后生息", InterestInitiateType.INTEREST_START_AT_LOAN, LoanPeriodUnit.MONTH);

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

    public String getInterestPointName() {
        return this.name.substring(name.indexOf("，") + 1);
    }

    public String getRepayType() {
        return this.name.substring(0, name.indexOf("，"));
    }

    public String getInterestType() {return Splitter.on('，').splitToList(this.name).get(2);}

    public InterestInitiateType getInterestInitiateType() {
        return interestInitiateType;
    }

    public LoanPeriodUnit getLoanPeriodUnit() {
        return loanPeriodUnit;
    }
}
