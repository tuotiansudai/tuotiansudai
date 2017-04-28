package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.dto.WechatLotteryDto;
import com.tuotiansudai.activity.repository.model.WechatLotteryPrize;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WechatLotteryService {

    private static final Logger logger = LoggerFactory.getLogger(WechatLotteryService.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String WECHAT_LOTTERY_COUNT_KEY = "WECHAT_LOTTERY_COUNT:";

    private static final int THIRTY_DAYS = 60 * 60 * 24 * 30;

    private static final String WECHAT_PRIZE_1_KEY = "WECHAT_PRIZE_1";

    private static final String WECHAT_PRIZE_2_KEY = "WECHAT_PRIZE_2";

    @Autowired
    private CouponAssignmentService couponAssignmentService;


    public BaseDto<WechatLotteryDto> drawLottery(String loginName) {

        BaseDto<WechatLotteryDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        WechatLotteryDto dto = new WechatLotteryDto();
        dto.setStatus(true);
        dto.setReturnCode(0);
        baseDto.setData(dto);

        Long leftDrawCount = redisWrapperClient.decrEx(WECHAT_LOTTERY_COUNT_KEY + loginName, THIRTY_DAYS, 1);
        if (leftDrawCount < 0) {
            redisWrapperClient.del(WECHAT_LOTTERY_COUNT_KEY + loginName);
            logger.error("no draw count left, someone is cheating. loginName:{0}", loginName);
            dto.setReturnCode(1);
            dto.setMessage("您的抽奖次数已用完，投资可以获得更多的抽奖机会！");
            dto.setStatus(false);
            return baseDto;
        }

        long random = (long) (Math.random() * 1000000);
        long mod = random % 100;
        if (mod == 0) {
            if (redisWrapperClient.exists(WECHAT_PRIZE_1_KEY)) {
                sendRedEnvelopCoupon20(loginName);
                dto.setWechatLotteryPrize(WechatLotteryPrize.RedEnvelop20);
                return baseDto;
            } else {
                redisWrapperClient.setex(WECHAT_PRIZE_1_KEY, THIRTY_DAYS, "1");
                dto.setWechatLotteryPrize(WechatLotteryPrize.Bedclothes);
                return baseDto;
            }
        } else if (mod == 1 || mod == 2) {
            if (redisWrapperClient.exists(WECHAT_PRIZE_2_KEY)) {
                dto.setWechatLotteryPrize(WechatLotteryPrize.RedEnvelop20);
                return baseDto;
            } else {
                redisWrapperClient.setex(WECHAT_PRIZE_2_KEY, THIRTY_DAYS, "1");
                dto.setWechatLotteryPrize(WechatLotteryPrize.Bag);
                return baseDto;
            }
        } else if (mod < 10) {
            dto.setWechatLotteryPrize(WechatLotteryPrize.Headgear);
            return baseDto;
        } else if (mod < 30) {
            dto.setWechatLotteryPrize(WechatLotteryPrize.Towel);
            return baseDto;
        } else {
            sendRedEnvelopCoupon20(loginName);
            dto.setWechatLotteryPrize(WechatLotteryPrize.RedEnvelop20);
            return baseDto;
        }
    }

    private void sendRedEnvelopCoupon20(String loginName) {
        couponAssignmentService.assignUserCoupon(loginName, 409L);
    }

    public int getLeftDrawCount(String loginName) {
        String count = redisWrapperClient.get(WECHAT_LOTTERY_COUNT_KEY + loginName);
        return Integer.parseInt(count);
    }

}
