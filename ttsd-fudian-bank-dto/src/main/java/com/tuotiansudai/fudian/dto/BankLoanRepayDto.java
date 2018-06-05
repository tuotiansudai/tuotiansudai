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

    private boolean normalRepay;

    private List<BankLoanRepayInvestDto> bankLoanRepayInvests;

    public BankLoanRepayDto() {
    }

    public BankLoanRepayDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long loanId, long loanRepayId, String loanTxNo, boolean normalRepay, List<BankLoanRepayInvestDto> bankLoanRepayInvests) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.capital = bankLoanRepayInvests.stream().mapToLong(BankLoanRepayInvestDto::getCapital).sum();
        this.interest = bankLoanRepayInvests.stream().mapToLong(bankLoanRepayInvest-> bankLoanRepayInvest.getInterest() + bankLoanRepayInvest.getDefaultInterest()).sum();
        this.loanTxNo = loanTxNo;
        this.normalRepay = normalRepay;
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

    public boolean isNormalRepay() {
        return normalRepay;
    }

    public List<BankLoanRepayInvestDto> getBankLoanRepayInvests() {
        return bankLoanRepayInvests;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && loanRepayId > 0
                && capital >= 0
                && interest >= 0
                && capital + interest > 0
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
