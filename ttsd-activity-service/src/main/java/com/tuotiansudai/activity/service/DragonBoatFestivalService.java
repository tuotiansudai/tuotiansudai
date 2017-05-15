package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.DragonBoatFestivalMapper;
import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import com.tuotiansudai.activity.repository.model.DragonBoatPunchCardPrize;
import com.tuotiansudai.coupon.service.impl.ExchangeCodeServiceImpl;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DragonBoatFestivalService {

    private static final Logger logger = LoggerFactory.getLogger(DragonBoatFestivalService.class);

    @Autowired
    private DragonBoatFestivalMapper dragonBoatFestivalMapper;

    @Autowired
    private UserMapper userMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String DRAGON_BOAT_EXCHANGE_FETCH = "dragon_boat_exchange_fetch:{0}:{1}";

    private static final ThreadLocal<SimpleDateFormat> SDF_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM-dd"));

    public String getCouponExchangeCode(String loginName) {
        logger.info("[Dragon boat festival] {} is fetching the prize.", loginName);

        String date = SDF_LOCAL.get().format(new Date());

        if (redisWrapperClient.exists(MessageFormat.format(DRAGON_BOAT_EXCHANGE_FETCH, date, loginName))) {
            logger.info("[Dragon boat festival] {} has fetched the prize today, can't fetch anymore.", loginName);
            return null;
        }

        int ramdom = (int) (Math.random() * 100000);
        int mod = ramdom % 100;

        DragonBoatPunchCardPrize prize;

        if (mod < 20) {
            prize = DragonBoatPunchCardPrize.RedEnveloper5;
        } else if (mod < 35) {
            prize = DragonBoatPunchCardPrize.RedEnveloper8;
        } else if (mod < 50) {
            prize = DragonBoatPunchCardPrize.RedEnveloper10;
        } else if (mod < 70) {
            prize = DragonBoatPunchCardPrize.RedEnveloper18;
        } else if (mod < 90) {
            prize = DragonBoatPunchCardPrize.RedEnveloper28;
        } else {
            prize = DragonBoatPunchCardPrize.InterestCoupon5;
        }

        String exchangeCode = redisWrapperClient.lpop(ExchangeCodeServiceImpl.EXCHANGE_CODE_LIST_KEY + prize.getCouponId());
        redisWrapperClient.set(MessageFormat.format(DRAGON_BOAT_EXCHANGE_FETCH, date, loginName), "1");

        logger.info("[Dragon boat festival] {} draw a prize:{}, exchange code:{}", loginName, prize, exchangeCode);

        return exchangeCode;
    }


    public void addInviteNewUserCount(String loginName){
        logger.info("[Dragon boat festival] add a new user count for user {}", loginName);
        UserModel userModel = userMapper.findByLoginName(loginName);
        dragonBoatFestivalMapper.addInviteNewUserCount(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile());
    }
}
