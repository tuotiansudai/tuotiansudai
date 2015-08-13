package com.ttsd.api.dto;

import com.esoft.jdp2p.loan.model.Recharge;

import java.text.SimpleDateFormat;

public class RechargeDetailResponseDataDto extends BaseResponseDataDto {

    /**
     * 充值ID（定单号）
     */
    private String rechargeId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 充值金额
     */
    private String actualMoney;
    /**
     * 充值类型（暂无数据）
     * 此功能暂未实现
     */
    private String rechargeType;
    /**
     * 充值状态
     */
    private String status;
    /**
     * 充值时间
     */
    private String time;
    /**
     * 到账时间
     */
    private String successTime;

    public RechargeDetailResponseDataDto() {

    }

    public RechargeDetailResponseDataDto(Recharge recharge) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.rechargeId = recharge.getId();
        this.userId = recharge.getUser().getId();
        this.actualMoney = recharge.getActualMoney().toString();
        this.status = recharge.getStatus();
        this.time = sdf.format(recharge.getTime());
        if(recharge.getSuccessTime()!=null) {
            this.successTime = sdf.format(recharge.getSuccessTime());
        }
        this.rechargeType = "None";
    }

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }
}
