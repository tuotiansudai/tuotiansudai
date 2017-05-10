package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.model.DragonBoatPunchCardPrize;
import com.tuotiansudai.coupon.service.impl.ExchangeCodeServiceImpl;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DragonBoatFestivalService {

    private static final Logger logger = LoggerFactory.getLogger(DragonBoatFestivalService.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public String getCouponExchangeCode(String loginName) {

        logger.info("[Dragon boat festival] {0} is fetching the prize.", loginName);

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

        logger.info("[Dragon boat festival] {0} draw a prize:{1}, exchagen code:{2}", loginName, prize, exchangeCode);

        return exchangeCode;

    }
}
