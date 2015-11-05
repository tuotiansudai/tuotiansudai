package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.utils.AmountUtil;

import java.io.Serializable;
import java.util.Date;

public class RechargePaginationItemDataDto extends RechargeDto implements Serializable {

    private long rechargeId;

    private String fee;

    private String status;

    private Date createdTime;


    public RechargePaginationItemDataDto(RechargeModel rechargeModel) {
        this.rechargeId = rechargeModel.getId();
        this.fee = AmountUtil.convertCentToString(rechargeModel.getFee());
        this.status = rechargeModel.getStatus().getDescription();
        this.createdTime = rechargeModel.getCreatedTime();
        super.setLoginName(rechargeModel.getLoginName());
        super.setBankCode(rechargeModel.getBankCode());
        super.setAmount(AmountUtil.convertCentToString(rechargeModel.getAmount()));
        super.setSource(rechargeModel.getSource());
        super.setFastPay(rechargeModel.isFastPay());
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(long rechargeId) {
        this.rechargeId = rechargeId;
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

}
