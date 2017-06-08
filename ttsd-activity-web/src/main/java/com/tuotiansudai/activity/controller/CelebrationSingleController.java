package com.tuotiansudai.activity.controller;

import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.activity.repository.model.MyHeroRanking;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/activity/hero-ranking")
public class CelebrationHeroRankingController {

    @Autowired
    private CelebrationHeroRankingService celebrationHeroRankingService;


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView celebrationHeroRanking(){
        ModelAndView modelAndView=new ModelAndView("/2017/hero-ranking","responsive", true);
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
        return modelAndView;
    }

    @RequestMapping(value = "/invest/{tradingTime}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public MyHeroRanking obtainMyRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime){
        final String loginName = LoginUserInfo.getLoginName();
        MyHeroRanking myHeroRanking=new MyHeroRanking();
        List<NewmanTyrantView> celebrationHeroRankingViews = celebrationHeroRankingService.obtainHero(tradingTime);

        int investRanking = CollectionUtils.isNotEmpty(celebrationHeroRankingViews) ?
                Iterators.indexOf(celebrationHeroRankingViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1 : 0;
        long investAmount = investRanking > 0 ? celebrationHeroRankingViews.get(investRanking - 1).getSumAmount() : 0;

        myHeroRanking.setInvestAmount(investAmount);
        myHeroRanking.setInvestRanking(investRanking);

        return myHeroRanking;

    }


}
