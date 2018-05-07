package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.List;

public class LoanCallbackRequestDto extends BaseRequestDto {

    private String loanTxNo;

    private List<LoanCallbackInvestItemRequestDto> investList;

    private String returnUrl; //同步回调地址

    private String notifyUrl; //异步回调地址

    public LoanCallbackRequestDto(String loanTxNo, List<LoanCallbackInvestItemRequestDto> investList, String loginName, String mobile) {
        super(ApiType.LOAN_CALLBACK, loginName, mobile);
        this.loanTxNo = loanTxNo;
        this.investList = investList;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public List<LoanCallbackInvestItemRequestDto> getInvestList() {
        return investList;
    }

    public void setInvestList(List<LoanCallbackInvestItemRequestDto> investList) {
        this.investList = investList;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}