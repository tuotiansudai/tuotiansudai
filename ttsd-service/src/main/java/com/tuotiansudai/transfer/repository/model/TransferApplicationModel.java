package com.tuotiansudai.transfer.repository.model;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.TransferStatus;

import java.io.Serializable;
import java.util.Date;

public class TransferApplicationModel implements Serializable {

    private long id;

    private String name;

    private long loanId;

    private long transferInvestId;

    private Long investId;

    private int period;

    private String loginName;

    private long investAmount;

    private long transferAmount;

    private long transferFee;

    private TransferStatus status;

    private Date deadline;

    private Date transferTime;

    private Date applicationTime;

    public TransferApplicationModel() {
    }

    public TransferApplicationModel(InvestModel transferInvestModel, String name, int period, long transferAmount, long transferFee, Date deadline) {
        this.loanId = transferInvestModel.getLoanId();
        this.name = name;
        this.transferInvestId = transferInvestModel.getId();
        this.period = period;
        this.loginName = transferInvestModel.getLoginName();
        this.investAmount = transferInvestModel.getAmount();
        this.transferAmount = transferAmount;
        this.transferFee = transferFee;
        this.status = TransferStatus.TRANSFERRING;
        this.deadline = deadline;
        this.applicationTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(long transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(long transferAmount) {
        this.transferAmount = transferAmount;
    }

    public long getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(long transferFee) {
        this.transferFee = transferFee;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

}
