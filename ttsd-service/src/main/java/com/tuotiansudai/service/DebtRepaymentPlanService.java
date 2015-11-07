package com.tuotiansudai.service;


import com.tuotiansudai.repository.model.DebtRepaymentPlanView;
import com.tuotiansudai.repository.model.RepayStatus;

import java.util.Date;
import java.util.List;

public interface DebtRepaymentPlanService {

    List<DebtRepaymentPlanView> findDebtRepaymentPlan(RepayStatus repayStatus);

    List<DebtRepaymentPlanView> findDebtRepaymentPlanDetail(RepayStatus repayStatus, String date);
}
