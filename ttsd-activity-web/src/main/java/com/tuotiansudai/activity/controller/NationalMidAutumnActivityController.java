package com.tuotiansudai.activity.controller;

import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestView;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.activity.service.NationalMidAutumnService;
import com.tuotiansudai.activity.service.SchoolSeasonService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/activity/national-mid-autumn")
public class NationalMidAutumnActivityController {

    @Autowired
    private NationalMidAutumnService nationalMidAutumnService;

    @Value(value = "${activity.national.day.startTime}")
    private String activityNationalDayStartTime;

    @Value(value = "${activity.national.day.endTime}")
    private String activityNationalDayEndTime;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView nationalMidAutumn() {
        ModelAndView modelAndView = new ModelAndView("/activities/2017/national-day", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();

        List<NewmanTyrantView> celebrationHeroRankingViews = nationalMidAutumnService.obtainRank(new Date());

        int investRanking = CollectionUtils.isNotEmpty(celebrationHeroRankingViews) ?
                Iterators.indexOf(celebrationHeroRankingViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1 : 0;
        long investAmount = investRanking > 0 ? celebrationHeroRankingViews.get(investRanking - 1).getSumAmount() : 0;

        if (investRanking > 10) {
            investRanking = 0;
        }
        modelAndView.addObject("prizeDto", nationalMidAutumnService.obtainPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        modelAndView.addObject("investRanking", investRanking);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("activityStartTime", activityNationalDayStartTime);
        modelAndView.addObject("activityEndTime", activityNationalDayEndTime);
        modelAndView.addObject("currentTime", new DateTime().withTimeAtStartOfDay().toDate());

        return modelAndView;
    }

    @RequestMapping(value = "/ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<NewmanTyrantView> obtainRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        final String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<NewmanTyrantView> baseListDataDto = new BasePaginationDataDto<>();
        List<NewmanTyrantView> nationalDayRankViews = nationalMidAutumnService.obtainRank(tradingTime);

        nationalDayRankViews = CollectionUtils.isNotEmpty(nationalDayRankViews) && nationalDayRankViews.size() > 10 ? nationalDayRankViews.subList(0, 10) : nationalDayRankViews;

        nationalDayRankViews.stream().forEach(newmanTyrantView -> newmanTyrantView.setLoginName(nationalMidAutumnService.encryptMobileForWeb(loginName, newmanTyrantView.getLoginName(), newmanTyrantView.getMobile())));
        baseListDataDto.setRecords(nationalDayRankViews);
        baseListDataDto.setStatus(true);

        return baseListDataDto;
    }
}
