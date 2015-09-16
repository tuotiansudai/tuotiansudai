package com.ttsd.special.controller;

import com.ttsd.special.dto.LotteryPrizeResponseDto;
import com.ttsd.special.model.InvestLotteryType;
import com.ttsd.special.services.InvestLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/invest-lottery")
public class InvestLotteryController {

    @Autowired
    private InvestLotteryService investLotteryService;

    @ResponseBody
    @RequestMapping("/novice")
    public LotteryPrizeResponseDto getLotteryPrizeNovice(){
        return investLotteryService.getLotteryPrize(InvestLotteryType.NOVICE);
    }

    @ResponseBody
    @RequestMapping("/normal")
    public LotteryPrizeResponseDto getLotteryPrizeNormal(){
        return investLotteryService.getLotteryPrize(InvestLotteryType.NORMAL);
    }



}
