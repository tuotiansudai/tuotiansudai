package com.tuotiansudai.membership.repository.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MembershipExperienceBillDto implements Serializable{

    private long id;
    private String loginName;
    private long experience;
    private long totalExperience;
    private String createdTime;
    private String description;


    public MembershipExperienceBillDto(){

    }

    public MembershipExperienceBillDto(MembershipExperienceBillModel membershipExperienceBillModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.loginName = membershipExperienceBillModel.getLoginName();
        this.experience = membershipExperienceBillModel.getExperience();
        this.totalExperience = membershipExperienceBillModel.getTotalExperience();
        this.createdTime = sdf.format(membershipExperienceBillModel.getCreatedTime());
        this.description = membershipExperienceBillModel.getDescription();
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
