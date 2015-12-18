package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.AgentDto;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class AgentLevelRateModel implements Serializable {
    private long id;
    private String loginName;
    private int level;
    private double rate;
    private Date inputTime;
    private Date updateTime;

    public AgentLevelRateModel(){

    }
    public AgentLevelRateModel(AgentDto agentDto){
        if(StringUtils.isNotEmpty(agentDto.getId())){
            this.id = Long.parseLong(agentDto.getId());
        }
        this.loginName = agentDto.getLoginName();
        this.level = Integer.parseInt(agentDto.getLevel());
        this.rate = Double.parseDouble(agentDto.getRate());
        this.inputTime = new Date();
        this.updateTime = new Date();
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
