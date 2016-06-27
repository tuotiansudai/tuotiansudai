package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ActivityService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping(path = "/activity-center")
public class ActivityCenterController {

    static Logger logger = Logger.getLogger(ActivityCenterController.class);

    @Autowired
    ActivityService activityService;

    @RequestMapping(path = "")
    @ResponseBody
    public List<ActivityDto> getAllOperatingActivities() {
        String LoginName = LoginUserInfo.getLoginName();
        return activityService.getAllOperatingActivities(LoginName, Source.WEB);
    }
}
