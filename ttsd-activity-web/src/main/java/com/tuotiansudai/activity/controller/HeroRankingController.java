package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.service.HeroRankingService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class HeroRankingController {

    @Autowired
    private HeroRankingService heroRankingService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        Map param = heroRankingService.obtainHeroRankingAndInvestAmountByLoginName(ActivityCategory.HERO_RANKING, new Date(), loginName);
        Integer investRanking = Integer.parseInt(String.valueOf(param.get("investRanking")));
        String investAmount = param.get("investAmount").toString();
        ModelAndView modelAndView = new ModelAndView("/activities/hero-ranking", "responsive", true);
        modelAndView.addObject("currentTime", new DateTime().withTimeAtStartOfDay().toDate());
        modelAndView.addObject("yesterdayTime", DateUtils.addDays(new DateTime().withTimeAtStartOfDay().toDate(), -1));
        Integer referRanking = heroRankingService.findHeroRankingByReferrerLoginName(ActivityCategory.HERO_RANKING, loginName);
        modelAndView.addObject("investRanking", investRanking);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("referRanking", referRanking);
        modelAndView.addObject("mysteriousPrizeDto", heroRankingService.obtainMysteriousPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        return modelAndView;
    }

    @RequestMapping(value = "/new", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadNewPageData() {
        String loginName = LoginUserInfo.getLoginName();
        Map param = heroRankingService.obtainHeroRankingAndInvestAmountByLoginName(ActivityCategory.NEW_HERO_RANKING, new Date(), loginName);
        Integer investRanking = Integer.parseInt(String.valueOf(param.get("investRanking")));
        String investAmount = param.get("investAmount").toString();
        ModelAndView modelAndView = new ModelAndView("/activities/hero-standings", "responsive", true);
        modelAndView.addObject("currentTime", new DateTime().withTimeAtStartOfDay().toDate());
        modelAndView.addObject("yesterdayTime", DateUtils.addDays(new DateTime().withTimeAtStartOfDay().toDate(), -1));
        Integer referRanking = heroRankingService.findHeroRankingByReferrerLoginName(ActivityCategory.NEW_HERO_RANKING, loginName);
        modelAndView.addObject("investRanking", investRanking);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("referRanking", referRanking);
        modelAndView.addObject("mysteriousPrizeDto", heroRankingService.obtainMysteriousPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        List<String> activityTime = heroRankingService.getActivityTime();
        modelAndView.addObject("activityStartTime", activityTime.get(0));
        modelAndView.addObject("activityEndTime", activityTime.get(1));
        Date activityEndTime = DateTime.parse(param.get("activityEndTime").toString(), org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        if (activityEndTime.after(DateTime.now().toDate())) {
            modelAndView.addObject("activityStatus", "true");
            modelAndView.addObject("currentTime", DateTime.now().toDate());
        } else {
            modelAndView.addObject("activityStatus", "false");
            modelAndView.addObject("currentTime", activityEndTime);
        }
        return modelAndView;
    }


    @RequestMapping(value = "/referrer-invest/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<HeroRankingView> obtainHeroRankingByReferrer(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        return heroRankingService.findHeroRankingByReferrer(tradingTime, LoginUserInfo.getLoginName(), 1, 3);
    }

    @RequestMapping(value = "/invest/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<HeroRankingView> obtainHeroRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime,
                                                                    @RequestParam(value = "activityCategory", defaultValue = "HERO_RANKING") ActivityCategory activityCategory) {
        final String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<HeroRankingView> baseListDataDto = new BasePaginationDataDto<>();
        List<HeroRankingView> heroRankingViews = heroRankingService.obtainHeroRanking(activityCategory, tradingTime);

        if (heroRankingViews != null) {
            for (HeroRankingView heroRankingView : heroRankingViews) {
                if (heroRankingView.getLoginName().equalsIgnoreCase(loginName)) {
                    heroRankingView.setLoginName("您的位置");
                    continue;
                }
                heroRankingView.setLoginName(this.encryptMobileForWeb(loginName, heroRankingView.getLoginName()));

            }

            baseListDataDto.setRecords(heroRankingViews.size() > 10 ? heroRankingViews.subList(0, 10) : heroRankingViews);
        }
        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }

    private String encryptMobileForWeb(String loginName, String encryptLoginName) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return "您的位置";
        }

        return MobileEncryptor.encryptMiddleMobile(userMapper.findByLoginName(encryptLoginName).getMobile());
    }


}
