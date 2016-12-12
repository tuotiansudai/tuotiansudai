package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AnnualPrizeMapper {

    void create(AnnualPrizeModel annualPrizeModel);


    List<AnnualPrizeModel> findAnnualPrizeModels(@Param(value = "mobile") String mobile,
                                                         @Param(value = "index") Integer index,
                                                         @Param(value = "pageSize") Integer pageSize);

    long findAnnualPrizeCount(@Param(value = "mobile") String mobile);

    AnnualPrizeModel find(@Param(value = "mobile") String mobile);

    void update(AnnualPrizeModel annualPrizeModel);


}
