package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawPaginationView;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class WithdrawPaginationItemDataDto implements Serializable {

    private long withdrawId;

    private String loginName;

    private String amount;

    private Source source;

    private String fee;

    private String applyNotifyMessage;

    private Date applyNotifyTime;

    private String notifyMessage;

    private Date notifyTime;

    private Date createdTime;

    private String status;

    private String bankCard;

    private String userName;

    private String mobile;

    private String isStaff;


    public WithdrawPaginationItemDataDto(WithdrawModel withdrawModel) {
        this.withdrawId = withdrawModel.getId();
        this.fee = AmountConverter.convertCentToString(withdrawModel.getFee());
        this.applyNotifyMessage = withdrawModel.getApplyNotifyMessage();
        this.applyNotifyTime = withdrawModel.getApplyNotifyTime();
        this.notifyMessage = withdrawModel.getNotifyMessage();
        this.notifyTime = withdrawModel.getNotifyTime();
        this.createdTime = withdrawModel.getCreatedTime();
        this.status = withdrawModel.getStatus().getDescription();
        this.loginName = withdrawModel.getLoginName();
        this.amount = AmountConverter.convertCentToString(withdrawModel.getAmount());
        this.source = withdrawModel.getSource();
        this.bankCard = withdrawModel.getBankCard().getCardNumber();

        if (withdrawModel instanceof WithdrawPaginationView) {
            WithdrawPaginationView withdrawView = (WithdrawPaginationView) withdrawModel;
            this.userName = withdrawView.getUserName();
            this.mobile = withdrawView.getMobile();
            this.isStaff = withdrawView.getIsStaff();
        }
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getAmount() {
        return amount;
    }

    public Source getSource() {
        return source;
    }

    public String getFee() {
        return fee;
    }

    public String getApplyNotifyMessage() {
        return applyNotifyMessage;
    }

    public Date getApplyNotifyTime() {
        return applyNotifyTime;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getStatus() {
        return status;
    }

    public String getBankCard() {
        return bankCard;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isStaff() {
        return "1".equals(isStaff);
    }
}
