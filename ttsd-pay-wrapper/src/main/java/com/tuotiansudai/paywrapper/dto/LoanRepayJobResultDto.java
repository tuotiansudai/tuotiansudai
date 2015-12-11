package com.tuotiansudai.paywrapper.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LoanRepayJobResultDto implements Serializable {

    @JsonProperty(value = "loanId")
    private long loanId;

    @JsonProperty(value = "loanRepayId")
    private long loanRepayId;

    @JsonProperty(value = "isOverdueRepay")
    private boolean isOverdueRepay;

    @JsonProperty(value = "repayAmount")
    private long repayAmount;

    @JsonProperty(value = "loanRepayBalance")
    private long loanRepayBalance;

    @JsonProperty(value = "actualRepayDate")
    private Date actualRepayDate;

    @JsonProperty(value = "isLastPeriod")
    private boolean isLastPeriod;

    @JsonProperty(value = "isUpdateAgentUserBillSuccess")
    private boolean isUpdateAgentUserBillSuccess;

    @JsonProperty(value = "loanRepayBalanceStatus")
    private SyncRequestStatus loanRepayBalanceStatus;

    @JsonProperty(value = "isUpdateSystemBillSuccess")
    private boolean isUpdateSystemBillSuccess;

    @JsonProperty(value = "isUpdateLoanRepayStatusSuccess")
    private boolean isUpdateLoanRepayStatusSuccess;

    @JsonProperty(value = "isUpdateLoanStatusSuccess")
    private boolean isUpdateLoanStatusSuccess;

    @JsonProperty(value = "investRepayJobResults")
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
    public boolean isSuccess() {
        Optional<InvestRepayJobResultDto> optional = Iterators.tryFind(this.investRepayJobResults.iterator(), new Predicate<InvestRepayJobResultDto>() {
            @Override
            public boolean apply(InvestRepayJobResultDto input) {
                return input.getInterestStatus() == SyncRequestStatus.FAIL || input.getFeeStatus() == SyncRequestStatus.FAIL;
            }
        });

        return !optional.isPresent() && !(loanRepayBalanceStatus == SyncRequestStatus.FAIL);
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public boolean isOverdueRepay() {
        return isOverdueRepay;
    }

    public long getRepayAmount() {
        return repayAmount;
    }

    public long getLoanRepayBalance() {
        return loanRepayBalance;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public boolean isLastPeriod() {
        return isLastPeriod;
    }

    public boolean isUpdateAgentUserBillSuccess() {
        return isUpdateAgentUserBillSuccess;
    }

    public SyncRequestStatus getLoanRepayBalanceStatus() {
        return loanRepayBalanceStatus;
    }

    public boolean isUpdateSystemBillSuccess() {
        return isUpdateSystemBillSuccess;
    }

    public boolean isUpdateLoanRepayStatusSuccess() {
        return isUpdateLoanRepayStatusSuccess;
    }

    public boolean isUpdateLoanStatusSuccess() {
        return isUpdateLoanStatusSuccess;
    }

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