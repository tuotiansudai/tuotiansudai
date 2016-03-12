package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointExchangeListRequestDto;
import com.tuotiansudai.api.dto.PointExchangeListResponseDataDto;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.api.service.impl.MobileAppPointExchangeListServiceImpl;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by gengbeijun on 16/3/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppPointExchangeListServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppPointExchangeListServiceImpl mobileAppPointExchangeListService;
    @Mock
    private CouponMapper couponMapper;
    @Mock
    private CouponExchangeMapper couponExchangeMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGeneratePointExchangeListIsOK(){

        when(couponMapper.findCouponExchangeableCount()).thenReturn(5);
        when(couponMapper.findCouponExchangeableList(anyInt(), anyInt())).thenReturn(generateMockedCouponModel());

        PointExchangeListRequestDto requestDto = new PointExchangeListRequestDto();
        requestDto.setPageSize(10);
        requestDto.setIndex(1);
        requestDto.setBaseParam(BaseParamTest.getInstance());
        BaseResponseDto<PointExchangeListResponseDataDto> responseDto = mobileAppPointExchangeListService.generatePointExchangeList(requestDto);

        assertEquals(5, responseDto.getData().getTotalCount().intValue());
        assertEquals(10, responseDto.getData().getPointExchange().size());

    }

    private List<CouponModel> generateMockedCouponModel() {
        List<CouponModel> CouponModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CouponModel couponModel = new CouponModel();
            couponModel.setId(idGenerator.generate());
            couponModel.setAmount(100L);
            couponModel.setActive(false);
            CouponModels.add(couponModel);
        }
        return CouponModels;
    }
}
