package com.tuotiansudai.activity.controller;

import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestView;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.activity.service.SchoolSeasonService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value="/activity/school-season")
public class SchoolSeasonActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private SchoolSeasonService schoolSeasonService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView schoolSeason(){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/school-open","responsive", true);
        String loginName = LoginUserInfo.getLoginName();

        List<ActivityInvestView> activityInvestViews=schoolSeasonService.obtainRank();
        int investRanking = 0;
        if (CollectionUtils.isNotEmpty(activityInvestViews)) {
            investRanking = Iterators.indexOf(activityInvestViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1;
            activityInvestViews.stream().forEach(activityInvestView -> activityInvestView.setLoginName(schoolSeasonService.encryptMobileForWeb(loginName, activityInvestView.getLoginName(), activityInvestView.getMobile())));
        }
        long investAmount = investRanking > 0 ? activityInvestViews.get(investRanking - 1).getSumAmount() : 0;

        modelAndView.addObject("drawCount", loginName==null?0:schoolSeasonService.toDayIsDrawByMobile(LoginUserInfo.getMobile(),ActivityCategory.SCHOOL_SEASON_ACTIVITY));
        modelAndView.addObject("investRanking", investRanking);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("rankList", activityInvestViews.size()>=18? activityInvestViews.subList(0,18):activityInvestViews);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/task-draw", method = RequestMethod.POST)
    public DrawLotteryResultDto singleTaskDrawPrize(@RequestParam(value = "activityCategory", defaultValue = "SCHOOL_SEASON_ACTIVITY", required = false) ActivityCategory activityCategory) {
        return new DrawLotteryResultDto(3);
    }

    @ResponseBody
    @RequestMapping(value = "/user-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByLoginName(@RequestParam(value = "mobile", required = false) String mobile,
                                                                @RequestParam(value = "activityCategory", defaultValue = "SCHOOL_SEASON_ACTIVITY", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecordByMobile(LoginUserInfo.getMobile(), activityCategory);
    }

    @ResponseBody
    @RequestMapping(value = "/all-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByAll(@RequestParam(value = "activityCategory", defaultValue = "SCHOOL_SEASON_ACTIVITY", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecord(null, activityCategory);
    }
}
