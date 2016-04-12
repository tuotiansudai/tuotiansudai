package com.tuotiansudai.coupon.service.impl;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
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

    public final static String EXCHANGE_CODE_KEY = "console:exchangeCode:";

    private final static int ONE_MONTH_SECOND = 60 * 60 * 24 * 30;

    private final static int RANDOM_SIZE = 10;

    // exclude I,O,R,0,2  26+10-5=31 31^10= 819,6282,8698,0801 819万亿
    private final static char[] chars = {
            'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '1',
            '3', '4', '5', '6', '7', '8',
            '9'
    };

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

        String prefix = toBase31Prefix(couponId);
        String randomCode;

        for (int i = 0; i < count; i++) {
            randomCode = genRandomCode(RANDOM_SIZE);
            while (redisWrapperClient.hexists(EXCHANGE_CODE_KEY + couponId, prefix + randomCode)) {
                randomCode = genRandomCode(RANDOM_SIZE);
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
     * parse a long positive value to a string representation in base 31
     * 4 bits length, max value: 31*31*31*31-1=923520
     *
     * @param couponId
     * @return
     */
    public String toBase31Prefix(long couponId) {
        char[] cs = {chars[0], chars[0], chars[0], chars[0]};
        int pos = 4;
        while (pos > 0 && couponId > 0) {
            int mod = (int) couponId % 31;
            cs[--pos] = chars[mod];
            couponId /= 31;
        }
        return new String(cs);
    }

}
