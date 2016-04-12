package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.DebtRepaymentPlanView;
import com.tuotiansudai.repository.model.RepayStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepaymentPlanMapper {

    List<DebtRepaymentPlanView> findDebtRepaymentPlan(@Param("repayStatus") RepayStatus repayStatus);

    List<DebtRepaymentPlanView> findDebtRepaymentPlanDetail(@Param("repayStatus") RepayStatus repayStatus, @Param("date") String date);
}
