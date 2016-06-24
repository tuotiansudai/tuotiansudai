package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.service.ActivityService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping(path = "/activity-center")
public class ActivityCenterController {

    static Logger logger = Logger.getLogger(ActivityCenterController.class);

    @Autowired
    ActivityService activityService;

    @RequestMapping(path = "")
    @ResponseBody
    public ModelAndView getAllOperatingActivities() {
        ModelAndView modelAndView = new ModelAndView("/activity-center");

        String loginName = LoginUserInfo.getLoginName();
        List<ActivityDto> activityDtos = activityService.getAllOperatingActivities(loginName);
        modelAndView.addObject("data", activityDtos);

        return modelAndView;
    }
}
