package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.WechatLotteryDto;
import com.tuotiansudai.activity.service.WechatLotteryService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/wechat/lottery")
public class WechatLotteryController {

    @Autowired
    private WechatLotteryService wechatLotteryService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView wechatLotteryPage() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView mav = new ModelAndView("/wechat-lottery");
        mav.addObject("leftDrawCount", wechatLotteryService.getLeftDrawCount(loginName));
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public BaseDto<WechatLotteryDto> drawLottery() {
        String loginName = LoginUserInfo.getLoginName();
        return wechatLotteryService.drawLottery(loginName);
    }
}
