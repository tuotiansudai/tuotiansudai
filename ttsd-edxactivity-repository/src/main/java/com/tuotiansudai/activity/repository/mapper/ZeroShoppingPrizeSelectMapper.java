package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeSelectModel;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeSelectView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ZeroShoppingPrizeSelectMapper {

    void create(ZeroShoppingPrizeSelectModel zeroShoppingPrizeSelectModel);

    List<ZeroShoppingPrizeSelectModel> findByMobileAndDate(@Param(value = "mobile") String mobile,
                                                          @Param(value = "startTime") Date startTime,
                                                          @Param(value = "endTime") Date endTime);

}
