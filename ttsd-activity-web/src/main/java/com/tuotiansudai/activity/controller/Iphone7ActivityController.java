package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.IPhone7InvestLotteryDto;
import com.tuotiansudai.activity.service.Iphone7LotteryService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


public class Iphone7ActivityController {

    @Autowired
    private Iphone7LotteryService iphone7LotteryService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/iphone7-lottery", "responsive", true);
        modelAndView.addObject("nextLotteryInvestAmount",iphone7LotteryService.nextLotteryInvestAmount());
        modelAndView.addObject("lotteryList", iphone7LotteryService.iphone7InvestLotteryWinnerViewList());
        modelAndView.addObject("loginName", loginName);
        return modelAndView;
    }

    @RequestMapping(value="/myInvestDetail", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> myInvestLotteryNumber(@RequestParam(name = "loginName") String loginName,
                                                                @RequestParam(name = "index", defaultValue = "1", required = false) int index) {

        BasePaginationDataDto<IPhone7InvestLotteryDto> dataDto = iphone7LotteryService.myInvestLotteryList(loginName, index, 10);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        return dto;
    }

    @RequestMapping(value = "/getDate")
    @ResponseBody
    public BaseDataDto isExpireDate(){
        BaseDataDto dto = new BaseDataDto();
        dto.setStatus(iphone7LotteryService.isNotExpiryDate());
        return dto;
    }
}
