package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.DragonBoatFestivalMapper;
import com.tuotiansudai.activity.repository.model.DragonBoatPunchCardPrize;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.impl.ExchangeCodeServiceImpl;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
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

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private MQWrapperClient mqWrapperClient;


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


    public void addInviteOldUserCount(String loginName) {
        logger.info("[Dragon boat festival] add a old user count for user {}", loginName);
        UserModel userModel = userMapper.findByLoginName(loginName);
        dragonBoatFestivalMapper.addInviteOldUserCount(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile());
    }

    private void addInviteNewUserCount(String loginName) {
        logger.info("[Dragon boat festival] add a new user count for user {}", loginName);
        UserModel userModel = userMapper.findByLoginName(loginName);
        dragonBoatFestivalMapper.addInviteNewUserCount(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile());
    }


    public void afterNewUserRegister(String regiseterUserMobile, String referrer) {

        // 给新用户发10元红包优惠券
        logger.info(MessageFormat.format("[Dragon Boat Register User {}] assign weiXin share ￥10 red enveloper ", regiseterUserMobile));
        couponAssignmentService.assignUserCoupon(regiseterUserMobile, 421);

        // 给分享者增加邀请用户数量
        logger.info(MessageFormat.format("[Dragon Boat Register User {}] add invite-new-user-count for {}", regiseterUserMobile, referrer));
        addInviteNewUserCount(referrer);

        // 给分享者发放5000元体验金
        logger.info(MessageFormat.format("[Dragon Boat Register User {}] send ￥5000 experience to {} for inviting new user.", regiseterUserMobile, referrer));
        mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                new ExperienceAssigningMessage(referrer, 500000, ExperienceBillOperationType.IN, ExperienceBillBusinessType.DRAGON_BOAT));
    }
}
