package com.tuotiansudai.activity.controller;

import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity/july-activity")
public class JulyActivity {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView activityHome() {
        return new ModelAndView("/activities/2018/July-activity", "responsive", true);
    }
}
