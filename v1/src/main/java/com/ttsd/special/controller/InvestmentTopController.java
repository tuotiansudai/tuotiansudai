package com.ttsd.special.controller;

import com.ttsd.special.dto.InvestTopItem;
import com.ttsd.special.dto.InvestTopResponse;
import com.ttsd.special.dto.InvestTopStatPeriod;
import com.ttsd.special.services.InvestmentTopService;
import com.ttsd.util.ChinaArea;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        mockData(resp);
        return resp;
    }

    private void mockData(InvestTopResponse resp){
        for(ChinaArea area : resp.getAreaInvestments().keySet()){
            List<InvestTopItem> list = resp.getAreaInvestments().get(area);
            for(int i=list.size();i<100;i++){
                InvestTopItem mockItem = new InvestTopItem();
                mockItem.setPhoneNumber("1"+ RandomStringUtils.randomNumeric(10));
                mockItem.setUserId(RandomStringUtils.randomAlphabetic(7));
                mockItem.setCorpus(String.format("%.2f",(100-i)*1000+Math.random()*1000));
                mockItem.setInterest(String.format("%.2f",Math.random()*1000));
                list.add(mockItem);
            }
        }
    }
}
