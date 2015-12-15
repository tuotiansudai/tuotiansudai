package com.tuotiansudai.paywrapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanRepayJobResultDto implements Serializable {

    private long loanId;

    private long loanRepayId;

    private boolean isOverdueRepay;

    private long repayAmount;

    private long loanRepayBalance;

    private Date actualRepayDate;

    private boolean isLastPeriod;

    private boolean isUpdateAgentUserBillSuccess;

    private SyncRequestStatus loanRepayBalanceStatus;

    private boolean isUpdateSystemBillSuccess;

    private boolean isUpdateLoanRepayStatusSuccess;

    private boolean isUpdateLoanStatusSuccess;

    private List<InvestRepayJobResultDto> investRepayJobResults;

    public LoanRepayJobResultDto() {
    }

    public LoanRepayJobResultDto(long loanId, long loanRepayId, boolean isOverdueRepay, long repayAmount, Date actualRepayDate, boolean isLastPeriod, List<InvestRepayJobResultDto> investRepayJobResults) {
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.isOverdueRepay = isOverdueRepay;
        this.repayAmount = repayAmount;
        this.loanRepayBalance = repayAmount;
        this.actualRepayDate = actualRepayDate;
        this.isLastPeriod = isLastPeriod;
        this.investRepayJobResults = investRepayJobResults;
        for (InvestRepayJobResultDto investRepayJobResult : investRepayJobResults) {
            this.loanRepayBalance -= investRepayJobResult.getCorpus() + investRepayJobResult.getActualInterest() + investRepayJobResult.getDefaultInterest();
        }
        this.loanRepayBalanceStatus = this.loanRepayBalance == 0 ? SyncRequestStatus.SUCCESS : SyncRequestStatus.READY;
        this.isUpdateSystemBillSuccess = this.loanRepayBalance == 0;
    }

    @JsonIgnore
    public boolean jobRetry() {
        final List<SyncRequestStatus> retryStatus = Lists.newArrayList(SyncRequestStatus.FAILURE, SyncRequestStatus.READY);

        Optional<InvestRepayJobResultDto> optional = Iterators.tryFind(this.investRepayJobResults.iterator(), new Predicate<InvestRepayJobResultDto>() {
            @Override
            public boolean apply(InvestRepayJobResultDto input) {
                return retryStatus.contains(input.getInterestStatus()) || retryStatus.contains(input.getFeeStatus());
            }
        });

        return optional.isPresent() || retryStatus.contains(loanRepayBalanceStatus);
    }

    @JsonProperty(value = "loanId")
    public long getLoanId() {
        return loanId;
    }

    @JsonProperty(value = "loanRepayId")
    public long getLoanRepayId() {
        return loanRepayId;
    }

    @JsonProperty(value = "isOverdueRepay")
    public boolean isOverdueRepay() {
        return isOverdueRepay;
    }

    @JsonProperty(value = "repayAmount")
    public long getRepayAmount() {
        return repayAmount;
    }

    @JsonProperty(value = "loanRepayBalance")
    public long getLoanRepayBalance() {
        return loanRepayBalance;
    }

    @JsonProperty(value = "actualRepayDate")
    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    @JsonProperty(value = "isLastPeriod")
    public boolean isLastPeriod() {
        return isLastPeriod;
    }

    @JsonProperty(value = "isUpdateAgentUserBillSuccess")
    public boolean isUpdateAgentUserBillSuccess() {
        return isUpdateAgentUserBillSuccess;
    }

    @JsonProperty(value = "loanRepayBalanceStatus")
    public SyncRequestStatus getLoanRepayBalanceStatus() {
        return loanRepayBalanceStatus;
    }

    @JsonProperty(value = "isUpdateSystemBillSuccess")
    public boolean isUpdateSystemBillSuccess() {
        return isUpdateSystemBillSuccess;
    }

    @JsonProperty(value = "isUpdateLoanRepayStatusSuccess")
    public boolean isUpdateLoanRepayStatusSuccess() {
        return isUpdateLoanRepayStatusSuccess;
    }

    @JsonProperty(value = "isUpdateLoanStatusSuccess")
    public boolean isUpdateLoanStatusSuccess() {
        return isUpdateLoanStatusSuccess;
    }

    @JsonProperty(value = "investRepayJobResults")
    public List<InvestRepayJobResultDto> getInvestRepayJobResults() {
        return investRepayJobResults;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public void setLoanRepayId(long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public void setOverdueRepay(boolean overdueRepay) {
        isOverdueRepay = overdueRepay;
    }

    public void setRepayAmount(long repayAmount) {
        this.repayAmount = repayAmount;
    }

    public void setLoanRepayBalance(long loanRepayBalance) {
        this.loanRepayBalance = loanRepayBalance;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public void setLastPeriod(boolean lastPeriod) {
        isLastPeriod = lastPeriod;
    }

    public void setUpdateAgentUserBillSuccess(boolean updateAgentUserBillSuccess) {
        isUpdateAgentUserBillSuccess = updateAgentUserBillSuccess;
    }

    public void setLoanRepayBalanceStatus(SyncRequestStatus loanRepayBalanceStatus) {
        this.loanRepayBalanceStatus = loanRepayBalanceStatus;
    }

    public void setUpdateSystemBillSuccess(boolean updateSystemBillSuccess) {
        isUpdateSystemBillSuccess = updateSystemBillSuccess;
    }

    public void setUpdateLoanRepayStatusSuccess(boolean updateLoanRepayStatusSuccess) {
        isUpdateLoanRepayStatusSuccess = updateLoanRepayStatusSuccess;
    }

    public void setUpdateLoanStatusSuccess(boolean updateLoanStatusSuccess) {
        isUpdateLoanStatusSuccess = updateLoanStatusSuccess;
    }

    public void setInvestRepayJobResults(List<InvestRepayJobResultDto> investRepayJobResults) {
        this.investRepayJobResults = investRepayJobResults;
    }
}