package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.WomanDayRecordView;
import com.tuotiansudai.activity.service.ActivityWomenDayService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping(value = "/activity/women-day")
public class WomenDayActivityController {

    @Autowired
    private ActivityWomenDayService activityWomanDayService;

    @Autowired
    private SignInService signInService;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/women-day", "responsive", true);
        List<WomanDayRecordView> records = activityWomanDayService.getWomanDayPrizeRecord(0, Integer.MAX_VALUE, loginName).getRecords();
        int totalLeaves = 0;
        String prize = "";
        if (CollectionUtils.isNotEmpty(records)) {
            totalLeaves = records.get(0).getTotalLeaves();
            prize = records.get(0).getPrize();
        }
        modelAndView.addObject("totalLeaves", totalLeaves);
        modelAndView.addObject("isActivity", activityWomanDayService.isActivityTime());
        modelAndView.addObject("signedIn", signInService.signInIsSuccess(loginName));
        modelAndView.addObject("isDraw", Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? true : lotteryDrawActivityService.toDayIsDrawByMobile(LoginUserInfo.getMobile(), ActivityCategory.WOMAN_DAY_ACTIVITY) > 0);
        modelAndView.addObject("prize", prize);
        return modelAndView;
    }
}
