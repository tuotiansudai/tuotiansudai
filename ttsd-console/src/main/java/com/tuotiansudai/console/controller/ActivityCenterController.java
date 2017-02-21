package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.ActivityDto;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.activity.service.ActivityService;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/activity-manage")
public class ActivityCenterController {

    static Logger logger = Logger.getLogger(ActivityCenterController.class);

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/activity-center", method = RequestMethod.GET)
    public ModelAndView activityCenter() {
        ModelAndView modelAndView = new ModelAndView("/activity-center-edit");
        modelAndView.addObject("sources", Lists.newArrayList(Source.values()));
        modelAndView.addObject("appUrls", Lists.newArrayList(AppUrl.values()).stream().filter(n -> n != AppUrl.NONE).collect(Collectors.toList()));
        return modelAndView;
    }

    @RequestMapping(value = "/activity-center/{activityId}", method = RequestMethod.GET)
    public ModelAndView activityCenter(@PathVariable long activityId) {
        ModelAndView modelAndView = new ModelAndView("/activity-center-edit");

        ActivityDto activityDto = activityService.findActivityDtoById(activityId);
        modelAndView.addObject("sources", Lists.newArrayList(Source.values()));
        modelAndView.addObject("appUrls", Lists.newArrayList(AppUrl.values()).stream().filter(n -> n != AppUrl.NONE).collect(Collectors.toList()));
        modelAndView.addObject("dto", activityDto);
        return modelAndView;
    }

    @RequestMapping(value = "/activity-center/{activityStatus}", method = RequestMethod.POST)
    public ModelAndView activityCenter(@ModelAttribute ActivityDto activityDto, @PathVariable ActivityStatus activityStatus, HttpServletRequest request) {

        String ip = RequestIPParser.parse(request);
        String loginName = LoginUserInfo.getLoginName();
        activityService.saveOrUpdate(activityDto, activityStatus, loginName, ip);

        return new ModelAndView("redirect:/activity-manage/activity-center-list");
    }

    @RequestMapping(value = "/activity-center-list", method = RequestMethod.GET)
    public ModelAndView activityCenterList(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                           @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                           @RequestParam(value = "activityStatus", required = false) ActivityStatus activityStatus,
                                           @RequestParam(value = "source", required = false) Source source) {

        List<ActivityDto> activityDtoList = activityService.findAllActivities(startTime, endTime, activityStatus, source);

        ModelAndView modelAndView = new ModelAndView("/activity-center-list");
        modelAndView.addObject("activityStatusList", Lists.newArrayList(ActivityStatus.values()));
        modelAndView.addObject("sourceList", Lists.newArrayList(Source.WEB, Source.IOS, Source.ANDROID));

        modelAndView.addObject("activityCenterList", activityDtoList);
        modelAndView.addObject("activityStatus", activityStatus);
        modelAndView.addObject("source", source);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }

}
