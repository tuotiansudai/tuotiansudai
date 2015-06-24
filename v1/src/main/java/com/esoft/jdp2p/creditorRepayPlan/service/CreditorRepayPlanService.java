package com.esoft.jdp2p.creditorRepayPlan.service;

import com.esoft.jdp2p.creditorRepayPlan.model.CreditorRepayPlan;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/6/16.
 */
public interface CreditorRepayPlanService {
    public List<CreditorRepayPlan> searchList(String status,Date startTime,Date endTime);
    public List<CreditorRepayPlan> searchDetail(String repayTime);
}
