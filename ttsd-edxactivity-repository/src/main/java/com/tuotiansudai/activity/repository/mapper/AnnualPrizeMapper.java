package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AnnualPrizeMapper {

    void create(AnnualPrizeModel annualPrizeModel);


    List<AnnualPrizeModel> findAnnualPrizeModels(@Param(value = "loginName") String loginName,
                                                         @Param(value = "index") Integer index,
                                                         @Param(value = "pageSize") Integer pageSize);

    AnnualPrizeModel find(@Param(value = "loginName") String loginName);

    void update(AnnualPrizeModel annualPrizeModel);


}
