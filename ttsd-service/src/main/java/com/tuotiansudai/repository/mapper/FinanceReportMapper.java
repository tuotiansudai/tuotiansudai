package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.FinanceReportView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FinanceReportMapper {
    List<FinanceReportView> findFinanceReportViews(@Param(value = "loanId") Long loanId,
                                                   @Param(value = "period") Integer period,
                                                   @Param(value = "investLoginName") String investLoginName,
                                                   @Param(value = "investStartTime") Date investStartTime,
                                                   @Param(value = "investEndTime") Date investEndTime);
}
