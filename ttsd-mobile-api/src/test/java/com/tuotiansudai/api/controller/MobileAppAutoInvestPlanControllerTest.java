package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppAutoInvestPlanService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppAutoInvestPlanControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppAutoInvestPlanController controller;

    @Mock
    private MobileAppAutoInvestPlanService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldBuildAutoInvestPlanIsSuccess() throws Exception {
        AutoInvestPlanDataDto autoInvestPlanDataDto = new AutoInvestPlanDataDto();
        List<AutoInvestPeriodDto> autoInvestPeriodDtos = Lists.newArrayList();
        AutoInvestPeriodDto autoInvestPeriodDto = new AutoInvestPeriodDto();
        autoInvestPeriodDto.setSelected(true);
        autoInvestPeriodDto.setPid("123");
        autoInvestPeriodDto.setpName("admin");
        autoInvestPeriodDtos.add(autoInvestPeriodDto);

        autoInvestPeriodDtos.add(autoInvestPeriodDto);
        autoInvestPlanDataDto.setEnabled(true);
        autoInvestPlanDataDto.setLoginName("admin");
        autoInvestPlanDataDto.setMaxInvestAmount("100000");
        autoInvestPlanDataDto.setMinInvestAmount("5000");
        autoInvestPlanDataDto.setAutoInvestPeriods(autoInvestPeriodDtos);

        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(autoInvestPlanDataDto);
        baseDto.setData(autoInvestPlanDataDto);

        when(service.buildAutoInvestPlan(any(AutoInvestPlanRequestDto.class))).thenReturn(baseDto);
        doRequestWithServiceMockedTest("/auto-invest-plan",
                new AutoInvestPlanRequestDto())
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.autoInvestPeriods[0].pid").value(autoInvestPeriodDto.getPid()));

    }



}
