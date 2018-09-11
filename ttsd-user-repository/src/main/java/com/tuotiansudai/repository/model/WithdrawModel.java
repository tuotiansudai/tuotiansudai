package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.WithdrawStatus;

import java.util.Date;

/**
 * Created by qduljs2011 on 2018/7/11.
 */
public class WithdrawModel {
    private long id;

    private long bankCardId;

    private String loginName;

    private long amount;

    private long fee;

    private String applyNotifyMessage;

    private Date applyNotifyTime;

    private String notifyMessage;

    private Date notifyTime;

    private Date createdTime;

    private WithdrawStatus status;

    private Source source;

    public WithdrawModel() {
    }

    public WithdrawModel(long id, long bankCardId, String loginName, long amount, long fee) {
        this.id = id;
        this.bankCardId = bankCardId;
        this.loginName = loginName;
        this.amount = amount;
        this.fee = fee;
        this.status = WithdrawStatus.WAIT_PAY;
        this.source = Source.WEB;
    }

    public long getId() {
        return id;
    }

    public long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getApplyNotifyMessage() {
        return applyNotifyMessage;
    }

    public void setApplyNotifyMessage(String applyNotifyMessage) {
        this.applyNotifyMessage = applyNotifyMessage;
    }

    public Date getApplyNotifyTime() {
        return applyNotifyTime;
    }

    public void setApplyNotifyTime(Date applyNotifyTime) {
        this.applyNotifyTime = applyNotifyTime;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public WithdrawStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatus status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
