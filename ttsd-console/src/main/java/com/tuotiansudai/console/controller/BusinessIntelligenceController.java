package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.Granularity;
import com.tuotiansudai.repository.model.KeyValueModel;
import com.tuotiansudai.service.BusinessIntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bi")
public class BusinessIntelligenceController {
    @Autowired
    private BusinessIntelligenceService businessIntelligenceService;

    @ResponseBody
    @RequestMapping(value = "/user-register-trend", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserRegisterTrend(
            @RequestParam(name = "granularity") Granularity granularity,
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        return businessIntelligenceService.queryUserRegisterTrend(granularity, startTime, endTime);
    }

    @ResponseBody
    @RequestMapping(value = "/user-distribution", method = RequestMethod.GET)
    public List<KeyValueModel> queryUserDistribution(
            @RequestParam(name = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        return businessIntelligenceService.queryUserDistribution(startTime, endTime);
    }
}
