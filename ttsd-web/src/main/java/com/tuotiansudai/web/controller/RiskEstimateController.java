package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.enums.riskestimation.Estimate;
import com.tuotiansudai.service.RiskEstimateService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@RequestMapping(path = "/risk-estimate")
public class RiskEstimateController {

    private final RiskEstimateService riskEstimateService;

    @Autowired
    public RiskEstimateController(RiskEstimateService riskEstimateService) {
        this.riskEstimateService = riskEstimateService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(@RequestParam(value = "retry", required = false) Boolean retry) {
        Estimate estimate = riskEstimateService.getEstimate(LoginUserInfo.getLoginName());
        return estimate == null || (retry != null && retry) ? new ModelAndView("/risk-estimate") : new ModelAndView("/risk-estimate-result", "estimate", estimate);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> estimate(@RequestParam(value = "answers[]") Integer[] answers) {
        Estimate estimate = riskEstimateService.estimate(LoginUserInfo.getLoginName(), Arrays.asList(answers));
        return new BaseDto<>(new BaseDataDto(estimate != null));
    }

    @RequestMapping(path = "/alert", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> estimate() {
        return new BaseDto<>(new BaseDataDto(riskEstimateService.alertEstimate(LoginUserInfo.getLoginName())));
    }
}
