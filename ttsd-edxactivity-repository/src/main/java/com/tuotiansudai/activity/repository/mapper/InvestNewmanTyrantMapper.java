package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvestNewmanTyrantMapper {

    void create(InvestNewmanTyrantModel investNewmanTyrantModel);

    List<NewmanTyrantView> findNewmanTyrantByTradingTime(@Param(value = "tradingTime") Date tradingTime,
                                                         @Param(value = "activityBeginTime") String activityBeginTime,
                                                         @Param(value = "activityEndTime") String activityEndTime,
                                                         @Param(value = "isNewman") boolean isNewman);

    List<NewmanTyrantView> findNewmanTyrantByTradingTimeWithEnd(@Param(value = "tradingStartTime") Date tradingStartTime,
                                                                @Param(value = "tradingEndTime") Date tradingEndTime,
                                                                @Param(value = "activityBeginTime") String activityBeginTime,
                                                                @Param(value = "activityEndTime") String activityEndTime);
}
