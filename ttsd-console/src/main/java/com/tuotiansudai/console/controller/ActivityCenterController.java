package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.service.ActivityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


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

}
