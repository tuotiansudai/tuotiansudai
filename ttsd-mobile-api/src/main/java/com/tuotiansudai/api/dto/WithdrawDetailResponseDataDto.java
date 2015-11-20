package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

public class WithdrawDetailResponseDataDto extends BaseResponseDataDto {
    private String withdrawId;
    private String time;
    private String recheckTime;
    private String money;
    private String status;
    private String statusDesc;

    public WithdrawDetailResponseDataDto() {
    }

    public WithdrawDetailResponseDataDto(WithdrawModel withdrawModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.withdrawId = "" + withdrawModel.getId();
        this.time = sdf.format(withdrawModel.getCreatedTime());
        if (withdrawModel.getNotifyTime() != null) {
            this.recheckTime = sdf.format(withdrawModel.getNotifyTime());
        } else {
            this.recheckTime = "";
        }
        this.money = AmountConverter.convertCentToString(withdrawModel.getAmount());
        this.status = withdrawModel.getStatus().name().toLowerCase();
        this.statusDesc = withdrawModel.getStatus().getDescription();

    }

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(String recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}
