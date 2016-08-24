package com.tuotiansudai.activity.service;



import com.tuotiansudai.activity.dto.DrawLotteryActivityDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.UserLotteryDto;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ranking.DrawLotteryDto;

import java.util.Date;
import java.util.List;

public interface LotteryActivityService {

    List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile, LotteryPrize selectPrize, Date startTime, Date endTime, Integer index, Integer pageSize);

    int findUserLotteryPrizeCountViews(String mobile, LotteryPrize selectPrize, Date startTime, Date endTime);

    UserLotteryDto findUserLotteryByLoginName(String mobile);

    BaseDto<DrawLotteryActivityDto> drawLotteryPrize(String loginName,String drawType);

}
