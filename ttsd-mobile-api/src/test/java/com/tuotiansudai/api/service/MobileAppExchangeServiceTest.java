package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppExchangeServiceImpl;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


public class MobileAppExchangeServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppExchangeServiceImpl mobileAppAgreementService;
    @Mock
    private ExchangeCodeService exchangeCodeService;
    @Mock
    private CouponMapper couponMapper;
    @Mock
    private CouponAssignmentService couponAssignmentService;
    @Mock
    private UserCouponMapper userCouponMapper;
    @Mock
    private RedisWrapperClient redisWrapperClient;


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

        CouponModel couponModel = new CouponModel();
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setRate(5);
        couponModel.setInvestLowerLimit(100);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30));

        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(123456789);
        userCouponModel.setStartTime(new DateTime("2016-01-01").toDate());
        userCouponModel.setEndTime(new DateTime("2016-04-05").toDate());
        userCouponModel.setExchangeCode(exchangeRequestDto.getExchangeCode());
        userCouponModel.setCoupon(couponModel);

        when(exchangeCodeService.getValueBase31(anyString())).thenReturn(123L);
        when(exchangeCodeService.checkExchangeCodeCorrect(anyString(), anyLong(), any(CouponModel.class))).thenReturn(true);
        when(exchangeCodeService.checkExchangeCodeExpire(any(CouponModel.class))).thenReturn(false);
        when(exchangeCodeService.checkExchangeCodeUsed(anyLong(), anyString())).thenReturn(false);
        when(exchangeCodeService.checkExchangeCodeDailyCount(anyString())).thenReturn(false);
        when(couponAssignmentService.assignUserCoupon(anyString(),anyString())).thenReturn(userCouponModel);
        when(redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1l);

        when(couponMapper.findById(anyLong())).thenReturn(couponModel);
        when(userCouponMapper.findUserCouponWithCouponByLoginName(anyString(), any(List.class))).thenReturn(Lists.newArrayList(userCouponModel));

        BaseResponseDto<UserCouponListResponseDataDto> baseResponseDto = mobileAppAgreementService.exchange(exchangeRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
        assertEquals(String.valueOf(userCouponModel.getId()),baseResponseDto.getData().getCoupons().get(0).getUserCouponId());
        assertEquals(userCouponModel.getStartTime(),baseResponseDto.getData().getCoupons().get(0).getStartDate());
        assertEquals(userCouponModel.getEndTime(),baseResponseDto.getData().getCoupons().get(0).getEndDate());
        assertEquals(couponModel.getCouponType().getName(),baseResponseDto.getData().getCoupons().get(0).getName());
        assertEquals(String.valueOf(500),baseResponseDto.getData().getCoupons().get(0).getRate());
        assertEquals(AmountConverter.convertCentToString(couponModel.getInvestLowerLimit()),baseResponseDto.getData().getCoupons().get(0).getInvestLowerLimit());
        assertEquals(baseResponseDto.getData().getCoupons().get(0).getProductTypes().get(0), ProductType._30.getProductLine());
    }
}
