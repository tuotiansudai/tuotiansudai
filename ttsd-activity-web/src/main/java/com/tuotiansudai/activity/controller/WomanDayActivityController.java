package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.model.WomanDayRecordView;
import com.tuotiansudai.activity.service.ActivityWomanDayService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity/woman-day")
public class WomanDayActivityController {

    @Autowired
    private ActivityWomanDayService activityWomanDayService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-split", "responsive", true);
        List<WomanDayRecordView> records = activityWomanDayService.getWomanDayPrizeRecord(0, Integer.MAX_VALUE, loginName).getRecords();
        int totalLeaves = 0;
        String prize = "";
        if (CollectionUtils.isNotEmpty(records)) {
            totalLeaves = records.get(0).getTotalLeaves();
            prize = records.get(0).getPrize();
        }
        modelAndView.addObject("totalLeaves", totalLeaves);
        modelAndView.addObject("prize", prize);
        return modelAndView;
    }
}
