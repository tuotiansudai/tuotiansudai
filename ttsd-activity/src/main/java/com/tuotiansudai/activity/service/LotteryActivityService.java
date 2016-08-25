package com.tuotiansudai.activity.service;



import com.tuotiansudai.activity.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.UserLotteryDto;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.dto.BaseDto;

import java.util.Date;
import java.util.List;

public interface LotteryActivityService {

    BaseDto<UserLotteryDto> findUserLotteryByLoginName(String mobile);

    BaseDto<DrawLotteryResultDto> drawLotteryPrize(String mobile,String drawType);

    List<UserLotteryPrizeView> findDrawLotteryPrizeRecord(String mobile,Integer index,Integer pageSize);
}
