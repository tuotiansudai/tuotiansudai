package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ExchangePrize;
import com.tuotiansudai.activity.repository.model.UserExchangePrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserExchangePrizeMapper {

    void create(UserExchangePrizeModel userExchangePrizeModel);

    void updatePrize(UserExchangePrizeModel userExchangePrizeModel);

    List<UserExchangePrizeModel> findUserExchangePrizeViews(@Param(value = "mobile") String mobile,
                                                            @Param(value = "userName") String userName,
                                                            @Param(value = "activityCategory") ActivityCategory activityCategory,
                                                            @Param(value = "startTime") Date startTime,
                                                            @Param(value = "endTime") Date endTime);

    List<UserExchangePrizeModel> findUserExchangePrizeByMobile(@Param(value = "mobile") String mobile,
                                                               @Param(value = "activityCategory") ActivityCategory activityCategory);


}
