package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class WithdrawDetailResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "提现记录ID", example = "1")
    private String withdrawId;

    @ApiModelProperty(value = "提现时间", example = "2016-11-25 16:50;01")
    private String time;

    @ApiModelProperty(value = "审核时间", example = "2016-11-25 16:50;02")
    private String recheckTime;

    @ApiModelProperty(value = "提醒金额", example = "1000")
    private String money;

    @ApiModelProperty(value = "提现状态代码", example = "SUCCESS")
    private String status;

    @ApiModelProperty(value = "提现状态描述", example = "提现成功")
    private String statusDesc;

    public WithdrawDetailResponseDataDto() {
    }

    public WithdrawDetailResponseDataDto(WithdrawModel withdrawModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.withdrawId = "" + withdrawModel.getId();
        this.time = sdf.format(withdrawModel.getCreatedTime());
        if (withdrawModel.getNotifyTime() != null) {
            this.recheckTime = sdf.format(withdrawModel.getNotifyTime());
        } else {
            this.recheckTime = "";
        }
        this.money = AmountConverter.convertCentToString(withdrawModel.getAmount());
        this.status = convertToMobileAppWithDrawStatus(withdrawModel.getStatus());
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

    private String convertToMobileAppWithDrawStatus(WithdrawStatus withdrawStatus){
        if(WithdrawStatus.WAIT_PAY.equals(withdrawStatus)){
            return "wait_verify";
        }else if(WithdrawStatus.APPLY_SUCCESS.equals(withdrawStatus)){
            return "recheck";
        }else if(WithdrawStatus.APPLY_FAIL.equals(withdrawStatus)){
            return "verify_fail";
        }else if(WithdrawStatus.FAIL.equals(withdrawStatus)){
            return "recheck_fail";
        }
        return "success";

    }



}
