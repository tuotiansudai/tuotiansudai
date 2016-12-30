package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnnualPrizeMapper {

    void create(AnnualPrizeModel annualPrizeModel);


    List<AnnualPrizeModel> findAnnualPrizeModels(@Param(value = "mobile") String mobile,
                                                 @Param(value = "index") Integer index,
                                                 @Param(value = "pageSize") Integer pageSize);

    long findAnnualPrizeCount(@Param(value = "mobile") String mobile);

    AnnualPrizeModel findByMobile(@Param(value = "mobile") String mobile);

    void update(AnnualPrizeModel annualPrizeModel);


}
