package com.ttsd.special.controller;

import com.ttsd.special.dto.InvestTopList;
import com.ttsd.special.dto.InvestTopStatPeriod;
import com.ttsd.special.services.InvestmentTopService;
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
    public InvestTopList queryInvestTop(HttpServletRequest request){
        String periodString = request.getParameter("period");
        InvestTopStatPeriod period = InvestTopStatPeriod.fromValue(periodString);

        InvestTopList investTopList = topService.queryInvestTopListCache(period);

        return investTopList;
    }
}
