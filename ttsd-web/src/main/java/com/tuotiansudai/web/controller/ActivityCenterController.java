package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ActivityService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/activity-center")
public class ActivityCenterController {

    @Autowired
    ActivityService activityService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView getAllOperatingActivities() {
        ModelAndView modelAndView = new ModelAndView("/activity-center");

        String loginName = LoginUserInfo.getLoginName();
        List<ActivityDto> activityDtos = activityService.getAllActiveActivities(loginName, Source.WEB);
        modelAndView.addObject("data", activityDtos);
        modelAndView.addObject("responsive",true);
        return modelAndView;
    }
}
