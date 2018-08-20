package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankLoanFullDto extends BankBaseDto {

    private long loanId;

    private String loanTxNo;

    private String loanOrderNo;

    private String loanOrderDate;

    private String expectRepayTime;

    private String checkerLoginName;


    private String fullTime;

    private  long  loanFee;

    public BankLoanFullDto() {
    }

    public BankLoanFullDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long loanId, String loanTxNo, String loanOrderNo, String loanOrderDate, String expectRepayTime, String checkerLoginName, String fullTime,long loanFee) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.loanId = loanId;
        this.loanTxNo = loanTxNo;
        this.loanOrderNo = loanOrderNo;
        this.loanOrderDate = loanOrderDate;
        this.expectRepayTime = expectRepayTime;
        this.checkerLoginName = checkerLoginName;
        this.fullTime = fullTime;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public String getLoanOrderNo() {
        return loanOrderNo;
    }

    public String getLoanOrderDate() {
        return loanOrderDate;
    }

    public String getExpectRepayTime() {
        return expectRepayTime;
    }

    public String getCheckerLoginName() {
        return checkerLoginName;
    }

    public String getFullTime() {
        return fullTime;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && !Strings.isNullOrEmpty(fullTime)
                && !Strings.isNullOrEmpty(loanTxNo)
                && !Strings.isNullOrEmpty(loanOrderNo)
                && !Strings.isNullOrEmpty(loanOrderDate)
                && !Strings.isNullOrEmpty(expectRepayTime);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }


    public long getLoanFee() {
        return loanFee;
    }
}
