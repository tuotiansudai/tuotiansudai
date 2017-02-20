package com.tuotiansudai.membership.repository.model;

import java.io.Serializable;
import java.util.Date;

public class MembershipPrivilegeModel implements Serializable{
    private long id;
    private String loginName;
    private MembershipPrivilege privilege;
    private Date startTime;
    private Date endTime;
    private Date createdTime;

    public MembershipPrivilegeModel(){}

    public MembershipPrivilegeModel(String loginName,MembershipPrivilege privilege,Date startTime,Date endTime){
        this.loginName = loginName;
        this.privilege = privilege;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdTime = new Date();
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

    public MembershipPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(MembershipPrivilege privilege) {
        this.privilege = privilege;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
