package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.dto.IPhone7InvestLotteryDto;
import com.tuotiansudai.activity.service.Iphone7LotteryService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/iphone7-lottery")
public class Iphone7ActivityController {

    @Autowired
    private Iphone7LotteryService iphone7LotteryService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = "gengbeijun";LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/iphone7-lottery", "responsive", true);
        modelAndView.addObject("nextLotteryInvestAmount",iphone7LotteryService.nextLotteryInvestAmount());
        modelAndView.addObject("lotteryList", iphone7LotteryService.iphone7InvestLotteryWinnerViewList());
        return modelAndView;
    }


    @RequestMapping(value="/myInvestDetail", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> myInvestLotteryNumber(@RequestParam(name = "loginName") String loginName,
                                                                @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                                @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {

        BasePaginationDataDto<IPhone7InvestLotteryDto> dataDto = iphone7LotteryService.myInvestLotteryList(loginName, index, pageSize);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        return dto;
    }



}
