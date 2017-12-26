package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.DoubleElevenService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity/double-eleven")
public class DoubleElevenActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private DoubleElevenService doubleElevenService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doubleEleven() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/double-eleven", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        String sumAmount = loginName == null ? "0" : doubleElevenService.sumInvestAmountByLoginName(loginName);
        modelAndView.addObject("sumAmount", sumAmount);
        modelAndView.addObject("jdAmount", doubleElevenService.calculateJDCardAmountByInvestAmount(sumAmount));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/left-times", method = RequestMethod.GET)
    public int leftDrawTimes() {
        return 0;
    }

    @ResponseBody
    @RequestMapping(value = "/task-draw", method = RequestMethod.POST)
    public DrawLotteryResultDto drawPrize(@RequestParam(value = "activityCategory", defaultValue = "DOUBLE_ELEVEN_ACTIVITY", required = false) ActivityCategory activityCategory) {
        return new DrawLotteryResultDto(3);
    }

    @ResponseBody
    @RequestMapping(value = "/user-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByLoginName(@RequestParam(value = "activityCategory", defaultValue = "DOUBLE_ELEVEN_ACTIVITY", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecordByMobile(LoginUserInfo.getMobile(), activityCategory);
    }

    @ResponseBody
    @RequestMapping(value = "/all-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByAll(@RequestParam(value = "activityCategory", defaultValue = "DOUBLE_ELEVEN_ACTIVITY", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecord(null, activityCategory);
    }
}
