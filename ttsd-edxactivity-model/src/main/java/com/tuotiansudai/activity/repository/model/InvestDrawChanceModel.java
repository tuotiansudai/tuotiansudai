package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestDrawChanceModel implements Serializable{

    private long id;
    private String loginName;
    private int chanceAmount;
    private String activityCategory;
    private Date createTime;

    public InvestDrawChanceModel(){}

    public InvestDrawChanceModel(String loginName,
                                 int chanceAmount,
                                 String activityCategory){
        this.loginName=loginName;
        this.chanceAmount=chanceAmount;
        this.activityCategory=activityCategory;
        this.createTime=new Date();
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

    public int getChanceAmount() {
        return chanceAmount;
    }

    public void setChanceAmount(int chanceAmount) {
        this.chanceAmount = chanceAmount;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
