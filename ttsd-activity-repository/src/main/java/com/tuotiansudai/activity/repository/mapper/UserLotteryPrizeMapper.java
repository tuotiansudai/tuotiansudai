package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserLotteryPrizeMapper {

    void create(UserLotteryPrizeModel userLotteryPrizeModel);


    List<UserLotteryPrizeView> findUserLotteryPrizeViews(@Param(value = "mobile") String mobile,
                                                         @Param(value = "lotteryPrize") LotteryPrize lotteryPrize,
                                                         @Param(value = "startTime") Date startTime,
                                                         @Param(value = "endTime") Date endTime,
                                                         @Param(value = "index") Integer index,
                                                         @Param(value = "pageSize") Integer pageSize);

    int findUserLotteryPrizeCountViews(@Param(value = "mobile") String mobile,
                                       @Param(value = "lotteryPrize") LotteryPrize LotteryPrize,
                                       @Param(value = "startTime") Date startTime,
                                       @Param(value = "endTime") Date endTime);

    List<UserLotteryPrizeView> findLotteryPrizeByMobile(@Param(value = "mobile") String mobile);
}
