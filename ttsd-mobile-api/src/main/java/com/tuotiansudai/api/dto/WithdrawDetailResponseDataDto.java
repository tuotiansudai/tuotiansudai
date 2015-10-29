package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.WithdrawModel;
import org.apache.commons.lang3.NotImplementedException;

public class WithdrawDetailResponseDataDto extends BaseResponseDataDto {
    private String withdrawId;
    private String time;
    private String recheckTime;
    private String money;
    private String status;
    private String statusDesc;

    public WithdrawDetailResponseDataDto() {
    }

    public WithdrawDetailResponseDataDto(WithdrawModel withdrawCash) {
        throw new NotImplementedException(getClass().getName());
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
