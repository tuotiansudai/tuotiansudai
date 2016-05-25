package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.*;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private UserMessageService userMessageService;

    @RequestMapping(value = "/{id:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView messageDetail(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/message");
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("loginName",loginName);
        modelAndView.addObject("messageDetail", userMessageService.findById(id));
        return modelAndView;
    }
}
