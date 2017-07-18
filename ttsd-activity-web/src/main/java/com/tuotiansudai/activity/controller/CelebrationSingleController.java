package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.service.CelebrationSingleActivityService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/activity/single-rank")
public class CelebrationSingleController {

    @Autowired
    private CelebrationSingleActivityService celebrationSingleActivityService;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getDrawPrizeTime() {
        ModelAndView modelAndView = new ModelAndView("/activities/single-rank", "responsive", true);
        modelAndView.addObject("drawCount", celebrationSingleActivityService.drawTimeByLoginNameAndActivityCategory(LoginUserInfo.getMobile(),LoginUserInfo.getLoginName()));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/single-draw", method = RequestMethod.POST)
    public DrawLotteryResultDto singleTaskDrawPrize(@RequestParam(value = "activityCategory", defaultValue = "CELEBRATION_SINGLE_ACTIVITY", required = false) ActivityCategory activityCategory) {
        DrawLotteryResultDto drawLotteryResultDto =lotteryDrawActivityService.drawPrizeByCompleteTask(LoginUserInfo.getMobile(), activityCategory);
        return drawLotteryResultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/draw-time", method = RequestMethod.POST)
    public String singleTaskDrawPrize() {
        return String.valueOf(celebrationSingleActivityService.drawTimeByLoginNameAndActivityCategory(LoginUserInfo.getMobile(),LoginUserInfo.getLoginName()));
    }

}
