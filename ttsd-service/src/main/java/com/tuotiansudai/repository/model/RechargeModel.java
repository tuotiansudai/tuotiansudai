package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.utils.AmountConverter;

import java.util.Date;

public class RechargeModel {

    private long id;

    private String loginName;

    private long amount;

    private long fee;

    private String bankCode;

    private RechargeStatus status;

    private Source source;

    private boolean fastPay;

    private Date createdTime;

    public RechargeModel() {

    }

    public RechargeModel(RechargeDto dto) {
        this.amount = AmountConverter.convertStringToCent(dto.getAmount());
        this.bankCode = dto.getBankCode();
        this.loginName = dto.getLoginName();
        this.status = RechargeStatus.WAIT_PAY;
        this.source = dto.getSource();
        this.fastPay = dto.isFastPay();
        this.createdTime = new Date();
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

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isFastPay() {
        return fastPay;
    }

    public void setFastPay(boolean fastPay) {
        this.fastPay = fastPay;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
