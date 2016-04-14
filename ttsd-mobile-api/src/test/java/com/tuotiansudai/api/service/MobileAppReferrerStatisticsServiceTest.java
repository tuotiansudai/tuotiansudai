package com.tuotiansudai.api.service;

import com.tuotiansudai.api.controller.ControllerTestBase;
import com.tuotiansudai.api.controller.MobileAppReferrerStatisticsController;
import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;


public class MobileAppReferrerStatisticsServiceTest  extends ControllerTestBase {



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
        baseParam.setUserId("baoxin");
        baseParamDto.setBaseParam(baseParam);

        BaseResponseDto baseResponseDto = service.getReferrerStatistics(baseParamDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
    }

}
