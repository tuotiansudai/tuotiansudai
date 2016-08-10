package com.tuotiansudai.activity.controller;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.PrepareUserService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/activity/app-share")
public class AppShareController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PrepareUserService prepareUserService;


    private String getReferrerInfo(UserModel referrer) {
        AccountModel referrerAccount = accountMapper.findByLoginName(referrer.getLoginName());
        if (null != referrerAccount && !StringUtils.isEmpty(referrerAccount.getUserName())) {
            return referrerAccount.getUserName().substring(0, 1) + "某";
        } else {
            return referrer.getMobile().substring(0, 2) + "****" + referrer.getMobile().substring(7, 10);
        }
    }

    @RequestMapping(value = "/ios", method = RequestMethod.GET)
    public ModelAndView getIOSPage(@RequestParam(value = "referrerMobile") String referrerMobile, HttpServletRequest httpServletRequest) {
        UserModel referrer = userMapper.findByMobile(referrerMobile);
        if (null == referrer) {
            ModelAndView modelAndView = new ModelAndView("/error/error-info-page");
            modelAndView.addObject("errorInfo", "无效推荐链接");
            return modelAndView;
        }

        String registerMobile = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("registerMobile")) {
                registerMobile = cookie.getValue();
                break;
            }
        }

        if (!StringUtils.isEmpty(registerMobile)) {
            if(userService.mobileIsRegister(registerMobile)){
                ModelAndView modelAndView = new ModelAndView("/activities/share-app");
                modelAndView.addObject("responsive", true);
                modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
                return modelAndView;
            }
        }

        ModelAndView modelAndView = new ModelAndView("/activities/share-app-ios");
        modelAndView.addObject("responsive", true);
        modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
        return modelAndView;
    }

    @RequestMapping(value = "/android", method = RequestMethod.GET)
    public ModelAndView getAndroidPage(@RequestParam(value = "referrerMobile") String referrerMobile, HttpServletRequest httpServletRequest) {
        UserModel referrer = userMapper.findByMobile(referrerMobile);
        if (null == referrer) {
            ModelAndView modelAndView = new ModelAndView("/error/error-info-page");
            modelAndView.addObject("errorInfo", "无效推荐链接");
            return modelAndView;
        }

        String registerMobile = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("registerMobile")) {
                registerMobile = cookie.getValue();
                break;
            }
        }
        if (!StringUtils.isEmpty(registerMobile)) {
            if(userService.mobileIsRegister(registerMobile)){
                ModelAndView modelAndView = new ModelAndView("/activities/share-app");
                modelAndView.addObject("responsive", true);
                modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
                return modelAndView;
            }
        }

        ModelAndView modelAndView = new ModelAndView("/activities/share-app-android");
        modelAndView.addObject("responsive", true);
        modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
        return modelAndView;
    }

    @RequestMapping( method = RequestMethod.GET)
    public ModelAndView getSuccessPage(@RequestParam(value = "referrerMobile") String referrerMobile) {
        UserModel referrer = userMapper.findByMobile(referrerMobile);
        ModelAndView modelAndView = new ModelAndView();
        if (null == referrer) {
            modelAndView.setViewName("/error/error-info-page");
            modelAndView.addObject("errorInfo", "无效推荐链接");
            return modelAndView;
        }

        modelAndView.setViewName("/activities/share-app");
        modelAndView.addObject("responsive", true);
        modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
        return modelAndView;
    }


}
