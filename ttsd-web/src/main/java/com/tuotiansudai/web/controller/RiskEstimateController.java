package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RiskEstimateDto;
import com.tuotiansudai.enums.riskestimation.Estimate;
import com.tuotiansudai.service.RiskEstimateService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/risk-estimate")
public class RiskEstimateController {

    private final RiskEstimateService riskEstimateService;

    @Autowired
    public RiskEstimateController(RiskEstimateService riskEstimateService) {
        this.riskEstimateService = riskEstimateService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show() {
        return new ModelAndView("/risk-estimate");
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<RiskEstimateDto> estimate(@RequestBody List<Integer> answers) {
        Estimate estimate = riskEstimateService.estimate(LoginUserInfo.getLoginName(), answers);
        return new BaseDto<>(new RiskEstimateDto(estimate));
    }

    @RequestMapping(path = "/alert", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> estimate() {
        return new BaseDto<>(new BaseDataDto(riskEstimateService.alertEstimate(LoginUserInfo.getLoginName())));
    }
}
