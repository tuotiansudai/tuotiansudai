package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.IphoneXService;
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
@RequestMapping(value = "/activity/iphonex")
public class IphoneXActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private IphoneXService iphoneXService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getDrawPrizeTime() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/iphonex", "responsive", true);
        modelAndView.addObject("drawCount", iphoneXService.iphoneXDrawTime(LoginUserInfo.getMobile(), LoginUserInfo.getLoginName()));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/iphonex-draw", method = RequestMethod.POST)
    public DrawLotteryResultDto iphoneXTaskDrawPrize(@RequestParam(value = "activityCategory", defaultValue = "IPHONEX_ACTIVITY", required = false) ActivityCategory activityCategory) {
        DrawLotteryResultDto drawLotteryResultDto = lotteryDrawActivityService.drawPrizeByCompleteTask(LoginUserInfo.getMobile(), activityCategory);
        return drawLotteryResultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/draw-time", method = RequestMethod.GET)
    public String iphoneXTaskDrawTime() {
        return String.valueOf(iphoneXService.iphoneXDrawTime(LoginUserInfo.getMobile(), LoginUserInfo.getLoginName()));
    }

    @ResponseBody
    @RequestMapping(value = "/prize-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByAll(@RequestParam(value = "activityCategory", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecord(null, activityCategory);
    }
}
