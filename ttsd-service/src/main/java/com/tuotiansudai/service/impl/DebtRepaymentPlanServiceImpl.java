package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.DebtRepaymentPlanMapper;
import com.tuotiansudai.repository.model.DebtRepaymentPlanView;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.DebtRepaymentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtRepaymentPlanServiceImpl implements DebtRepaymentPlanService{

    @Autowired
    private DebtRepaymentPlanMapper debtRepaymentPlanMapper;

    @Override
    public List<DebtRepaymentPlanView> findDebtRepaymentPlan(RepayStatus repayStatus) {
        return debtRepaymentPlanMapper.findDebtRepaymentPlan(repayStatus);
    }

    @Override
    public List<DebtRepaymentPlanView> findDebtRepaymentPlanDetail(RepayStatus repayStatus, String date) {
        return debtRepaymentPlanMapper.findDebtRepaymentPlanDetail(repayStatus,date);
    }
}
