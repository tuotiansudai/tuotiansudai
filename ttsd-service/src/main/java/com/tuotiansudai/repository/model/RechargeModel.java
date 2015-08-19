package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.UUIDGenerator;

import java.util.Date;

public class RechargeModel {

    private String id;

    private String loginName;

    private int amount;

    private int fee;

    private String bank;

    private RechargeStatus status;

    private Date createdTime = new Date();

    public RechargeModel() {

    }

    public RechargeModel(RechargeDto dto) {
        this.id = UUIDGenerator.generate();
        this.amount = AmountUtil.convertStringToCent(dto.getAmount());
        this.bank = dto.getBank();
        this.loginName = dto.getLoginName();
        this.status = RechargeStatus.WAIT_PAY;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
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
