package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargePaginationView;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class RechargePaginationItemDataDto extends RechargeDto implements Serializable {

    private long rechargeId;

    private String fee;

    private String status;

    private Date createdTime;

    private String userName;

    private String mobile;

    private String isStaff;

    public RechargePaginationItemDataDto(RechargeModel rechargeModel) {
        this.rechargeId = rechargeModel.getId();
        this.fee = AmountConverter.convertCentToString(rechargeModel.getFee());
        this.status = rechargeModel.getStatus().getDescription();
        this.createdTime = rechargeModel.getCreatedTime();
        super.setLoginName(rechargeModel.getLoginName());
        super.setBankCode(rechargeModel.getBankCode());
        super.setAmount(AmountConverter.convertCentToString(rechargeModel.getAmount()));
        super.setSource(rechargeModel.getSource());
        super.setFastPay(rechargeModel.isFastPay());
        super.setChannel(rechargeModel.getChannel());

        if (rechargeModel instanceof RechargePaginationView) {
            RechargePaginationView rechargeView = (RechargePaginationView) rechargeModel;
            this.userName = rechargeView.getUserName();
            this.mobile = rechargeView.getMobile();
            this.isStaff = rechargeView.getIsStaff();
        }
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isStaff() {
        return "1".equals(isStaff);
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}
