package com.tuotiansudai.activity.controller;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.PrepareModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/app-share")
public class AppShareController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PrepareUserMapper prepareUserMapper;

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
            }
        }
        if (!StringUtils.isEmpty(registerMobile)) {
            PrepareModel prepareUser = prepareUserMapper.findByMobile(referrerMobile);
            if (null != prepareUser) {
                ModelAndView modelAndView = new ModelAndView("/activities/share-app");
                modelAndView.addObject("userMobile", prepareUser.getMobile());
                return modelAndView;
            }
        }

        ModelAndView modelAndView = new ModelAndView("/activities/share-app-android");
        AccountModel referrerAccount = accountMapper.findByLoginName(referrer.getLoginName());
        if (null != referrerAccount && !StringUtils.isEmpty(referrerAccount.getUserName())) {
            modelAndView.addObject("referrerInfo", referrerAccount.getUserName().substring(0, 1) + "**");
        } else {
            modelAndView.addObject("referrerInfo", referrer.getMobile().substring(0, 2) + "****" + referrer.getMobile().substring(7, 10));
        }
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
            }
        }
        if (!StringUtils.isEmpty(registerMobile)) {
            UserModel user = userMapper.findByMobile(registerMobile);
            if (null != user) {
                ModelAndView modelAndView = new ModelAndView("/activities/share-app");
                modelAndView.addObject("userMobile", user.getMobile());
                return modelAndView;
            }
        }

        ModelAndView modelAndView = new ModelAndView("/activities/share-app-android");
        AccountModel referrerAccount = accountMapper.findByLoginName(referrer.getLoginName());
        if (null != referrerAccount && !StringUtils.isEmpty(referrerAccount.getUserName())) {
            modelAndView.addObject("referrerInfo", referrerAccount.getUserName().substring(0, 1) + "**");
        } else {
            modelAndView.addObject("referrerInfo", referrer.getMobile().substring(0, 2) + "****" + referrer.getMobile().substring(7, 10));
        }
        return modelAndView;
    }
}
