package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.utils.AmountConverter;
import org.apache.commons.lang3.NotImplementedException;

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

    public WithdrawDetailResponseDataDto(WithdrawModel withdrawCash) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.withdrawId = "" + withdrawCash.getId();
        this.time = sdf.format(withdrawCash.getCreatedTime());
        if(withdrawCash.getRecheckTime()!=null) {
            this.recheckTime = sdf.format(withdrawCash.getRecheckTime());
        }else{
            this.recheckTime = "";
        }
        this.money = AmountConverter.convertCentToString(withdrawCash.getAmount());
        this.status = withdrawCash.getStatus().name().toLowerCase();
        this.statusDesc = withdrawCash.getStatus().getDescription();

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
