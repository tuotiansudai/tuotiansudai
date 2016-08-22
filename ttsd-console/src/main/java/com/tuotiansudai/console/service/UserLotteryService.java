package com.tuotiansudai.console.service;


import com.tuotiansudai.repository.model.LotteryPrize;
import com.tuotiansudai.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.repository.model.UserLotteryTimeView;

import java.util.Date;
import java.util.List;

public interface UserLotteryService {

    List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile,Integer index,Integer pageSize);

    int findUserLotteryTimeCountViews(String mobile);

    List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime,Integer index,Integer pageSize);

    int findUserLotteryPrizeCountViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime);
}
