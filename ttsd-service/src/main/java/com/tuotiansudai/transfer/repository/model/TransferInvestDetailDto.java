package com.tuotiansudai.transfer.repository.model;

import java.io.Serializable;
import java.util.Date;

public class TransferInvestDetailDto implements Serializable {

    private String loanName;

    private long transferAmount;

    private long investAmount;

    private Date transferTime;

    private Date nextRepayDate;

    private long nextRepayAmount;

    private long investId;

    private long transferInvestId;

    private String transferrerLoginName;

    private String transfereeLoginName;

    private long loanId;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(long transferAmount) {
        this.transferAmount = transferAmount;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public long getNextRepayAmount() {
        return nextRepayAmount;
    }

    public void setNextRepayAmount(long nextRepayAmount) {
        this.nextRepayAmount = nextRepayAmount;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public long getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(long transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public String getTransferrerLoginName() {
        return transferrerLoginName;
    }

    public void setTransferrerLoginName(String transferrerLoginName) {
        this.transferrerLoginName = transferrerLoginName;
    }

    public String getTransfereeLoginName() {
        return transfereeLoginName;
    }

    public void setTransfereeLoginName(String transfereeLoginName) {
        this.transfereeLoginName = transfereeLoginName;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }
}
