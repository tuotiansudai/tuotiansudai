package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class UserLotteryPrizeMapperTest extends BaseMapperTest{

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Test
    public void shouldCreateIsOk(){
        userLotteryPrizeMapper.create(getUserLotteryPrizeModel("testUserLotteryPrize", "12312341113", LotteryPrize.INTEREST_COUPON_2));
    }

    @Test
    public void shouldFindLotteryPrizeByMobileAndPrizeIsOk(){
        String mobile = "12312341113";
        userLotteryPrizeMapper.create(getUserLotteryPrizeModel("testUserLotteryPrize", mobile, LotteryPrize.INTEREST_COUPON_2));
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile,null,null, null);
        assertTrue(CollectionUtils.isNotEmpty(userLotteryPrizeViews));
    }

    @Test
    public void shouldFindUserLotteryPrizeViewsIsOk(){
        String mobile = "12312341113";
        userLotteryPrizeMapper.create(getUserLotteryPrizeModel("testUserLotteryPrize", mobile, LotteryPrize.INTEREST_COUPON_2));
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, LotteryPrize.INTEREST_COUPON_2,null, DateUtils.addDays(DateTime.now().toDate(),-10),DateUtils.addDays(DateTime.now().toDate(),10),0,10);
        assertTrue(CollectionUtils.isNotEmpty(userLotteryPrizeViews));
    }

    private UserLotteryPrizeModel getUserLotteryPrizeModel(String loginName,String mobile,LotteryPrize lotteryPrize){
        return new UserLotteryPrizeModel(mobile,loginName,null,lotteryPrize, DateTime.now().toDate(), ActivityCategory.AUTUMN_PRIZE);
    }


}
