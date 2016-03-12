package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppPointExchangeServiceImpl;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by gengbeijun on 16/3/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppPointExchangeServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppPointExchangeServiceImpl mobileAppPointExchangeService;
    @Mock
    private CouponExchangeMapper couponExchangeMapper;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private CouponActivationService couponActivationService;
    @Mock
    private PointBillService pointBillService;

    @Test
    public void shouldGeneratePointExchangeIsOK(){
        BaseResponseDto dto = new BaseResponseDto();
        PointExchangeRequestDto pointExchangeRequestDto = new PointExchangeRequestDto();
        pointExchangeRequestDto.setCouponId("1000");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("logname");
        pointExchangeRequestDto.setBaseParam(baseParam);
        AccountModel accountModel = new AccountModel();
        when(accountMapper.lockByLoginName(anyString())).thenReturn(generateAccountModel());
        when(couponExchangeMapper.findByCouponId(anyLong())).thenReturn(generateCouponExchangeModel());
        PointExchangeResponseDataDto pointExchangeResponseDataDto = new PointExchangeResponseDataDto();
        BaseResponseDto<PointExchangeResponseDataDto> responseDto = mobileAppPointExchangeService.generatePointExchange(pointExchangeRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals(90, responseDto.getData().getPoint());

    }

    @Test
    public void shouldGeneratePointExchangeIsFail(){
        BaseResponseDto dto = new BaseResponseDto();
        PointExchangeRequestDto pointExchangeRequestDto = new PointExchangeRequestDto();
        pointExchangeRequestDto.setCouponId("1000");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("logname");
        pointExchangeRequestDto.setBaseParam(baseParam);
        AccountModel accountModel = new AccountModel();
        when(accountMapper.lockByLoginName(anyString())).thenReturn(generateAccountModel());
        when(couponExchangeMapper.findByCouponId(anyLong())).thenReturn(generateNextCouponExchangeModel());
        PointExchangeResponseDataDto pointExchangeResponseDataDto = new PointExchangeResponseDataDto();
        BaseResponseDto<PointExchangeResponseDataDto> responseDto = mobileAppPointExchangeService.generatePointExchange(pointExchangeRequestDto);

        assertEquals(ReturnMessage.POINT_EXCHANGE_FAIL.getCode(), responseDto.getCode());
        assertEquals(100, responseDto.getData().getPoint());
    }

    private AccountModel generateAccountModel() {
        AccountModel accountModel = new AccountModel();
        accountModel.setPoint(100);
        return accountModel;
    }

    private CouponExchangeModel generateCouponExchangeModel(){
        CouponExchangeModel couponExchangeModel = new CouponExchangeModel();
        couponExchangeModel.setExchangePoint(10);
        return couponExchangeModel;
    }

    private CouponExchangeModel generateNextCouponExchangeModel(){
        CouponExchangeModel couponExchangeModel = new CouponExchangeModel();
        couponExchangeModel.setExchangePoint(200);
        return couponExchangeModel;
    }

}
