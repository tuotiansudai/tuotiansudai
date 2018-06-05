package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankLoanRepayInvestDto;
import com.tuotiansudai.fudian.util.AmountUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanCallbackInvestItemRequestDto implements Serializable {

    private transient long id;

    private transient long loanCallbackId;

    private String capital;

    private String interest;

    private String interestFee;

    private String rateInterest;

    private String investUserName;

    private String investAccountNo;

    private String investOrderNo;

    private String investOrderDate;

    private String orderNo;

    private String orderDate;

    public LoanCallbackInvestItemRequestDto() {
    }

    public LoanCallbackInvestItemRequestDto(String orderNo, BankLoanRepayInvestDto bankLoanRepayInvestDto) {
        this.capital = AmountUtils.toYuan(bankLoanRepayInvestDto.getCapital());
        this.interest = AmountUtils.toYuan(bankLoanRepayInvestDto.getInterest());
        this.interestFee = AmountUtils.toYuan(bankLoanRepayInvestDto.getInterestFee());
        this.rateInterest = "0.00";
        this.investUserName = bankLoanRepayInvestDto.getBankUserName();
        this.investAccountNo = bankLoanRepayInvestDto.getBankAccountNo();
        this.investOrderNo = bankLoanRepayInvestDto.getInvestOrderNo();
        this.investOrderDate = bankLoanRepayInvestDto.getInvestOrderDate();
        this.orderNo = orderNo;
        this.orderDate = new SimpleDateFormat("yyyyMMdd").format(new Date()); //订单日期
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanCallbackId() {
        return loanCallbackId;
    }

    public void setLoanCallbackId(long loanCallbackId) {
        this.loanCallbackId = loanCallbackId;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestFee() {
        return interestFee;
    }

    public void setInterestFee(String interestFee) {
        this.interestFee = interestFee;
    }

    public String getRateInterest() {
        return rateInterest;
    }

    public void setRateInterest(String rateInterest) {
        this.rateInterest = rateInterest;
    }

    public String getInvestUserName() {
        return investUserName;
    }

    public void setInvestUserName(String investUserName) {
        this.investUserName = investUserName;
    }

    public String getInvestAccountNo() {
        return investAccountNo;
    }

    public void setInvestAccountNo(String investAccountNo) {
        this.investAccountNo = investAccountNo;
    }

    public String getInvestOrderDate() {
        return investOrderDate;
    }

    public void setInvestOrderDate(String investOrderDate) {
        this.investOrderDate = investOrderDate;
    }

    public String getInvestOrderNo() {
        return investOrderNo;
    }

    public void setInvestOrderNo(String investOrderNo) {
        this.investOrderNo = investOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}