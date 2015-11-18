package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.api.service.impl.MobileAppReferrerInvestServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.service.ReferrerManageService;
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

public class MobileAppReferrerInvestServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppReferrerInvestServiceImpl mobileAppReferrerInvestService;
    @Mock
    private ReferrerManageMapper referrerManageMapper;

    @Mock
    private ReferrerManageServiceImpl referrerManageService;

    @Test
    public void shouldGenerateReferrerInvestListIsOk() {
        List<ReferrerManageView> referrerManageViews = fakeReferrerManageView(2);
        ReflectionTestUtils.setField(referrerManageService,"userReward","0.1|0.2|0.3");
        ReflectionTestUtils.setField(referrerManageService,"staffReward","0.1|0.2|0.3|0.4");
        when(referrerManageMapper.findReferInvestList(anyString(), anyString(), any(Date.class), any(Date.class), anyString(), anyInt(), anyInt())).thenReturn(referrerManageViews);
        when(referrerManageMapper.findReferInvestCount(anyString(), anyString(), any(Date.class), any(Date.class),anyString())).thenReturn(referrerManageViews.size());
        ReferrerInvestListRequestDto referrerInvestListRequestDto = new ReferrerInvestListRequestDto();
        referrerInvestListRequestDto.setReferrerId("loginName");
        referrerInvestListRequestDto.setPageSize(10);
        referrerInvestListRequestDto.setIndex(1);
        BaseResponseDto<ReferrerInvestListResponseDataDto> baseResponseDto = mobileAppReferrerInvestService.generateReferrerInvestList(referrerInvestListRequestDto);
        assertTrue(baseResponseDto.isSuccess());
    }

    private List<ReferrerManageView> fakeReferrerManageView(int count) {
        List<ReferrerManageView> referrerManageViews = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            ReferrerManageView referrerManageView = new ReferrerManageView();
            referrerManageView.setLevel(1);
            referrerManageView.setInvestAmount(1000l);
            referrerManageView.setInvestLoginName("loginName" + i);
            referrerManageView.setInvestName("用户" + i);
            referrerManageView.setInvestTime(new Date());
            referrerManageView.setLoanName("标的" + i);
            referrerManageView.setPeriods(i);
            referrerManageView.setReferrerLoginName("admin" + i);
            referrerManageView.setReferrerName("推荐人" + i);
            referrerManageView.setInvestAmount(2000l);
            referrerManageView.setRewardAmount(5000l);
            referrerManageView.setRewardTime(new Date());
            referrerManageViews.add(referrerManageView);

        }
        return referrerManageViews;
    }


}
