package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppReferrerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppReferrerStatisticsController extends MobileAppBaseController{

    @Autowired
    private MobileAppReferrerStatisticsService mobileAppReferrerStatisticsService;

    @RequestMapping(value = "/get/referrer-statistics", method = RequestMethod.POST)
    public BaseResponseDto getReferrerStatistics(@RequestBody BaseParamDto paramDto){
        return mobileAppReferrerStatisticsService.getReferrerStatistics(paramDto);
    }
}
