package com.tuotiansudai.coupon.service.impl;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.dto.BaseDataDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExchangeCodeServiceImpl implements ExchangeCodeService {

    private static Logger logger = Logger.getLogger(ExchangeCodeServiceImpl.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private CouponMapper couponMapper;

    public static String EXCHANGE_CODE_KEY = "console:exchangeCode:";

    private static int ONE_MONTH_SECOND = 60 * 60 * 24 * 30;

    // exclude I,O,R,0,2  26+10-5=31 31^11= 2,5000,0000,0000,0000 两点五亿亿
    private static char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '3', '4', '5', '6', '7', '8', '9'};

    @Override
    public boolean generateExchangeCode(long couponId, int count) {

        if (redisWrapperClient.exists(EXCHANGE_CODE_KEY + couponId)) {
            logger.error("generate exchange code failed, redis key already exists:" + EXCHANGE_CODE_KEY + couponId);
            return false;
        }

        CouponModel couponModel = couponMapper.findById(couponId);
        Date endTime = couponModel.getEndTime();
        Date now = new Date();
        int lifeSeconds = (int) ((endTime.getTime() - now.getTime()) / 1000 + ONE_MONTH_SECOND * 6); // delay 6 months

        genExchangeCode(couponId, count, lifeSeconds);
        return true;
    }


    private void genExchangeCode(long couponId, int count, int lifeSeconds) {
        logger.debug("generate exchange code, couponId:" + couponId + ", count:" + count + ", lifeSeconds:" + lifeSeconds);

        String prefix = genCouponIdPrefix(couponId);
        String randomCode;

        for (int i = 0; i < count; i++) {
            randomCode = genRandomCode(11);
            while (redisWrapperClient.hexists(EXCHANGE_CODE_KEY + couponId, prefix + randomCode)) {
                randomCode = genRandomCode(11);
            }
            redisWrapperClient.hset(EXCHANGE_CODE_KEY + couponId, prefix + randomCode, "0", lifeSeconds);
            logger.debug("generate exchange code, couponId:" + couponId + ", code:" + prefix + randomCode + ", lifeSeconds:" + lifeSeconds);
        }
    }


    /**
     * generate random code of size length, with A-Z, 0-9, exclude "I,O,R,0,2" for better reading for Chinese
     *
     * @param size
     * @return
     */
    private String genRandomCode(int size) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            int index = ((int) (Math.random() * 100000000)) % 31;
            sb.append(chars[index]);
        }
        return sb.toString();
    }

    /**
     * use the digits on the lowest 3 positions of couponId as the couponId prefix.
     * fill with "0" if the length is less than 3.
     *
     * @param couponId
     * @return
     */
    private String genCouponIdPrefix(long couponId) {
        long couponId3 = couponId % 1000;
        String prefixCouponId = String.valueOf(couponId3);
        if (couponId3 < 10) {
            prefixCouponId = "00" + prefixCouponId;
        } else if (couponId3 < 100) {
            prefixCouponId = "0" + prefixCouponId;
        }
        return prefixCouponId;
    }

    @Override
    public BaseDataDto exchange(String exchangeCode) {
        BaseDataDto baseDataDto = new BaseDataDto();
        long couponId = 0;
        try {
            couponId = Long.parseLong(exchangeCode.substring(0, 3));
        } catch (Exception e) {
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("请输入正确的兑换码");
            return baseDataDto;
        }
        CouponModel couponModel = couponMapper.findById(couponId);
        if (couponModel.getEndTime().before(new Date())) {
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("该兑换码已过期");
            return baseDataDto;
        }
        if (couponModel == null || exchangeCode.length() != 14 || !redisWrapperClient.hexists(EXCHANGE_CODE_KEY + couponId, exchangeCode)) {
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("请输入正确的兑换码");
            return baseDataDto;
        }
        if (redisWrapperClient.hget(EXCHANGE_CODE_KEY + couponId, exchangeCode).equals("1")) {
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("请输入正确的兑换码");
            return baseDataDto;
        } else {
            redisWrapperClient.hset(EXCHANGE_CODE_KEY + couponId, exchangeCode, "1");
            baseDataDto.setStatus(true);
            baseDataDto.setMessage("恭喜您兑换成功");
            return baseDataDto;
        }
    }

}
