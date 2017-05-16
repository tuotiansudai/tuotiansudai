package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.WechatLotteryDto;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.WechatLotteryService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity/wechat/lottery")
public class WechatLotteryController {

    @Autowired
    private WechatLotteryService wechatLotteryService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView wechatLotteryPage() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView mav = new ModelAndView("/activities/wx-lottery");
        mav.addObject("leftDrawCount", wechatLotteryService.getLeftDrawCount(loginName));
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public BaseDto<WechatLotteryDto> drawLottery() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<WechatLotteryDto> baseDto = wechatLotteryService.drawLottery(loginName);
        baseDto.getData().setLeftDrawCount(wechatLotteryService.getLeftDrawCount(loginName));
        return baseDto;
    }

    @ResponseBody
    @RequestMapping(value = "/getLotteryList", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getLotteryListTop20() {
        return wechatLotteryService.getLotteryListTop20();
    }

    @ResponseBody
    @RequestMapping(value = "/getMyLotteryList", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getMyLotteryList() {
        String mobile = LoginUserInfo.getMobile();
        return wechatLotteryService.getMyLotteryList(mobile);
    }

}
