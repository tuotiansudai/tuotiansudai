package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.MyHeroRanking;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.activity.service.YearEndAwardsActivityService;
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
import java.util.Map;

@Controller
@RequestMapping(value = "/activity/year-end-awards")
public class YearEndAwardsActivityController {

    @Autowired
    private YearEndAwardsActivityService yearEndAwardsActivityService;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Value(value = "${activity.year.end.awards.startTime}")
    private String activityYearEndAwardsStartTime;

    @Value(value = "${activity.year.end.awards.rankTime}")
    private String activityYearEndAwardsRankTime;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView yearEndAwards() {
        ModelAndView modelAndView = new ModelAndView("/year-end-awards", "responsive", false);
        String loginName = LoginUserInfo.getLoginName();
        List<NewmanTyrantView> yearEndAwardsRankingViews = yearEndAwardsActivityService.obtainRank(new Date());
        int investRanking = CollectionUtils.isNotEmpty(yearEndAwardsRankingViews) ?
                Iterators.indexOf(yearEndAwardsRankingViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1 : 0;

        modelAndView.addObject("prizeDto", yearEndAwardsActivityService.obtainPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        modelAndView.addObject("investRanking", investRanking > 10 ? 0 : investRanking);
        modelAndView.addObject("investAmount", investRanking > 0 ? yearEndAwardsRankingViews.get(investRanking - 1).getSumAmount() : 0);
        modelAndView.addObject("activityStartTime", activityYearEndAwardsStartTime);
        modelAndView.addObject("activityEndTime", activityYearEndAwardsRankTime);
        modelAndView.addObject("currentTime", new DateTime().withTimeAtStartOfDay().toDate());

        Map<String, String> map = yearEndAwardsActivityService.annualizedAmountAndRewards(loginName);
        modelAndView.addObject("sumAnnualizedAmount", map.get("sumAnnualizedAmount"));
        modelAndView.addObject("rewards", map.get("rewards"));
        return modelAndView;
    }

    @RequestMapping(value = "/ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<NewmanTyrantView> obtainRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        BasePaginationDataDto<NewmanTyrantView> baseListDataDto = new BasePaginationDataDto<>();
        List<NewmanTyrantView> yearEndAwardsRankViews = yearEndAwardsActivityService.obtainRank(tradingTime);
        yearEndAwardsRankViews = CollectionUtils.isNotEmpty(yearEndAwardsRankViews) && yearEndAwardsRankViews.size() > 10 ? yearEndAwardsRankViews.subList(0, 10) : yearEndAwardsRankViews;
        yearEndAwardsRankViews.stream().forEach(newmanTyrantView -> newmanTyrantView.setLoginName(yearEndAwardsActivityService.encryptMobileForWeb(LoginUserInfo.getLoginName(), newmanTyrantView.getLoginName(), newmanTyrantView.getMobile())));
        baseListDataDto.setRecords(yearEndAwardsRankViews);
        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }

    @RequestMapping(value = "/my-ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public MyHeroRanking obtainMyRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        MyHeroRanking myHeroRanking = new MyHeroRanking();
        List<NewmanTyrantView> yearEndAwardsRankViews = yearEndAwardsActivityService.obtainRank(tradingTime);
        int investRanking = CollectionUtils.isNotEmpty(yearEndAwardsRankViews) ?
                Iterators.indexOf(yearEndAwardsRankViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(LoginUserInfo.getLoginName())) + 1 : 0;
        myHeroRanking.setInvestAmount(investRanking > 0 ? yearEndAwardsRankViews.get(investRanking - 1).getSumAmount() : 0);
        myHeroRanking.setInvestRanking(investRanking > 10 ? 0 : investRanking);
        return myHeroRanking;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public DrawLotteryResultDto iphoneXTaskDrawPrize(@RequestParam(value = "activityCategory", defaultValue = "YEAR_END_AWARDS_ACTIVITY", required = false) ActivityCategory activityCategory) {
        DrawLotteryResultDto drawLotteryResultDto = lotteryDrawActivityService.drawPrizeByCompleteTask(LoginUserInfo.getMobile(), activityCategory);
        return drawLotteryResultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/user-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByLoginName(@RequestParam(value = "mobile", required = false) String mobile,
                                                                @RequestParam(value = "activityCategory", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecordByMobile(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile(), activityCategory);
    }

    @ResponseBody
    @RequestMapping(value = "/all-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByAll(@RequestParam(value = "activityCategory", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecord(null, activityCategory);
    }
}
