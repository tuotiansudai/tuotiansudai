package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.enums.RechargeStatus;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class SystemRechargeModel implements Serializable {

    private long id;

    private String loginName;

    private Date time;

    private long amount;

    private Date successTime;

    private RechargeStatus status;

    private String remark;

    public SystemRechargeModel(SystemRechargeDto dto, String loginName){
        this.loginName = loginName;
        this.time = new Date();
        this.amount = AmountConverter.convertStringToCent(dto.getAmount());
        this.status = RechargeStatus.WAIT_PAY;
    }
    public SystemRechargeModel(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public RechargeStatus getStatus() {
        return status;
    }

    public void setStatus(RechargeStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
