package com.tuotiansudai.activity.controller;

import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestView;
import com.tuotiansudai.activity.repository.model.MyHeroRanking;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.activity.service.ActivityRankingService;
import com.tuotiansudai.activity.service.ActivityWeChatService;
import com.tuotiansudai.activity.service.SpringBreezeActivityService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="/activity/spring-breeze")
public class SpringBreezeActivityController {

    @Autowired
    private ActivityWeChatService activityWeChatService;

    @Autowired
    private ActivityRankingService activityRankingService;

    @Value(value = "${activity.spring.breeze.startTime}")
    private String activityStartTime;

    @Value(value = "${activity.spring.breeze.endTime}")
    private String activityEndTime;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView schoolSeason(){
        ModelAndView modelAndView = new ModelAndView("/activities/2018/spring-breeze","responsive", true);
        modelAndView.addAllObjects(activityRankingService.activityHome(LoginUserInfo.getLoginName(), ActivityCategory.SPRING_BREEZE_ACTIVITY));
        return modelAndView;
    }

    @RequestMapping(value = "/ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<NewmanTyrantView> obtainRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        return activityRankingService.obtainRanking(tradingTime, LoginUserInfo.getLoginName(), ActivityCategory.SPRING_BREEZE_ACTIVITY);
    }

    @RequestMapping(value = "/my-ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public MyHeroRanking obtainMyRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        return activityRankingService.obtainMyRanking(tradingTime, LoginUserInfo.getLoginName(), ActivityCategory.SPRING_BREEZE_ACTIVITY);
    }
}
