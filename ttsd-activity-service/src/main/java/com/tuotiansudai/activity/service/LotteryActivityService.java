package com.tuotiansudai.activity.service;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LotteryActivityService {

    private static Logger logger = Logger.getLogger(LotteryActivityService.class);

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public DrawLotteryResultDto drawLotteryPrize(String mobile){
        return lotteryDrawActivityService.drawPrizeByCompleteTask(mobile, ActivityCategory.AUTUMN_PRIZE);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile,LotteryPrize activityType){
        if(Strings.isNullOrEmpty(mobile)){
            return Lists.newArrayList();
        }
        return findDrawLotteryPrizeRecord(mobile,activityType);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecord(String mobile,LotteryPrize activityType){
        List<LotteryPrize> lotteryPrizes = activityType.equals(LotteryPrize.TOURISM) ? Lists.newArrayList(LotteryPrize.TOURISM,LotteryPrize.MANGO_CARD_100) : Lists.newArrayList(LotteryPrize.PORCELAIN_CUP,LotteryPrize.LUXURY);

        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, lotteryPrizes, ActivityCategory.AUTUMN_PRIZE, null);
        for(UserLotteryPrizeView view : userLotteryPrizeViews){
            view.setMobile(MobileEncryptor.encryptMiddleMobile(view.getMobile()));
        }
        return userLotteryPrizeViews;
    }
}
