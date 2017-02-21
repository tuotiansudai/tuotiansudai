package com.tuotiansudai.console.repository.mapper;

import com.tuotiansudai.console.repository.model.InvestRepayExperienceView;
import com.tuotiansudai.repository.model.RepayStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository(value = "consoleInvestRepayMapper")
public interface InvestRepayMapperConsole {

    List<InvestRepayExperienceView> findInvestRepayExperience(@Param(value = "mobile") String mobile,
                                                              @Param(value = "repayDateMin") Date repayDateMin,
                                                              @Param(value = "repayDateMax") Date repayDateMax,
                                                              @Param(value = "repayStatus") RepayStatus repayStatus,
                                                              @Param(value = "index") int index,
                                                              @Param(value = "pageSize") int pageSize);

    int findCountInvestRepayExperience(@Param(value = "mobile") String mobile,
                                       @Param(value = "repayDateMin") Date repayDateMin,
                                       @Param(value = "repayDateMax") Date repayDateMax,
                                       @Param(value = "repayStatus") RepayStatus repayStatus);

    long findSumExpectedInterestExperience(@Param(value = "mobile") String mobile,
                                           @Param(value = "repayDateMin") Date repayDateMin,
                                           @Param(value = "repayDateMax") Date repayDateMax,
                                           @Param(value = "repayStatus") RepayStatus repayStatus);

    long findSumActualInterestExperience(@Param(value = "mobile") String mobile,
                                         @Param(value = "repayDateMin") Date repayDateMin,
                                         @Param(value = "repayDateMax") Date repayDateMax,
                                         @Param(value = "repayStatus") RepayStatus repayStatus);
}
