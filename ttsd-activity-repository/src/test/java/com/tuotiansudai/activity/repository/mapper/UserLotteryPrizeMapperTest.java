package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

public class UserLotteryPrizeMapperTest extends BaseMapperTest{

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;


    public void shouldCreateIsOk(){

    }


    public UserLotteryPrizeModel getUserLotteryPrizeModel(String loginName,String mobile,LotteryPrize lotteryPrize){
        return new UserLotteryPrizeModel(mobile,loginName,lotteryPrize, DateTime.now().toDate());
    }


}
