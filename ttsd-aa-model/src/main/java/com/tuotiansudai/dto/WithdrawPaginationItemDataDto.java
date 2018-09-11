package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;

public class WithdrawPaginationItemDataDto implements Serializable {

    private long withdrawId;

    private String loginName;

    private String mobile;

    private String userName;

    private String amount;

    private String fee;

    private Source source;

    private String status;

    private Date createdTime;

    private String isStaff;

    private String umpUserName;

    public WithdrawPaginationItemDataDto() {
    }

    public WithdrawPaginationItemDataDto(long withdrawId, String loginName, String mobile,String umpUserName,String userName, String amount, String fee, Source source, String status, Date createdTime, String isStaff) {
        this.withdrawId = withdrawId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.amount = amount;
        this.fee = fee;
        this.source = source;
        this.status = status;
        this.createdTime = createdTime;
        this.isStaff = isStaff;
        this.umpUserName=umpUserName;
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserName() {
        return userName;
    }

    public String getAmount() {
        return amount;
    }

    public String getFee() {
        return fee;
    }

    public Source getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public String getUmpUserName() {
        return umpUserName;
    }
}
