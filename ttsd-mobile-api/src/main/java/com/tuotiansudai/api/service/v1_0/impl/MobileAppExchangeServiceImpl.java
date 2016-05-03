package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.MobileAppExchangeService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;


@Service
public class MobileAppExchangeServiceImpl implements MobileAppExchangeService{

    static Logger logger = Logger.getLogger(MobileAppExchangeServiceImpl.class);
    @Autowired
    private ExchangeCodeService exchangeCodeService;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponActivationService couponActivationService;
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private UserCouponMapper userCouponMapper;


    public final static String EXCHANGE_CODE_KEY = "console:exchangeCode:";

    @Override
    @Transactional
    public BaseResponseDto exchange(ExchangeRequestDto exchangeRequestDto) {
        String loginName = exchangeRequestDto.getBaseParam().getUserId();
        final String exchangeCode = exchangeRequestDto.getExchangeCode();
        long couponId = exchangeCodeService.getValueBase31(exchangeCode);
        final CouponModel couponModel = couponMapper.findById(couponId);
        if (!exchangeCodeService.checkExchangeCodeCorrect(exchangeCode, couponId, couponModel)) {
           return new BaseResponseDto(ReturnMessage.EXCHANGE_CODE_IS_INVALID.getCode(),ReturnMessage.EXCHANGE_CODE_IS_INVALID.getMsg());
        }
        if (exchangeCodeService.checkExchangeCodeExpire(couponModel)) {
            return new BaseResponseDto(ReturnMessage.EXCHANGE_CODE_IS_EXPIRE.getCode(),ReturnMessage.EXCHANGE_CODE_IS_EXPIRE.getMsg());
        }
        if (exchangeCodeService.checkExchangeCodeUsed(couponId, exchangeCode)){
            return new BaseResponseDto(ReturnMessage.EXCHANGE_CODE_IS_USED.getCode(),ReturnMessage.EXCHANGE_CODE_IS_USED.getMsg());
        }
        if (exchangeCodeService.checkExchangeCodeDailyCount(loginName)) {
            return new BaseResponseDto(ReturnMessage.EXCHANGE_CODE_OVER_DAILY_COUNT.getCode(),ReturnMessage.EXCHANGE_CODE_OVER_DAILY_COUNT.getMsg());
        }
        couponActivationService.assignUserCoupon(loginName, Lists.newArrayList(UserGroup.EXCHANGER_CODE), couponId, exchangeCode);
        String isUsed = "1";
        redisWrapperClient.hset(EXCHANGE_CODE_KEY + couponId, exchangeCode, isUsed);

        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
        if(CollectionUtils.isEmpty(userCouponModels)){
            return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(),ReturnMessage.SUCCESS.getMsg());
        }
        UnmodifiableIterator<UserCouponModel> filter = Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {

            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                return exchangeCode.equals(userCouponModel.getExchangeCode());
            }
        });

        Iterator<BaseCouponResponseDataDto> items = Iterators.transform(filter, new Function<UserCouponModel, BaseCouponResponseDataDto>() {
            @Override
            public BaseCouponResponseDataDto apply(UserCouponModel userCouponModel) {
                BaseCouponResponseDataDto dataDto = new BaseCouponResponseDataDto(couponModel, userCouponModel);
                return dataDto;

            }
        });

        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(new UserCouponListResponseDataDto(Lists.newArrayList(items)));
        return responseDto;
    }
}
