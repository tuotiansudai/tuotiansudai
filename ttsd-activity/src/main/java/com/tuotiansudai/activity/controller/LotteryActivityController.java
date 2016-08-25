package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.dto.UserLotteryDto;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.LotteryActivityService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/activity")
public class LotteryActivityController {

    @Autowired
    private LotteryActivityService lotteryActivityService;

    @ResponseBody
    @RequestMapping(value = "/draw-lottery", method = RequestMethod.POST)
    public DrawLotteryResultDto drawLotteryPrize() {
        return lotteryActivityService.drawLotteryPrize(LoginUserInfo.getMobile(),"");
    }

    @ResponseBody
    @RequestMapping(value = "/lottery-record-list", method = RequestMethod.POST)
    public List<UserLotteryPrizeView> getLotteryRecord() {
        return lotteryActivityService.findDrawLotteryPrizeRecordByMobile(LoginUserInfo.getMobile());
    }

    @ResponseBody
    @RequestMapping(value = "/lottery-all-record", method = RequestMethod.POST)
    public List<UserLotteryPrizeView> getAllDrawLotteryPrizeRecord() {
        return lotteryActivityService.findDrawLotteryPrizeRecordByAll();
    }

    @RequestMapping(value = "/user-lottery", method = RequestMethod.POST)
    public UserLotteryDto getUserLotteryInfo() {
        return lotteryActivityService.findUserLotteryByLoginName(LoginUserInfo.getMobile());
    }

}
