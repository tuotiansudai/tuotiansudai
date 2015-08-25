package com.ttsd.special.dto;

import java.util.Date;
import java.util.List;

public class InvestTopList {
    private Date  updateTime;
    /**
     * 全国
     */
    private List<InvestTopItem> ChinaInvestTopList;
    /**
     * 华北
     */
    private List<InvestTopItem> NorthInvestTopList;
    /**
     * 东北
     */
    private List<InvestTopItem> NorthEastInvestTopList;
    /**
     * 华东
     */
    private List<InvestTopItem> EastInvestTopList;
    /**
     * 中南
     */
    private List<InvestTopItem> SouthInvestTopList;
    /**
     * 西南
     */
    private List<InvestTopItem> SouthWestInvestTopList;
    /**
     * 西北
     */
    private List<InvestTopItem> NorthWestInvestTopList;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<InvestTopItem> getChinaInvestTopList() {
        return ChinaInvestTopList;
    }

    public void setChinaInvestTopList(List<InvestTopItem> chinaInvestTopList) {
        ChinaInvestTopList = chinaInvestTopList;
    }

    public List<InvestTopItem> getNorthInvestTopList() {
        return NorthInvestTopList;
    }

    public void setNorthInvestTopList(List<InvestTopItem> northInvestTopList) {
        NorthInvestTopList = northInvestTopList;
    }

    public List<InvestTopItem> getNorthEastInvestTopList() {
        return NorthEastInvestTopList;
    }

    public void setNorthEastInvestTopList(List<InvestTopItem> northEastInvestTopList) {
        NorthEastInvestTopList = northEastInvestTopList;
    }

    public List<InvestTopItem> getEastInvestTopList() {
        return EastInvestTopList;
    }

    public void setEastInvestTopList(List<InvestTopItem> eastInvestTopList) {
        EastInvestTopList = eastInvestTopList;
    }

    public List<InvestTopItem> getSouthInvestTopList() {
        return SouthInvestTopList;
    }

    public void setSouthInvestTopList(List<InvestTopItem> southInvestTopList) {
        SouthInvestTopList = southInvestTopList;
    }

    public List<InvestTopItem> getSouthWestInvestTopList() {
        return SouthWestInvestTopList;
    }

    public void setSouthWestInvestTopList(List<InvestTopItem> southWestInvestTopList) {
        SouthWestInvestTopList = southWestInvestTopList;
    }

    public List<InvestTopItem> getNorthWestInvestTopList() {
        return NorthWestInvestTopList;
    }

    public void setNorthWestInvestTopList(List<InvestTopItem> northWestInvestTopList) {
        NorthWestInvestTopList = northWestInvestTopList;
    }
}
