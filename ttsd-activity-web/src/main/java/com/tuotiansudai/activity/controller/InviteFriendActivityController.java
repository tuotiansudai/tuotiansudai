package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.InviteFriendActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value = "/activity/invite-friend")
public class InviteFriendActivityController {

    @Autowired
    private InviteFriendActivityService inviteFriendActivityService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadPageData() {
        ModelAndView modelAndView = new ModelAndView("/activities/invite-friend", "responsive", true);
        Map<String, String> activityReferrerParam = inviteFriendActivityService.getActivityReferrer("gaoyinglong");
        modelAndView.addObject("referrerCount", activityReferrerParam.get("referrerCount"));
        modelAndView.addObject("referrerRedEnvelop", activityReferrerParam.get("referrerRedEnvelop"));
        modelAndView.addObject("referrerAmount", activityReferrerParam.get("referrerAmount"));
        return modelAndView;
    }
}
