package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.ActivityConsoleIphoneXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class IphoneXController {

    @Autowired
    private ActivityConsoleIphoneXService activityConsoleIphoneXService;
}
