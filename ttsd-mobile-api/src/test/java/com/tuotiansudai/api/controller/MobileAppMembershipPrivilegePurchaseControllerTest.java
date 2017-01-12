package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.controller.v1_0.MobileAppAgreementController;
import com.tuotiansudai.api.controller.v1_0.MobileAppMembershipPrivilegePurchaseController;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppAgreementService;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPrivilegePurchaseService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppMembershipPrivilegePurchaseControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppMembershipPrivilegePurchaseController controller;

    @Mock
    private MobileAppMembershipPrivilegePurchaseService mobileAppMembershipPrivilegePurchaseService;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldObtainMembershipPrivilegePricesIsOk() throws Exception{
        MembershipPrivilegePriceResponseDataDto membershipPrivilegePriceResponseDataDto = new MembershipPrivilegePriceResponseDataDto();
        membershipPrivilegePriceResponseDataDto.setDuration(MembershipPrivilegePriceType._30.getDuration());
        membershipPrivilegePriceResponseDataDto.setName(MembershipPrivilegePriceType._30.getName());
        membershipPrivilegePriceResponseDataDto.setPrice(MembershipPrivilegePriceType._30.getPrice());
        List<MembershipPrivilegePriceResponseDataDto> prices = Lists.newArrayList(membershipPrivilegePriceResponseDataDto);
        MembershipPrivilegePriceResponseDto membershipPrivilegePriceResponseDto = new MembershipPrivilegePriceResponseDto(prices);
        when(mobileAppMembershipPrivilegePurchaseService.obtainMembershipPrivilegePrices()).thenReturn(membershipPrivilegePriceResponseDto);
        doRequestWithServiceMockedTest("/get/membership-privilege-price", new BaseParamDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

    @Test
    public void shouldPurchaseIsSuccess() throws Exception {
        BaseResponseDto<MembershipPrivilegePurchaseResponseDataDto> baseResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        MembershipPrivilegePurchaseResponseDataDto membershipPrivilegePurchaseResponseDataDto = new MembershipPrivilegePurchaseResponseDataDto();
        membershipPrivilegePurchaseResponseDataDto.setUrl("url");
        membershipPrivilegePurchaseResponseDataDto.setRequestData("requestData");
        baseResponseDto.setData(membershipPrivilegePurchaseResponseDataDto);
        when(mobileAppMembershipPrivilegePurchaseService.purchase(any(MembershipPrivilegePurchaseRequestDto.class))).thenReturn(baseResponseDto);
        doRequestWithServiceMockedTest("/get/membership-privilege-purchase", new MembershipPrivilegePurchaseRequestDto());
        assertEquals(ReturnMessage.SUCCESS.getCode(), successResponseDto.getCode());
    }

}
