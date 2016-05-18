package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

public class InvestRepayRecordResponseDataDto {
    /**
     * 标的id
     */
    private String loanId;
    /**
     * 标的名称
     */
    private String loanName;
    /**
     * 投资id
     */
    private String investId;
    /**
     * 投资金额
     */
    private String investMoney;
    /**
     * 投资时间
     */
    private String investTime;
    /**
     * 还款日
     */
    private String repayDay;
    /**
     * 还款状态
     */
    private String status;
    /**
     * 还款状态描述
     */
    private String statusDesc;
    /**
     * 本金
     */
    private String corpus;
    /**
     * 利息
     */
    private String interest;

    private String loanType;

    public InvestRepayRecordResponseDataDto() {
    }

    public InvestRepayRecordResponseDataDto(InvestRepayModel investRepay, InvestModel invest, LoanModel loan) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        this.loanId = String.valueOf(loan.getId());
        this.loanName = loan.getName();
        this.investId = String.valueOf(invest.getId());
        this.investMoney = AmountConverter.convertCentToString(invest.getAmount());
        this.investTime = sdf.format(invest.getInvestTime());
        this.repayDay = sdfDay.format(investRepay.getRepayDate());
        this.status = investRepay.getStatus().name();
        this.statusDesc = investRepay.getStatus().getDescription();
        if (investRepay.getStatus() == RepayStatus.COMPLETE) {
            if(investRepay.getActualRepayDate() != null){

                this.repayDay = sdf.format(investRepay.getActualRepayDate());
            }
        }else{
            this.repayDay = sdfDay.format(investRepay.getRepayDate());
        }
        this.corpus = AmountConverter.convertCentToString(investRepay.getCorpus());
        if (RepayStatus.COMPLETE == investRepay.getStatus()) {
            this.interest = AmountConverter.convertCentToString(investRepay.getActualInterest() + investRepay.getDefaultInterest() - investRepay.getActualFee());
        } else {
            this.interest = AmountConverter.convertCentToString(investRepay.getExpectedInterest() + investRepay.getDefaultInterest() - investRepay.getExpectedFee());
        }
        this.loanType = loan.getProductType() != null ? loan.getProductType().name() : "";
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
