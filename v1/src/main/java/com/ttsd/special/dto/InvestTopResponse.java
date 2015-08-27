package com.ttsd.special.dto;

import com.ttsd.util.ChinaArea;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InvestTopResponse implements Serializable{
    private Date  updateTime;
    private Date beginTime;
    private Date endTime;
    private Map<ChinaArea,List<InvestTopItem>> areaInvestments;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Map<ChinaArea, List<InvestTopItem>> getAreaInvestments() {
        return areaInvestments;
    }

    public void setAreaInvestments(Map<ChinaArea, List<InvestTopItem>> areaInvestments) {
        this.areaInvestments = areaInvestments;
    }
}
