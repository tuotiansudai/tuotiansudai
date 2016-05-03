package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppExchangeServiceImpl;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;


public class MobileAppExchangeServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppExchangeServiceImpl mobileAppAgreementService;
    @Mock
    private ExchangeCodeService exchangeCodeService;
    @Mock
    private CouponMapper couponMapper;
    @Mock
    private CouponActivationService couponActivationService;
    @Mock
    private RedisWrapperClient redisWrapperClient;
    @Mock
    private UserCouponMapper userCouponMapper;


    @Test
    public void shouldExchangeCodeIsInValid(){
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        exchangeRequestDto.setBaseParam(baseParam);
        exchangeRequestDto.setExchangeCode("127389719");
        when(exchangeCodeService.getValueBase31(anyString())).thenReturn(123L);
        when(couponMapper.findById(anyLong())).thenReturn(new CouponModel());


        BaseResponseDto baseResponseDto = mobileAppAgreementService.exchange(exchangeRequestDto);

        assertEquals(ReturnMessage.EXCHANGE_CODE_IS_INVALID.getCode(),baseResponseDto.getCode());
    }
    @Test
    public void shouldExchangeIsSuccess(){
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        exchangeRequestDto.setBaseParam(baseParam);
        exchangeRequestDto.setExchangeCode("127389719");
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(123456789);
        userCouponModel.setStartTime(new DateTime("2016-01-01").toDate());
        userCouponModel.setEndTime(new DateTime("2016-04-05").toDate());
        userCouponModel.setExchangeCode(exchangeRequestDto.getExchangeCode());

        CouponModel couponModel = new CouponModel();
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setRate(5);
        couponModel.setInvestLowerLimit(100);
        couponModel.setProductTypes(Lists.newArrayList(ProductType.JYF));

        when(exchangeCodeService.getValueBase31(anyString())).thenReturn(123L);
        when(exchangeCodeService.checkExchangeCodeCorrect(anyString(), anyLong(), any(CouponModel.class))).thenReturn(true);
        when(exchangeCodeService.checkExchangeCodeExpire(any(CouponModel.class))).thenReturn(false);
        when(exchangeCodeService.checkExchangeCodeUsed(anyLong(), anyString())).thenReturn(false);
        when(exchangeCodeService.checkExchangeCodeDailyCount(anyString())).thenReturn(false);
        doNothing().when(couponActivationService).assignUserCoupon(anyString(), any(List.class), anyLong(), anyString());
        doNothing().when(redisWrapperClient).hset(anyString(), anyString(), anyString());

        when(couponMapper.findById(anyLong())).thenReturn(couponModel);
        when(userCouponMapper.findByLoginName(anyString(),any(List.class))).thenReturn(Lists.newArrayList(userCouponModel));

        BaseResponseDto<UserCouponListResponseDataDto> baseResponseDto = mobileAppAgreementService.exchange(exchangeRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
        assertEquals(String.valueOf(userCouponModel.getId()),baseResponseDto.getData().getCoupons().get(0).getUserCouponId());
        assertEquals(userCouponModel.getStartTime(),baseResponseDto.getData().getCoupons().get(0).getStartDate());
        assertEquals(userCouponModel.getEndTime(),baseResponseDto.getData().getCoupons().get(0).getEndDate());
        assertEquals(couponModel.getCouponType().getName(),baseResponseDto.getData().getCoupons().get(0).getName());
        assertEquals(String.valueOf(500),baseResponseDto.getData().getCoupons().get(0).getRate());
        assertEquals(AmountConverter.convertCentToString(couponModel.getInvestLowerLimit()),baseResponseDto.getData().getCoupons().get(0).getInvestLowerLimit());
        assertEquals(couponModel.getProductTypes().get(0),baseResponseDto.getData().getCoupons().get(0).getProductTypes().get(0));
    }



}
