package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.paywrapper.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/job")
public class JobController {

    static Logger logger = Logger.getLogger(JobController.class);

    @Autowired
    private InvestService investService;

    @ResponseBody
    @RequestMapping(value = "/async_invest_notify", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> asyncInvestNotify(HttpServletRequest request) {
        return this.investService.asyncInvestCallback();
    }

}