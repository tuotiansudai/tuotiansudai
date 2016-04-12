package com.ttsd.special.controller;

import com.ttsd.special.dto.InvestTopResponse;
import com.ttsd.special.dto.InvestTopStatPeriod;
import com.ttsd.special.services.InvestmentTopService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/investmenttop")
public class InvestmentTopController {

    @Autowired
    private InvestmentTopService topService;

    @ResponseBody
    @RequestMapping("")
    public InvestTopResponse queryInvestTop(HttpServletRequest request){
        String periodString = request.getParameter("period");
        InvestTopStatPeriod period = InvestTopStatPeriod.Week;
        if(StringUtils.isNoneBlank(periodString)){
            period = InvestTopStatPeriod.fromValue(periodString);
        }
        InvestTopResponse resp = topService.queryInvestTopResponse(period);
        return resp;
    }
}
