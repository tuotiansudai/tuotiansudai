package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;

import java.util.Date;
import java.util.List;

public class TransferApplicationDetailDto extends BaseDataDto {

    private long id;

    private String name;

    private long loan_id;

    private String loanName;

    private long investId;

    private long transferInvestId;

    private String transferAmount;

    private String investAmount;

    private double baseRate;

    private int leftPeriod;

    private Date dueDate;

    private Date nextRefundDate;

    private String nextExpecedInterest;

    private String loanType;

    private Date deadLine;

    private String expecedInterest;

    private String balance;

    private TransferStatus transferStatus;

    private Date transferTime;

    public TransferApplicationDetailDto() {
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

    public long getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(long loan_id) {
        this.loan_id = loan_id;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
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

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public int getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(int leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getNextRefundDate() {
        return nextRefundDate;
    }

    public void setNextRefundDate(Date nextRefundDate) {
        this.nextRefundDate = nextRefundDate;
    }

    public String getNextExpecedInterest() {
        return nextExpecedInterest;
    }

    public void setNextExpecedInterest(String nextExpecedInterest) {
        this.nextExpecedInterest = nextExpecedInterest;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public String getExpecedInterest() {
        return expecedInterest;
    }

    public void setExpecedInterest(String expecedInterest) {
        this.expecedInterest = expecedInterest;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }
}
