package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class AccountModel implements Serializable {

    private static final long serialVersionUID = -1887375479975954346L;

    private long id;

    private String loginName;

    private String payUserId;

    private String payAccountId;

    private long balance;

    private long freeze;

    private Date registerTime;

    private boolean autoInvest;

    private boolean autoRepay;

    private boolean noPasswordInvest;

    private long membershipPoint;

    public long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public String getPayAccountId() {
        return payAccountId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getFreeze() {
        return freeze;
    }

    public void setFreeze(long freeze) {
        this.freeze = freeze;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public boolean isAutoRepay() {
        return autoRepay;
    }

    public void setAutoRepay(boolean autoRepay) {
        this.autoRepay = autoRepay;
    }

    public boolean isNoPasswordInvest() {
        return noPasswordInvest;
    }

    public void setNoPasswordInvest(boolean noPasswordInvest) {
        this.noPasswordInvest = noPasswordInvest;
    }

    public long getMembershipPoint() {
        return membershipPoint;
    }

    public void setMembershipPoint(long membershipPoint) {
        this.membershipPoint = membershipPoint;
    }
}
