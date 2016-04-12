package com.esoft.jdp2p.creditorRepayPlan.controller;

import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.creditorRepayPlan.model.CreditorRepayPlan;
import com.esoft.jdp2p.creditorRepayPlan.service.CreditorRepayPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/6/15.
 */
@Component
@Scope(ScopeType.VIEW)
public class CreditorRepayPlanList {

    @Autowired
    private CreditorRepayPlanService creditorRepayPlanService;

    private List<CreditorRepayPlan> searchList;

    private List<CreditorRepayPlan> searchDetail;
    private Date startTime;
    private Date endTime;

    private String status;
    private String repayTime;

    public CreditorRepayPlanList(){
        this.status = "unComplete";
    }
    public List<CreditorRepayPlan> getSearchList() {
        return creditorRepayPlanService.searchList(status,startTime,endTime);
    }

    public void setSearchList(List<CreditorRepayPlan> searchList) {
        this.searchList = searchList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public List<CreditorRepayPlan> getSearchDetail() {
        return creditorRepayPlanService.searchDetail(repayTime);
    }

    public void setSearchDetail(List<CreditorRepayPlan> searchDetail) {
        this.searchDetail = searchDetail;
    }
}
