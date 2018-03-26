package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.model.ActivityInvestRanking;
import com.tuotiansudai.activity.repository.model.MyHeroRanking;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.activity.service.ActivityRankingService;
import com.tuotiansudai.activity.service.ActivityWeChatDrawCouponService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.WeChatDrawCoupon;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping(value="/activity/spring-breeze")
public class SpringBreezeActivityController {

    @Autowired
    private ActivityWeChatDrawCouponService activityWeChatService;

    @Autowired
    private ActivityRankingService activityRankingService;

    @Autowired
    private WeChatService weChatService;

    private String startTime = ETCDConfigReader.getReader().getValue("activity.spring.breeze.startTime");

    private String endTime = ETCDConfigReader.getReader().getValue("activity.spring.breeze.endTime");

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView springBreeze(){
        ModelAndView modelAndView = new ModelAndView("/activities/2018/peach-blossom-festival","responsive", true);
        modelAndView.addAllObjects(activityRankingService.activityHome(LoginUserInfo.getLoginName(), ActivityInvestRanking.SPRING_BREEZE_ACTIVITY_RANKING));
        modelAndView.addObject("prizeDto", activityRankingService.obtainPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        return modelAndView;
    }

    @RequestMapping(value = "/ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<NewmanTyrantView> obtainRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        return activityRankingService.obtainRanking(tradingTime, LoginUserInfo.getLoginName(), ActivityInvestRanking.SPRING_BREEZE_ACTIVITY_RANKING);
    }

    @RequestMapping(value = "/my-ranking/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public MyHeroRanking obtainMyRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        return activityRankingService.obtainMyRanking(tradingTime, LoginUserInfo.getLoginName(), ActivityInvestRanking.SPRING_BREEZE_ACTIVITY_RANKING);
    }

    @RequestMapping(path = "/wechat", method = RequestMethod.GET)
    public ModelAndView startWorkActivityWechat(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/spring-breeze");
        }
        ModelAndView modelAndView = new ModelAndView("/wechat/peach-blossom-festival");
        modelAndView.addObject("activityStartTime", startTime);
        modelAndView.addObject("activityEndTime", endTime);
        String loginName = LoginUserInfo.getLoginName();
        if (loginName != null) {
            modelAndView.addObject("drewCoupon", activityWeChatService.drewCoupon(loginName, WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT));
        }
        return modelAndView;
    }

    @RequestMapping(path = "/draw", method = RequestMethod.GET)
    public ModelAndView newYearActivityDrawCoupon(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/spring-breeze");
        }
        boolean duringActivities = activityWeChatService.duringActivities(WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT);
        if (!duringActivities) {
            return new ModelAndView("redirect:/activity/spring-breeze/wechat");
        }

        String loginName = LoginUserInfo.getLoginName();
        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView("redirect:/we-chat/entry-point?redirect=/activity/spring-breeze/draw");
        }
        if (!weChatService.isBound(loginName)) {
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/peach-blossom-festival");
        boolean drewCoupon = activityWeChatService.drewCoupon(loginName, WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT);
        modelAndView.addObject("activityStartTime", startTime);
        modelAndView.addObject("activityEndTime", endTime);
        if (!drewCoupon) {
            activityWeChatService.sendDrawCouponMessage(loginName, WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT);
            modelAndView.addObject("drawSuccess", true);
        }
        modelAndView.addObject("drewCoupon", true);
        return modelAndView;
    }
}
