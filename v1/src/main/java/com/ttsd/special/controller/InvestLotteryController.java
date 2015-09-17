package com.ttsd.special.controller;

import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.InvestLotteryType;
import com.ttsd.special.services.InvestLotteryService;
import com.ttsd.util.SpringSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/invest-lottery")
public class InvestLotteryController {

    @Autowired
    private InvestLotteryService investLotteryService;

    @RequestMapping("")
    public ModelAndView investLotteryPage() {
        List<InvestLottery> investLotteryList = investLotteryService.findInvestLotteryTops(50);
        String username = SpringSecurityUtils.getLoginUserId();
        int normalRemainCount = 0;
        int noviceRemainCount = 0;
        if(!SpringSecurityUtils.isAnonymousUser(username)) {
            normalRemainCount = investLotteryService.getRemainingTimes(username, InvestLotteryType.NORMAL);
            noviceRemainCount = investLotteryService.getRemainingTimes(username, InvestLotteryType.NOVICE);
        }
        ModelAndView mv = new ModelAndView("/invest-lottery");
        mv.addObject("investLotterys", investLotteryList);
        mv.addObject("normalRemainCount", normalRemainCount);
        mv.addObject("noviceRemainCount", noviceRemainCount);
        mv.addObject("currentLoginName", username);
        return mv;
    }
}
