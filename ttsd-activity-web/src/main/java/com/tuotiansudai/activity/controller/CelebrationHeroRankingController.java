package com.tuotiansudai.activity.controller;

import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.activity.service.CelebrationHeroRankingService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/activity/celebration-heroRanking")
public class CelebrationHeroRankingController {

    @Autowired
    private CelebrationHeroRankingService celebrationHeroRankingService;


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView celebrationHeroRanking(){
        ModelAndView modelAndView=new ModelAndView("/activities/celebration-heroRanking","responsive", true);
        String loginName = LoginUserInfo.getLoginName();
        List<String> activityTime=celebrationHeroRankingService.getActivityTime();

        List<NewmanTyrantView> celebrationHeroRankingViews=celebrationHeroRankingService.obtainHero(new Date());

        int investRanking = CollectionUtils.isNotEmpty(celebrationHeroRankingViews) ?
                Iterators.indexOf(celebrationHeroRankingViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1 : 0;
        long investAmount = investRanking > 0 ? celebrationHeroRankingViews.get(investRanking - 1).getSumAmount() : 0;


        modelAndView.addObject("prizeDto", celebrationHeroRankingService.obtainPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        modelAndView.addObject("investRanking", investRanking);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("activityStartTime", activityTime.get(0));
        modelAndView.addObject("activityEndTime", activityTime.get(1));
        modelAndView.addObject("currentTime", new DateTime().withTimeAtStartOfDay().toDate());
        modelAndView.addObject("yesterdayTime", DateUtils.addDays(new DateTime().withTimeAtStartOfDay().toDate(), -1));

        return modelAndView;
    }

    @RequestMapping(value = "/hreo/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<NewmanTyrantView> obtainCelebrationHero(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        final String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<NewmanTyrantView> baseListDataDto = new BasePaginationDataDto<>();
        List<NewmanTyrantView> celebrationHeroRankingViews = celebrationHeroRankingService.obtainHero(tradingTime);
        celebrationHeroRankingViews.stream().forEach(newmanTyrantView -> newmanTyrantView.setLoginName(celebrationHeroRankingService.encryptMobileForWeb(loginName, newmanTyrantView.getLoginName(), newmanTyrantView.getMobile())));
        baseListDataDto.setRecords(celebrationHeroRankingViews);
        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }



}
