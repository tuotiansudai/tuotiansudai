package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeSelectModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ZeroShoppingPrizeSelectMapper {

    List<ZeroShoppingPrizeSelectModel> findByMobileAndDate(@Param(value = "mobile") String mobile,
                                                           @Param(value = "startTime") Date startTime,
                                                           @Param(value = "endTime") Date endTime);

}
