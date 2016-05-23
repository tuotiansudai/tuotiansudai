package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppUserCouponService;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppUserCouponControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppUserCouponController controller;

    @Mock
    private MobileAppUserCouponService mobileAppUserCouponService;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetUserCoupons() throws Exception {
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        UserCouponResponseDataDto item = new UserCouponResponseDataDto();
        item.setUserCouponId("1");
        item.setType(CouponType.NEWBIE_COUPON);
        item.setName(CouponType.NEWBIE_COUPON.getAbbr());
        item.setAmount("1.00");
        item.setStartDate(new Date());
        item.setEndDate(new Date());
        item.setInvestLowerLimit("1.00");
        item.setProductTypes(Lists.newArrayList(ProductType._30.getProductLine(), ProductType._90.getProductLine(), ProductType._180.getProductLine()));
        item.setUsedTime(new Date());
        item.setLoanId("loanId");
        item.setLoanName("loanName");
        item.setLoanProductType(ProductType._30.getProductLine());
        item.setExpectedInterest("1.00");

        UserCouponListResponseDataDto dataDto = new UserCouponListResponseDataDto(Lists.newArrayList((BaseCouponResponseDataDto)item));
        responseDto.setData(dataDto);

        when(mobileAppUserCouponService.getUserCoupons(any(UserCouponRequestDto.class))).thenReturn(responseDto);
        doRequestWithServiceMockedTest("/get/userCoupons", new UserCouponRequestDto())
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.coupons[0].userCouponId").value(item.getUserCouponId()))
                .andExpect(jsonPath("$.data.coupons[0].type").value(item.getType().name()))
                .andExpect(jsonPath("$.data.coupons[0].name").value(item.getName()))
                .andExpect(jsonPath("$.data.coupons[0].amount").value(item.getAmount()))
                .andExpect(jsonPath("$.data.coupons[0].startDate").value(new DateTime(item.getStartDate()).toString("yyyy-MM-dd")))
                .andExpect(jsonPath("$.data.coupons[0].endDate").value(new DateTime(item.getEndDate()).toString("yyyy-MM-dd")))
                .andExpect(jsonPath("$.data.coupons[0].investLowerLimit").value(item.getInvestLowerLimit()))
                .andExpect(jsonPath("$.data.coupons[0].productTypes[0]").value("SYL"))
                .andExpect(jsonPath("$.data.coupons[0].productTypes[1]").value("WYX"))
                .andExpect(jsonPath("$.data.coupons[0].productTypes[2]").value("JYF"))
                .andExpect(jsonPath("$.data.coupons[0].loanId").value(item.getLoanId()))
                .andExpect(jsonPath("$.data.coupons[0].loanName").value(item.getLoanName()))
                .andExpect(jsonPath("$.data.coupons[0].loanProductType").value(item.getLoanProductType()))
                .andExpect(jsonPath("$.data.coupons[0].expectedInterest").value(item.getExpectedInterest()))
                .andExpect(jsonPath("$.data.coupons[0].usedTime").value(new DateTime(item.getUsedTime()).toString("yyyy-MM-dd")))
        ;
    }
}
