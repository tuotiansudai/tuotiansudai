package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvestCelebrationHeroRankingMapper {

    void create(InvestNewmanTyrantModel investNewmanTyrantModel);

    List<NewmanTyrantView> findCelebrationHeroRankingByTradingTime(@Param(value = "tradingTime") Date tradingTime,
                                                         @Param(value = "activityBeginTime") String activityBeginTime,
                                                         @Param(value = "activityEndTime") String activityEndTime);


}
