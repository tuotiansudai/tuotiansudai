package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.dto.ExchangePrizeDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ExchangePrize;
import com.tuotiansudai.activity.service.ExerciseVSWorkActivityService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/exercise-work")
public class ExerciseVSWorkActivityController {

    @Autowired
    private ExerciseVSWorkActivityService exerciseVSWorkActivityService;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getDrawPrizeTime(){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/sport-play", "responsive", true);
        String loginName=LoginUserInfo.getLoginName();
        modelAndView.addObject("drawCount", 0);
        modelAndView.addObject("investAmount",loginName==null?null: AmountConverter.convertCentToString(exerciseVSWorkActivityService.sumInvestByLoginNameExceptTransferAndTime(loginName)));
        modelAndView.addObject("exchangePrize", loginName==null ?null :
                exerciseVSWorkActivityService.getPrizeByMobile(LoginUserInfo.getMobile())==null?null:exerciseVSWorkActivityService.getPrizeByMobile(LoginUserInfo.getMobile()).getPrizeName());
        modelAndView.addObject("prize",loginName==null ?"" :
                exerciseVSWorkActivityService.getPrizeByMobile(LoginUserInfo.getMobile())==null?"":exerciseVSWorkActivityService.getPrizeByMobile(LoginUserInfo.getMobile()).name());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/exercise-work-draw", method = RequestMethod.POST)
    public DrawLotteryResultDto singleTaskDrawPrize(@RequestParam(value = "activityCategory", defaultValue = "EXERCISE_WORK_ACTIVITY", required = false) ActivityCategory activityCategory) {
        DrawLotteryResultDto drawLotteryResultDto =lotteryDrawActivityService.drawPrizeByCompleteTask(LoginUserInfo.getMobile(), activityCategory);
        return drawLotteryResultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/exchange-prize",method = RequestMethod.POST)
    public ExchangePrizeDto exchangeTaskPrize(@RequestParam(value = "exchangePrize") ExchangePrize exchangePrize){
        ExchangePrizeDto exchangePrizeDto=exerciseVSWorkActivityService.exchangePrize(exchangePrize,LoginUserInfo.getMobile(),ActivityCategory.EXERCISE_WORK_ACTIVITY);
        return exchangePrizeDto;
    }

    @ResponseBody
    @RequestMapping(value = "/draw-time",method = RequestMethod.POST)
    public String getDrawTime(){
        return "0";
    }

}
