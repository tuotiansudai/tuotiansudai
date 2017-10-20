package com.tuotiansudai.api.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppMembershipPrivilegePurchaseServiceImpl;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.exception.MembershipPrivilegeIsPurchasedException;
import com.tuotiansudai.membership.exception.NotEnoughAmountException;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.model.Source;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppMembershipPrivilegePurchaseServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppMembershipPrivilegePurchaseServiceImpl mobileAppMembershipPrivilegePurchaseService;

    @Mock
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Mock
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldObtainMembershipPrivilegePricesIsSuccess() {

        MembershipPrivilegePriceResponseDto responseDto = mobileAppMembershipPrivilegePurchaseService.obtainMembershipPrivilegePrices();

        assertNotNull(responseDto);
        assertThat(responseDto.getPrices().get(0).getName(), is(MembershipPrivilegePriceType._30.getName()));
        assertThat(responseDto.getPrices().get(0).getDuration(), is(MembershipPrivilegePriceType._30.getDuration()));
        assertThat(responseDto.getPrices().get(0).getPrice(), is(MembershipPrivilegePriceType._30.getPrice()));

        assertThat(responseDto.getPrices().get(1).getName(), is(MembershipPrivilegePriceType._180.getName()));
        assertThat(responseDto.getPrices().get(1).getDuration(), is(MembershipPrivilegePriceType._180.getDuration()));
        assertThat(responseDto.getPrices().get(1).getPrice(), is(MembershipPrivilegePriceType._180.getPrice()));

        assertThat(responseDto.getPrices().get(2).getName(), is(MembershipPrivilegePriceType._360.getName()));
        assertThat(responseDto.getPrices().get(2).getDuration(), is(MembershipPrivilegePriceType._360.getDuration()));
        assertThat(responseDto.getPrices().get(2).getPrice(), is(MembershipPrivilegePriceType._360.getPrice()));
    }

    @Test
    public void shouldPurchaseIsSuccess() throws MembershipPrivilegeIsPurchasedException, NotEnoughAmountException, JsonProcessingException, UnsupportedEncodingException {
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        Map<String, String> fields = Maps.newHashMap();
        fields.put("key", "value");
        System.out.println(objectMapper.writeValueAsString(fields));
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        payFormDataDto.setUrl("url");
        payFormDataDto.setMessage("message");
        payFormDataDto.setFields(fields);
        baseDto.setData(payFormDataDto);

        MembershipPrivilegePurchaseRequestDto requestDto = new MembershipPrivilegePurchaseRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("tester");
        baseParam.setPlatform("ios");
        requestDto.setDuration(30);
        requestDto.setBaseParam(baseParam);
        when(membershipPrivilegePurchaseService.purchase(anyString(), any(MembershipPrivilegePriceType.class), any(Source.class))).thenReturn(baseDto);


        BaseResponseDto<MembershipPrivilegePurchaseResponseDataDto> baseResponseDto = mobileAppMembershipPrivilegePurchaseService.purchase(requestDto);
        assertThat(baseResponseDto.getData().getUrl(), is(baseDto.getData().getUrl()));
        assertThat(baseResponseDto.getData().getRequestData(), is(CommonUtils.mapToFormData(baseDto.getData().getFields())));
    }


}
