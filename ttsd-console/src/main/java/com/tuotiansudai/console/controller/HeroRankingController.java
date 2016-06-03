package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
public class HeroRankingController {

    @Autowired
    private InvestMapper investMapper;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView heroRanking(Date tradingTime) {
        List<HeroRankingView> heroRankingViewList = investMapper.findHeroRankingByReferrer(tradingTime);
    }
}
