package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppReferrerStatisticsService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

public class MobileAppReferrerStatisticsControllerTest extends ControllerTestBase {



    @InjectMocks
    private MobileAppReferrerStatisticsController controller;

    @Autowired
    private MobileAppReferrerStatisticsService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }



    @Test
    public void getReferrerStatistics() throws Exception {
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("");
        baseParamDto.setBaseParam(baseParam);

        BaseResponseDto baseResponseDto = service.getReferrerStatistics(baseParamDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
    }

}
