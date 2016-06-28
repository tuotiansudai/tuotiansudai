package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ActivityService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/activity-manage")
public class ActivityCenterController {

    static Logger logger = Logger.getLogger(ActivityCenterController.class);
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/activity-center", method = RequestMethod.GET)
    public ModelAndView activityCenter(){
        return new ModelAndView("/activity-center-edit");
    }

    @RequestMapping(value = "/activity-center/{activityId}", method = RequestMethod.GET)
    public ModelAndView activityCenter(@PathVariable long activityId){
        ModelAndView modelAndView = new ModelAndView("/activity-center-edit");
        ActivityModel activityModel = activityService.findById(activityId);
        ActivityDto activityDto = new ActivityDto(activityModel);
        modelAndView.addObject("dto",activityDto);
        return modelAndView;
    }

    @RequestMapping(value = "/activity-center/{activityStatus}",method = RequestMethod.POST)
    public ModelAndView activityCenter(@ModelAttribute ActivityDto activityDto, @PathVariable ActivityStatus activityStatus){

        String loginName = LoginUserInfo.getLoginName();
        activityService.createEditRecheckActivity(activityDto,activityStatus,loginName);

        return new ModelAndView("redirect:/activity-manage/activity-center");
    }

    @RequestMapping(value = "/activity-center-list", method = RequestMethod.GET)
    public ModelAndView activityCenterList(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "activityStatus", required = false) ActivityStatus activityStatus,
                                        @RequestParam(value = "source", required = false) Source source){

        List<ActivityDto> activityDtoList = activityService.findAllActivities(
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? new DateTime(9999, 12, 31, 0, 0, 0).toDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                activityStatus, source);

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
