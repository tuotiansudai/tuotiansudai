package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.UserLotteryPrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserLotteryPrizeMapper {

    void create(UserLotteryPrizeModel userLotteryPrizeModel);

    void update(UserLotteryPrizeModel userLotteryPrizeModel);

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
}
