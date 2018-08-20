package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankLoanFullDto;
import com.tuotiansudai.fudian.util.AmountUtils;

public class LoanFullRequestDto extends BaseRequestDto {

    private String loanFee = "0.00";

    private String loanTxNo;

    private String loanOrderNo;

    private String loanOrderDate;

    private String expectRepayTime;

    private String notifyUrl; //异步回调地址

    public LoanFullRequestDto(BankLoanFullDto bankLoanFullDto) {
        super(Source.WEB, bankLoanFullDto.getLoginName(), bankLoanFullDto.getMobile(), bankLoanFullDto.getBankUserName(), bankLoanFullDto.getBankAccountNo());
        this.loanTxNo = bankLoanFullDto.getLoanTxNo();
        this.loanOrderNo = bankLoanFullDto.getLoanOrderNo();
        this.loanOrderDate = bankLoanFullDto.getLoanOrderDate();
        this.expectRepayTime = bankLoanFullDto.getExpectRepayTime();
        this.loanFee= AmountUtils.toYuan(bankLoanFullDto.getLoanFee());
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}