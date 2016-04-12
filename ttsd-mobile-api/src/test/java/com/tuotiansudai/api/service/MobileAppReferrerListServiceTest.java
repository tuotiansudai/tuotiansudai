package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.api.service.impl.MobileAppReferrerListServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.service.impl.ReferrerManageServiceImpl;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppReferrerListServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppReferrerListServiceImpl mobileAppReferrerListService;
    @Mock
    private ReferrerManageMapper referrerManageMapper;

    @Mock
    private ReferrerManageServiceImpl referrerManageService;

    @Test
    public void shouldGenerateReferrerListIsOk(){
        ReflectionTestUtils.setField(referrerManageService, "userReward", "0.1|0.2|0.3");
        ReflectionTestUtils.setField(referrerManageService,"staffReward","0.1|0.2|0.3|0.4");
        ReferrerRelationView referrerRelationView1 = new ReferrerRelationView();
        referrerRelationView1.setLoginName("loginName1");
        referrerRelationView1.setReferrerLoginName("referrerLoginName1");
        referrerRelationView1.setLevel(2);
        referrerRelationView1.setRegisterTime(new Date());
        ReferrerRelationView referrerRelationView2 = new ReferrerRelationView();
        referrerRelationView2.setLoginName("loginName1");
        referrerRelationView2.setReferrerLoginName("referrerLoginName2");
        referrerRelationView2.setLevel(4);
        referrerRelationView2.setRegisterTime(new Date());

        List<ReferrerRelationView> referrerRelationDtos = Lists.newArrayList();
        referrerRelationDtos.add(referrerRelationView1);
        referrerRelationDtos.add(referrerRelationView2);
        when(referrerManageMapper.findReferRelationList(anyString(), anyString(), any(Date.class), any(Date.class),anyString(),anyInt(), anyInt())).thenReturn(referrerRelationDtos);
        when(referrerManageMapper.findReferRelationCount(anyString(), anyString(), any(Date.class), any(Date.class),anyString())).thenReturn(2);
        ReferrerListRequestDto referrerListRequestDto = new ReferrerListRequestDto();
        referrerListRequestDto.setIndex(1);
        referrerListRequestDto.setPageSize(10);
        referrerListRequestDto.setBaseParam(BaseParamTest.getInstance());
        BaseResponseDto<ReferrerListResponseDataDto> baseResponseDto = mobileAppReferrerListService.generateReferrerList(referrerListRequestDto);
        assertTrue(baseResponseDto.isSuccess());
        assertEquals("2", baseResponseDto.getData().getReferrerList().get(0).getLevel());
        assertEquals("4",baseResponseDto.getData().getReferrerList().get(1).getLevel());


    }


}
