package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.v1_0.MobileAppAutoInvestPlanInfoController;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanInfoService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppAutoInvestPlanInfoControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppAutoInvestPlanInfoController controller;

    @Mock
    private MobileAppAutoInvestPlanInfoService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetAutoInvestPlanInfoDataIsSuccess() throws Exception {
        AutoInvestPlanInfoResponseDataDto dataDto = new AutoInvestPlanInfoResponseDataDto();
        dataDto.setAutoInvest(true);

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
        dataDto.setAutoInvestPlan(autoInvestPlanDataDto);

        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(autoInvestPlanDataDto);
        baseDto.setData(dataDto);

        when(service.getAutoInvestPlanInfoData(any(BaseParamDto.class))).thenReturn(baseDto);
        doRequestWithServiceMockedTest("/get/auto-invest-plan",
                new BaseParamDto())
                .andExpect(jsonPath("$.data.autoInvest").value(true))
                .andExpect(jsonPath("$.data.autoInvestPlan.autoInvestPeriods[0].pid").value(autoInvestPeriodDto.getPid()));
    }


}
