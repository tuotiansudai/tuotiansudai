package com.tuotiansudai.dto;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InvestorInvestDetailDto implements Serializable {

    private long investId;

    private long loanId;

    private String loanName;

    private long amount;

    private long expectInterest;

    private long repayAmount;

    private long unRepayAmount;

    private Date interestBeginTime;

    private Date deadline;

    private double annualizedRate;
    /**
     * 借款天数
     */
    private int duration;

    private Date investTime;

    private ProductType productType;

    private String contractNo;

    private String contractUrl;

//    coupons;
//    membership;

    private List<InvestorInvestRepayDto> repayList;

    public InvestorInvestDetailDto(
            InvestModel investModel, LoanModel loanModel, List<InvestRepayModel> investRepayModelList,
            long expectInterest) {
        this.investId = investModel.getId();
        this.loanId = loanModel.getId();
        this.loanName = loanModel.getName();
        this.amount = investModel.getAmount();
        this.expectInterest = expectInterest;

        if (Arrays.asList(LoanType.INVEST_INTEREST_MONTHLY_REPAY, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY).contains(loanModel.getType())) {
            this.interestBeginTime = investModel.getInvestTime();
        } else {
            this.interestBeginTime = loanModel.getRecheckTime();
        }
        this.deadline = loanModel.getDeadline();
        this.annualizedRate = loanModel.getBaseRate() + loanModel.getActivityRate();
        this.duration = loanModel.getDuration();
        this.investTime = investModel.getInvestTime();

        this.repayAmount = investRepayModelList.stream()
                .filter(r -> r.getStatus().equals(RepayStatus.COMPLETE))
                .map(InvestRepayModel::getRepayAmount)
                .reduce((a, b) -> a + b)
                .orElse(0L);
        this.unRepayAmount = investModel.getAmount() + this.expectInterest - this.repayAmount;
        this.repayList = investRepayModelList.stream().map(InvestorInvestRepayDto::new).collect(Collectors.toList());
        this.productType = loanModel.getProductType();
        this.contractNo = investModel.getContractNo();
        if (!Strings.isNullOrEmpty(contractNo) && !ProductType.EXPERIENCE.equals(loanModel.getProductType())) {
            if ("OLD".equalsIgnoreCase(investModel.getContractNo())) {
                this.contractUrl = String.format("/contract/investor/loanId/%d/investId/%d", loanId, investId);
            } else {
                this.contractUrl = String.format("/contract/invest/contractNo/%s", contractNo);
            }
        }
    }

    public long getInvestId() {
        return investId;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public long getAmount() {
        return amount;
    }

    public long getExpectInterest() {
        return expectInterest;
    }

    public long getRepayAmount() {
        return repayAmount;
    }

    public long getUnRepayAmount() {
        return unRepayAmount;
    }

    public Date getInterestBeginTime() {
        return interestBeginTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public double getAnnualizedRate() {
        return annualizedRate;
    }

    public int getDuration() {
        return duration;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public List<InvestorInvestRepayDto> getRepayList() {
        return repayList;
    }

    public ProductType getProductType() {
        return productType;
    }

    public String getContractNo() {
        return contractNo;
    }

    public String getContractUrl() {
        return contractUrl;
    }
}
