package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPerceptionService;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppAgreementServiceImpl;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppMembershipPerceptionServiceImpl;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementBusinessType;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppMembershipPerceptionServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppMembershipPerceptionServiceImpl mobileAppMembershipPerceptionService;

    @Mock
    private UserMembershipMapper userMemberhshipMapper;

    @Mock
    private InvestService investService;

    @Mock
    private MembershipMapper membershipMapper;

    @Test
    public void shouldGetMembershipPerceptionIsOk() {
        MembershipPerceptionRequestDto membershipPerceptionRequestDto = new MembershipPerceptionRequestDto();
        membershipPerceptionRequestDto.setInvestAmount("100.00");
        membershipPerceptionRequestDto.setLoanId("10000000000");
        membershipPerceptionRequestDto.setUserCouponIds(Lists.newArrayList(100L));
        membershipPerceptionRequestDto.setBaseParam(BaseParamTest.getInstance());



        UserMembershipModel userMembershipModel = new UserMembershipModel(membershipPerceptionRequestDto.getBaseParam().getUserId(), 5, new DateTime().plusDays(10).toDate(), UserMembershipType.GIVEN);

        MembershipModel membershipModel = new MembershipModel(5, 4, 500000, 0.07);


        when(userMemberhshipMapper.findCurrentMaxByLoginName(anyString())).thenReturn(userMembershipModel);
        when(membershipMapper.findById(anyLong())).thenReturn(membershipModel);
        when(investService.calculateMembershipPreference(anyString(), anyLong(), anyLong())).thenReturn(80000L);

        BaseResponseDto<MembershipPerceptionResponseDataDto> responseDto = mobileAppMembershipPerceptionService.getMembershipPerception(membershipPerceptionRequestDto);
        
        assertEquals("0000", responseDto.getCode());
        assertEquals("V4会员,专享服务费7折优惠,已经多赚800.00元", responseDto.getData().getTip());
    }

}
