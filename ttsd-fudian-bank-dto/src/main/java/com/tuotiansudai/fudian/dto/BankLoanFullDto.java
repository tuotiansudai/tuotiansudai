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

    private long triggerTime;

    public BankLoanFullDto() {
    }

    public BankLoanFullDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long loanId, String loanTxNo, String loanOrderNo, String loanOrderDate, String expectRepayTime, String checkerLoginName, long triggerTime) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.loanId = loanId;
        this.loanTxNo = loanTxNo;
        this.loanOrderNo = loanOrderNo;
        this.loanOrderDate = loanOrderDate;
        this.expectRepayTime = expectRepayTime;
        this.checkerLoginName = checkerLoginName;
        this.triggerTime = triggerTime;
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

    public long getTriggerTime() {
        return triggerTime;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && triggerTime >= 0
                && !Strings.isNullOrEmpty(loanTxNo)
                && !Strings.isNullOrEmpty(loanOrderNo)
                && !Strings.isNullOrEmpty(loanOrderDate)
                && !Strings.isNullOrEmpty(expectRepayTime);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
