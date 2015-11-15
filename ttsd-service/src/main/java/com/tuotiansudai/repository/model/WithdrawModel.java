package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class WithdrawModel implements Serializable {
    private long id;

    private String loginName;

    private long amount;

    private long fee;

    private String verifyMessage;

    private Date verifyTime;

    private String recheckMessage;

    private Date recheckTime;

    private Date createdTime;

    private WithdrawStatus status;

    private Source source;

    private String userName;

    private int isAdmin;

    public WithdrawModel() {
    }

    public WithdrawModel(WithdrawDto dto) {
        this.amount = AmountConverter.convertStringToCent(dto.getAmount());
        this.loginName = dto.getLoginName();
        this.createdTime = new Date();
        this.status = WithdrawStatus.WAIT_VERIFY;
        this.source = dto.getSource();
    }

    public long getId() {
        return id;
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

    public String getVerifyMessage() {
        return verifyMessage;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getRecheckMessage() {
        return recheckMessage;
    }

    public void setRecheckMessage(String recheckMessage) {
        this.recheckMessage = recheckMessage;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WithdrawModel that = (WithdrawModel) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
