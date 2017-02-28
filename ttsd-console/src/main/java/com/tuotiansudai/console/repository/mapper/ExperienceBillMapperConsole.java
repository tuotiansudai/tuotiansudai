package com.tuotiansudai.console.repository.mapper;

import com.tuotiansudai.console.repository.model.ExperienceBillView;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBusinessType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository(value = "consoleExperienceBillMapper")
public interface ExperienceBillMapperConsole {

    List<ExperienceBillView> findExperienceBill(@Param(value = "mobile") String mobile,
                                                @Param(value = "startTime") Date startTime,
                                                @Param(value = "endTime") Date endTime,
                                                @Param(value = "operationType") ExperienceBillOperationType operationType,
                                                @Param(value = "businessType") ExperienceBusinessType businessType,
                                                @Param(value = "index") int index,
                                                @Param(value = "pageSize") int pageSize);

    int findCountExperienceBill(@Param(value = "mobile") String mobile,
                                @Param(value = "startTime") Date startTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "operationType") ExperienceBillOperationType operationType,
                                @Param(value = "businessType") ExperienceBusinessType businessType);

    long findSumExperienceBillAmount(@Param(value = "mobile") String mobile,
                                     @Param(value = "startTime") Date startTime,
                                     @Param(value = "endTime") Date endTime,
                                     @Param(value = "operationType") ExperienceBillOperationType operationType,
                                     @Param(value = "businessType") ExperienceBusinessType businessType);


}
