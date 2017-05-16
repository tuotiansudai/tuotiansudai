package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ExperienceBillModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExperienceBillMapper {

    void create(ExperienceBillModel experienceBillModel);

    ExperienceBillModel findById(long id);

    void update(ExperienceBillModel experienceBillModel);

    Date findLastExchangeTimeByLoginName(String loginName);

    List<ExperienceBillModel> findExperienceBillPagination(@Param(value = "loginName") String loginName,
                                                           @Param(value = "operationType") String operationType,
                                                           @Param(value = "index") int index,
                                                           @Param(value = "pageSize") int pageSize);

   long findCountExperienceBillPagination(@Param(value = "loginName") String loginName,
                                          @Param(value = "operationType") String operationType);

    long findSumExperienceBillByBusinessType(@Param(value = "loginName") String loginName,
                                           @Param(value = "businessType") String businessType);

}
