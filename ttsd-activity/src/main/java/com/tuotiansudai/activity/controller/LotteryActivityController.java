package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.dto.DrawLotteryActivityDto;
import com.tuotiansudai.activity.service.LotteryActivityService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/activity")
public class LotteryActivityController {

    @Autowired
    private LotteryActivityService lotteryActivityService;

    @ResponseBody
    @RequestMapping(value = "/draw-lottery", method = RequestMethod.POST)
    public BaseDto<DrawLotteryActivityDto> drawLotteryPrize() {
        String loginName = LoginUserInfo.getLoginName();
        return lotteryActivityService.drawLotteryPrize(loginName,"");
    }
}
