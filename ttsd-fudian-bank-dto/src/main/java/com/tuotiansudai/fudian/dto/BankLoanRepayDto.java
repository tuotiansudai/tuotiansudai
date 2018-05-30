package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import java.util.List;

public class BankLoanRepayDto extends BankBaseDto {

    private long loanId;

    private long loanRepayId;

    private long capital;

    private long interest;

    private String loanTxNo;

    private List<BankLoanRepayInvestDto> bankLoanRepayInvests;

    public BankLoanRepayDto() {
    }

    public BankLoanRepayDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long loanId, long loanRepayId, long capital, long interest, String loanTxNo, List<BankLoanRepayInvestDto> bankLoanRepayInvests) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.capital = capital;
        this.interest = interest;
        this.loanTxNo = loanTxNo;
        this.bankLoanRepayInvests = bankLoanRepayInvests;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getCapital() {
        return capital;
    }

    public long getInterest() {
        return interest;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public List<BankLoanRepayInvestDto> getBankLoanRepayInvests() {
        return bankLoanRepayInvests;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && loanRepayId > 0
                && capital > 0
                && interest > 0
                && !Strings.isNullOrEmpty(loanTxNo)
                && bankLoanRepayInvests != null
                && bankLoanRepayInvests.size() > 0
                && bankLoanRepayInvests.stream().allMatch(BankLoanRepayInvestDto::isValid)
                && capital + interest == bankLoanRepayInvests.stream().mapToLong(bankLoanRepayInvest -> bankLoanRepayInvest.getCapital() + bankLoanRepayInvest.getInterest()).sum();

    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
