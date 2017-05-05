package com.tuotiansudai.activity.controller;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
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
import java.util.List;


public class AppShareController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PrepareUserMapper prepareUserMapper;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAppSharePage(@RequestParam(value = "referrerMobile",required = false) String referrerMobile, HttpServletRequest httpServletRequest) {
        UserModel referrer = userMapper.findByMobile(referrerMobile);
        if (null == referrer) {
            ModelAndView modelAndView = new ModelAndView("/error/error-info-page");
            modelAndView.addObject("errorInfo", "无效推荐链接");
            return modelAndView;
        }

        String registerMobile = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("registerMobile")) {
                    registerMobile = cookie.getValue();
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(registerMobile)) {
            if(userService.mobileIsRegister(registerMobile)){
                ModelAndView modelAndView = new ModelAndView("/wechat/share-app");
                modelAndView.addObject("responsive", true);
                modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
                return modelAndView;
            }
        }
        String userAgent = httpServletRequest.getHeader("user-agent");
        ModelAndView modelAndView = new ModelAndView(obtainPageByMobileClient(userAgent));
        modelAndView.addObject("responsive", true);
        modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
        return modelAndView;
    }

    @RequestMapping(value = "/success",method = RequestMethod.GET)
    public ModelAndView getSuccessPage(@RequestParam(value = "referrerMobile") String referrerMobile,
                                        @RequestParam(value = "mobile",required = false) String mobile) {
        UserModel referrer = userMapper.findByMobile(referrerMobile);
        ModelAndView modelAndView = new ModelAndView();
        if (null == referrer) {
            modelAndView.setViewName("/error/error-info-page");
            modelAndView.addObject("errorInfo", "无效推荐链接");
            return modelAndView;
        }
        UserModel userModel = userMapper.findByMobile(mobile);
        boolean isOldUser = userModel != null && !"shareAB".equals(userModel.getChannel()) && prepareUserMapper.findByMobile(mobile) == null;
        modelAndView.addObject("isOldUser",isOldUser);
        modelAndView.setViewName("/wechat/share-app");
        modelAndView.addObject("responsive", true);
        modelAndView.addObject("referrerInfo", getReferrerInfo(referrer));
        return modelAndView;
    }

    private String obtainPageByMobileClient(final String userAgent){
        List<String> applyDevice = Lists.newArrayList("MAC", "IPAD", "IPHONE");
        Integer index = Iterators.indexOf(applyDevice.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return userAgent.toUpperCase().indexOf(input) > -1;
            }
        });
        if(index >= 0){
            return "/wechat/share-app-ios";
        }

        return "/wechat/share-app-android";
    }

    private String getReferrerInfo(UserModel referrer) {
        if (!StringUtils.isEmpty(referrer.getUserName())) {
            return  StringUtils.leftPad(StringUtils.right(referrer.getUserName(),1),referrer.getUserName().length(),"*") ;
        } else {
            return referrer.getMobile().substring(0, 3) + "****" + referrer.getMobile().substring(7);
        }
    }
}
