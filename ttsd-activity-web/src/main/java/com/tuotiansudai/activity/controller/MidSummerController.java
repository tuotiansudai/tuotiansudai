package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.MidSummerService;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity/mid-summer")
public class MidSummerController {

    @Autowired
    private MidSummerService midSummerService;

    @Autowired
    private WeChatService weChatService;

    @RequestMapping(path = "/shared-user", method = RequestMethod.GET)
    public ModelAndView sharedUserHome() {
        String loginName = LoginUserInfo.getLoginName();
        

        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView("/wechat/mid-summer-unbound");
        }

        if (!weChatService.isBound(loginName)) {
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/mid-summer-bound");
        modelAndView.addObject("sharedUser", true);
        modelAndView.addObject("mobile", LoginUserInfo.getMobile());
        modelAndView.addObject("invitedCount", midSummerService.getInvitedCount(loginName));

        midSummerService.saveSharedUser(loginName);

        return modelAndView;
    }

    @RequestMapping(path = "/invited-user", method = RequestMethod.GET)
    public ModelAndView invitedUserHome(@RequestParam(name = "mobile") String mobile) {
        if (!midSummerService.isUserShared(mobile)) {
            return new ModelAndView("/error/404");
        }
        ModelAndView modelAndView = new ModelAndView("/wechat/mid-summer-bound");
        modelAndView.addObject("sharedUser", false);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("invitedCount", midSummerService.getInvitedCount(mobile));
        return modelAndView;
    }
}
