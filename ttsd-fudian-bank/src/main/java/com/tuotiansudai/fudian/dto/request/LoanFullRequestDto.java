package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class LoanFullRequestDto extends PayBaseRequestDto {

    private String loanFee = "0.00";

    private String loanTxNo;

    private String loanOrderNo;

    private String loanOrderDate;

    private String expectRepayTime;

    public LoanFullRequestDto(String loginName, String mobile, String userName, String accountNo, String loanTxNo, String loanOrderNo, String loanOrderDate, String expectRepayTime, Map<String, String> extraValues) {
        super(Source.WEB, loginName, mobile, userName, accountNo, ApiType.LOAN_FULL, extraValues);
        this.loanTxNo = loanTxNo;
        this.loanOrderNo = loanOrderNo;
        this.loanOrderDate = loanOrderDate;
        this.expectRepayTime = expectRepayTime;
    }

    public String getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(String loanFee) {
        this.loanFee = loanFee;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getLoanOrderNo() {
        return loanOrderNo;
    }

    public void setLoanOrderNo(String loanOrderNo) {
        this.loanOrderNo = loanOrderNo;
    }

    public String getLoanOrderDate() {
        return loanOrderDate;
    }

    public void setLoanOrderDate(String loanOrderDate) {
        this.loanOrderDate = loanOrderDate;
    }

    public String getExpectRepayTime() {
        return expectRepayTime;
    }

    public void setExpectRepayTime(String expectRepayTime) {
        this.expectRepayTime = expectRepayTime;
    }
}