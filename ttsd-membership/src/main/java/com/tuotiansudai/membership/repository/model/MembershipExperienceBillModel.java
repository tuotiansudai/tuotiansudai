package com.tuotiansudai.membership.repository.model;

import java.io.Serializable;
import java.util.Date;

public class MembershipExperienceBillModel implements Serializable{

    private long id;
    private String loginName;
    private long experience;
    private long totalExperience;
    private Date createdTime;
    private String description;

    public MembershipExperienceBillModel(){

    }

    public MembershipExperienceBillModel(String loginName, long experience, long totalExperience, String description) {
        this.loginName = loginName;
        this.experience = experience;
        this.totalExperience = totalExperience;
        this.createdTime = new Date();
        this.description = description;
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

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public long getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(long totalExperience) {
        this.totalExperience = totalExperience;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
