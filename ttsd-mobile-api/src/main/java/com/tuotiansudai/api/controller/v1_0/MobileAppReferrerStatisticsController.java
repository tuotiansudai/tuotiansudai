package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppReferrerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MobileAppReferrerStatisticsController extends MobileAppBaseController{

    @Autowired
    private MobileAppReferrerStatisticsService mobileAppReferrerStatisticsService;

    @RequestMapping(value = "/get/referrer-statistics", method = RequestMethod.POST)
    public BaseResponseDto getReferrerStatistics(@RequestBody BaseParamDto paramDto){
        paramDto.getBaseParam().setUserId(getLoginName());
        return mobileAppReferrerStatisticsService.getReferrerStatistics(paramDto);
    }
}
