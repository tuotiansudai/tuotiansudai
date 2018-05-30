package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankLoanRepayInvestDto extends BankBaseDto {

    private long investId;

    private long investRepayId;

    private String loanTxNo;

    private long capital;

    private long interest;

    private long interestFee;

    private String investOrderNo;

    private String investOrderDate;

    public BankLoanRepayInvestDto() {
    }

    public BankLoanRepayInvestDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long investId, long investRepayId, String loanTxNo, long capital, long interest, long interestFee, String investOrderNo, String investOrderDate) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.investId = investId;
        this.investRepayId = investRepayId;
        this.loanTxNo = loanTxNo;
        this.capital = capital;
        this.interest = interest;
        this.interestFee = interestFee;
        this.investOrderNo = investOrderNo;
        this.investOrderDate = investOrderDate;
    }

    public long getInvestId() {
        return investId;
    }

    public long getInvestRepayId() {
        return investRepayId;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public long getCapital() {
        return capital;
    }

    public long getInterest() {
        return interest;
    }

    public long getInterestFee() {
        return interestFee;
    }

    public String getInvestOrderNo() {
        return investOrderNo;
    }

    public String getInvestOrderDate() {
        return investOrderDate;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && investId > 0
                && investRepayId > 0
                && capital > 0
                && interest > 0
                && interestFee > 0
                && !Strings.isNullOrEmpty(investOrderNo)
                && !Strings.isNullOrEmpty(investOrderDate);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
