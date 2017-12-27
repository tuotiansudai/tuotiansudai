package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.HeroRankingService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.DateConvertUtil;
import com.tuotiansudai.util.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class LanternFestivalController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private HeroRankingService heroRankingService;

    @Autowired
    private RandomUtils randomUtils;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView lanternFestival() {
        ModelAndView modelAndView = new ModelAndView("/activities/lantern-festival", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        Map param = heroRankingService.obtainHeroRankingAndInvestAmountByLoginName(ActivityCategory.LANTERN_FESTIVAL_ACTIVITY, new Date(), loginName);
        modelAndView.addObject("investRanking", Integer.parseInt(String.valueOf(param.get("investRanking"))));
        modelAndView.addObject("investAmount", String.valueOf(param.get("investAmount")));
        modelAndView.addObject("currentTime", DateConvertUtil.currentDate());
        modelAndView.addObject("mysteriousPrizeDto", heroRankingService.obtainMysteriousPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        List<String> activityTime = heroRankingService.getActivityPeriod(ActivityCategory.LANTERN_FESTIVAL_ACTIVITY);
        modelAndView.addObject("activityStartTime", activityTime.get(0));
        modelAndView.addObject("activityEndTime", activityTime.get(1));
        return modelAndView;

    }
    @RequestMapping(value = "/invest/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<HeroRankingView> obtainHeroRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        final String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<HeroRankingView> baseListDataDto = new BasePaginationDataDto<>();
        List<HeroRankingView> heroRankingViews = heroRankingService.obtainHeroRanking(ActivityCategory.LANTERN_FESTIVAL_ACTIVITY, tradingTime);

        if (heroRankingViews != null) {
            for (HeroRankingView heroRankingView : heroRankingViews) {
                if (heroRankingView.getLoginName().equalsIgnoreCase(loginName)) {
                    heroRankingView.setLoginName("您的位置");
                    continue;
                }
                heroRankingView.setLoginName(randomUtils.encryptMobile(loginName, heroRankingView.getLoginName()));

            }

            baseListDataDto.setRecords(heroRankingViews.size() > 10 ? heroRankingViews.subList(0, 10) : heroRankingViews);
        }
        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }

    @RequestMapping(value = "/prize", method = RequestMethod.POST)
    @ResponseBody
    public DrawLotteryResultDto prize() {
        return new DrawLotteryResultDto(3);
    }

    @ResponseBody
    @RequestMapping(value = "/drawTime", method = RequestMethod.GET)
    public int getDrawTime() {
        return 0;
    }

    @RequestMapping(value = "/user-prize-list", method = RequestMethod.GET)
    @ResponseBody
    public List<UserLotteryPrizeView> userPrizeList() {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecordByMobile(LoginUserInfo.getMobile(), ActivityCategory.LANTERN_FESTIVAL_ACTIVITY);
    }

    @RequestMapping(value = "/all-prize-list", method = RequestMethod.GET)
    @ResponseBody
    public List<UserLotteryPrizeView> allPrizeList() {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecord(null, ActivityCategory.LANTERN_FESTIVAL_ACTIVITY);
    }




}
