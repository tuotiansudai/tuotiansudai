package com.ttsd.api.dto;

import com.esoft.jdp2p.loan.model.Recharge;

/**
 * Created by tuotian on 15/8/7.
 */
public class RechargeRequestDto extends BaseParamDto{
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 充值金额
     */
    private String rechargeAmount;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
}
