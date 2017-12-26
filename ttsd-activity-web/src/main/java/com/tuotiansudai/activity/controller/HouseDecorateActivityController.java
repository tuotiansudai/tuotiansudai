package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.service.HouseDecorateService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/house-decorate")
public class HouseDecorateActivityController {

    @Autowired
    private HouseDecorateService houseDecorateService;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView familyFinanceIndex(){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/house-decorate","responsive", true);
        modelAndView .addObject("toDayAmount", LoginUserInfo.getLoginName()==null?null: AmountConverter.convertCentToString(houseDecorateService.toDayInvestAmount(LoginUserInfo.getLoginName())));
        modelAndView .addObject("amount", LoginUserInfo.getLoginName()==null?null: AmountConverter.convertCentToString(houseDecorateService.investAmount(LoginUserInfo.getLoginName())));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/house-decorate-draw", method = RequestMethod.POST)
    public DrawLotteryResultDto lotteryDraw(@RequestParam(value = "activityCategory", defaultValue = "HOUSE_DECORATE_ACTIVITY", required = false) ActivityCategory activityCategory){
        return new DrawLotteryResultDto(3);
    }

}
