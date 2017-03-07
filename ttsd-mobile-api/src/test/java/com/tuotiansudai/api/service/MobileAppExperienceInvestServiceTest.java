package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppExperienceInvestServiceImpl;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppExperienceInvestServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppExperienceInvestServiceImpl mobileAppExperienceInvestService;

    @Mock
    private ExperienceInvestService experienceInvestService;

    @Mock
    private MobileAppChannelService mobileAppChannelService;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldExperienceInvestSuccess() {
        long loanId = idGenerator.generate();
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setLoanId(String.valueOf(loanId));
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("investor");
        baseParam.setPlatform("IOS");
        investRequestDto.setBaseParam(baseParam);
        investRequestDto.setInvestMoney("10000.00");

        when(mobileAppChannelService.obtainChannelBySource(any(BaseParam.class))).thenReturn("tuotiansudai");

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);

        when(experienceInvestService.invest(any(InvestDto.class))).thenReturn(baseDto);

        BaseResponseDto<InvestExperienceResponseDto> baseResponseDto = mobileAppExperienceInvestService.experienceInvest(investRequestDto);

        assertThat(baseResponseDto.getMessage(), is(ReturnMessage.SUCCESS.getMsg()));
    }

}
