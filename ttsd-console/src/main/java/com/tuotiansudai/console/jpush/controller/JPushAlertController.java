package com.tuotiansudai.console.jpush.controller;

import com.tuotiansudai.console.jpush.dto.JPushAlertDto;
import com.tuotiansudai.console.jpush.service.JPushAlertService;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.exception.CreateCouponException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.lang.reflect.Method;

@Controller
@RequestMapping(value = "/app-push-manage")
public class JPushAlertController {
    static Logger logger = Logger.getLogger(JPushAlertController.class);
    @Autowired
    private JPushAlertService jPushAlertService;

    @RequestMapping(value = "/manual-app-push",method = RequestMethod.GET)
    public ModelAndView appPush(){
        return new ModelAndView("/manual-app-push");
    }
    @RequestMapping(value = "/manual-app-push",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView buildAppPush(@Valid @ModelAttribute JPushAlertDto jPushAlertDto,RedirectAttributes redirectAttributes){
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        jPushAlertService.buildJPushAlert(loginName, jPushAlertDto);
        modelAndView.setViewName("redirect:/app-push-manage/app-push-list");
        return modelAndView;
    }

}
