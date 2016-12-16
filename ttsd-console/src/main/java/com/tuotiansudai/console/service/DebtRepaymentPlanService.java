package com.tuotiansudai.console.service;

import com.tuotiansudai.repository.mapper.DebtRepaymentPlanMapper;
import com.tuotiansudai.repository.model.DebtRepaymentPlanView;
import com.tuotiansudai.repository.model.RepayStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtRepaymentPlanService {

    @Autowired
    private DebtRepaymentPlanMapper debtRepaymentPlanMapper;

    public List<DebtRepaymentPlanView> findDebtRepaymentPlan(RepayStatus repayStatus) {
        return debtRepaymentPlanMapper.findDebtRepaymentPlan(repayStatus);
    }

    public List<DebtRepaymentPlanView> findDebtRepaymentPlanDetail(RepayStatus repayStatus, String date) {
        return debtRepaymentPlanMapper.findDebtRepaymentPlanDetail(repayStatus, date);
    }
}
