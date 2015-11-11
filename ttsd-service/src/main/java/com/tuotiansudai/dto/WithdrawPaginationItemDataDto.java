package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.utils.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class WithdrawPaginationItemDataDto extends WithdrawDto implements Serializable {

    private long withdrawId;

    private String fee;

    private String verifyMessage;

    private Date verifyTime;

    private String recheckMessage;

    private Date recheckTime;

    private Date createdTime;

    private String status;

    private String userName;

    private int adminRole; // 1: true; 0:false


    public WithdrawPaginationItemDataDto(WithdrawModel withdrawModel) {
        this.withdrawId = withdrawModel.getId();
        this.fee = AmountConverter.convertCentToString(withdrawModel.getFee());
        this.verifyMessage = withdrawModel.getVerifyMessage();
        this.verifyTime = withdrawModel.getVerifyTime();
        this.recheckMessage = withdrawModel.getRecheckMessage();
        this.recheckTime = withdrawModel.getRecheckTime();
        this.createdTime = withdrawModel.getCreatedTime();
        this.status = withdrawModel.getStatus().getDescription();
        this.userName = withdrawModel.getUserName();
        this.adminRole = withdrawModel.getIsAdmin();
        super.setLoginName(withdrawModel.getLoginName());
        super.setAmount(AmountConverter.convertCentToString(withdrawModel.getAmount()));
        super.setSource(withdrawModel.getSource());
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(long withdrawId) {
        this.withdrawId = withdrawId;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(int adminRole) {
        this.adminRole = adminRole;
    }
}
