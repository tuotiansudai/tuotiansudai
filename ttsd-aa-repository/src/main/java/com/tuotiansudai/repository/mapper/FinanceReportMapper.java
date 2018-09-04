package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.FinanceReportItemView;
import com.tuotiansudai.repository.model.PreferenceType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FinanceReportMapper {
    List<FinanceReportItemView> findFinanceReportViews(@Param(value = "loanId") Long loanId,
                                                       @Param(value = "period") Integer period,
                                                       @Param(value = "investLoginName") String investLoginName,
                                                       @Param(value = "investStartTime") Date investStartTime,
                                                       @Param(value = "investEndTime") Date investEndTime,
                                                       @Param(value = "repayStartTime") Date repayStartTime,
                                                       @Param(value = "repayEndTime") Date repayEndTime,
                                                       @Param(value = "preferenceType") PreferenceType preferenceType,
                                                       @Param(value = "index") int index,
                                                       @Param(value = "pageSize") int pageSize);

    int findCountFinanceReportViews(@Param(value = "loanId") Long loanId,
                                    @Param(value = "period") Integer period,
                                    @Param(value = "investLoginName") String investLoginName,
                                    @Param(value = "investStartTime") Date investStartTime,
                                    @Param(value = "investEndTime") Date investEndTime,
                                    @Param(value = "repayStartTime") Date repayStartTime,
                                    @Param(value = "repayEndTime") Date repayEndTime,
                                    @Param(value = "preferenceType") PreferenceType preferenceType);
}
