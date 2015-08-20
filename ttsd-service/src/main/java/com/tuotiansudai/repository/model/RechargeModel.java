package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.utils.AmountUtil;

import java.util.Date;

public class RechargeModel {

    private long id;

    private String loginName;

    private long amount;

    private long fee;

    private String bank;

    private RechargeStatus status;

    private Date createdTime = new Date();

    public RechargeModel() {

    }

    public RechargeModel(RechargeDto dto) {
        this.amount = AmountUtil.convertStringToCent(dto.getAmount());
        this.bank = dto.getBank();
        this.loginName = dto.getLoginName();
        this.status = RechargeStatus.WAIT_PAY;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public RechargeStatus getStatus() {
        return status;
    }

    public void setStatus(RechargeStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
