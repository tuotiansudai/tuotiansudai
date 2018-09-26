package com.tuotiansudai.coupon.service;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.dto.BaseWrapperDataDto;
import com.tuotiansudai.dto.UserCouponDto;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeCodeService {

    private static Logger logger = Logger.getLogger(ExchangeCodeService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    public final static String EXCHANGE_CODE_KEY = "console:exchangeCode:";

    public final static String EXCHANGE_CODE_LIST_KEY = "console:exchangeCode:list:";

    private final static int ONE_MONTH_SECOND = 60 * 60 * 24 * 30;

    private final static int RANDOM_SIZE = 10;

    private final static int EXCHANGE_CODE_LENGTH = 14;

    private final static int DAILY_EXCHANGE_LIMIT = 2;

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

    public List<String> getExchangeCodes(long couponId) {
        Map<String, String> map = redisWrapperClient.hgetAll(ExchangeCodeService.EXCHANGE_CODE_KEY + couponId);
        if (map == null) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(map.keySet());
    }

    private void genExchangeCode(long couponId, int count, int lifeSeconds) {
        logger.info("generate exchange code, couponId:" + couponId + ", count:" + count + ", lifeSeconds:" + lifeSeconds);

        String prefix = toBase31Prefix(couponId);
        String randomCode;

        for (int i = 0; i < count; i++) {
            randomCode = genRandomCode(RANDOM_SIZE);
            while (redisWrapperClient.hexists(EXCHANGE_CODE_KEY + couponId, prefix + randomCode)) {
                randomCode = genRandomCode(RANDOM_SIZE);
            }
            redisWrapperClient.hset(EXCHANGE_CODE_KEY + couponId, prefix + randomCode, "0", lifeSeconds);
            // FIXME：only for 端午节优惠券兑换码
            redisWrapperClient.lpush(EXCHANGE_CODE_LIST_KEY + couponId, prefix + randomCode);
            logger.info("generate exchange code, couponId:" + couponId + ", code:" + prefix + randomCode + ", lifeSeconds:" + lifeSeconds);
        }
        redisWrapperClient.expire(EXCHANGE_CODE_LIST_KEY + couponId, lifeSeconds);
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

    public BaseWrapperDataDto<UserCouponDto> exchange(String loginName, String exchangeCode) {
        BaseWrapperDataDto<UserCouponDto> baseDataDto = new BaseWrapperDataDto<>();
        long couponId = getValueBase31(exchangeCode);
        CouponModel couponModel = couponMapper.findById(couponId);
        if (!checkExchangeCodeCorrect(exchangeCode, couponId, couponModel)) {
            baseDataDto.setMessage("请输入正确的兑换码");
            return baseDataDto;
        }
        if (checkExchangeCodeExpire(couponModel)) {
            baseDataDto.setMessage("该兑换码已过期");
            return baseDataDto;
        }
        if (checkExchangeCodeUsed(couponId, exchangeCode)) {
            baseDataDto.setMessage("该兑换码已被使用");
            return baseDataDto;
        }
        if (checkExchangeCodeDailyCount(loginName)) {
            baseDataDto.setMessage("当天兑换次数达到上限");
            return baseDataDto;
        }
        UserCouponModel userCouponModel = couponAssignmentService.assignUserCoupon(loginName, exchangeCode);
        if (userCouponModel == null) {
            baseDataDto.setMessage("兑换码兑换失败");
            return baseDataDto;
        }
        redisWrapperClient.hset(EXCHANGE_CODE_KEY + couponId, exchangeCode, "1");
        baseDataDto.setStatus(true);
        baseDataDto.setMessage("恭喜您兑换成功");
        baseDataDto.setData(new UserCouponDto(couponModel, userCouponModel, 0));
        return baseDataDto;
    }

    public boolean checkExchangeCodeDailyCount(String loginName) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
        UnmodifiableIterator<UserCouponModel> filter = Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel input) {
                return input.getExchangeCode() != null && Days.daysBetween(new LocalDate(input.getCreatedTime()), new LocalDate()).getDays() < 1;
            }
        });
        return Iterators.size(filter) >= DAILY_EXCHANGE_LIMIT;
    }

    public boolean checkExchangeCodeUsed(long couponId, String exchangeCode) {
        return redisWrapperClient.hget(EXCHANGE_CODE_KEY + couponId, exchangeCode).equals("1");
    }

    public boolean checkExchangeCodeExpire(CouponModel couponModel) {
        return couponModel.getEndTime().before(new Date());
    }

    public boolean checkExchangeCodeCorrect(String exchangeCode, long couponId, CouponModel couponModel) {
        return couponModel != null && exchangeCode.length() == EXCHANGE_CODE_LENGTH && redisWrapperClient.hexists(EXCHANGE_CODE_KEY + couponId, exchangeCode);
    }

    /**
     * return the int value of a base 31 input string (especially for exchange code prefix)
     *
     * @param exchangeCode
     * @return
     */
    public long getValueBase31(String exchangeCode) {
        if (exchangeCode == null || exchangeCode.length() != 14) return 0;
        String prefix = exchangeCode.substring(0, 4);
        int value = 0;
        try {
            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);
                int index = getExchangeCodeCharIndex(c);
                value = value * 31 + index;
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            return 0;
        }
        return value;
    }

    private int getExchangeCodeCharIndex(char c) throws Exception {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) return i;
        }
        throw new Exception("input value '" + c + "' is out of range.");
    }

}
