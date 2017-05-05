package com.tuotiansudai.activity.controller;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.service.RedEnvelopSplitActivityService;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;


public class RedEnvelopSplitActivityController {

    private final static Logger logger = Logger.getLogger(RedEnvelopSplitActivityController.class);

    @Autowired
    private RedEnvelopSplitActivityService redEnvelopSplitActivityService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private static final Long WEIXIN_REFERRER_COUPON_ID = 339L;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-split", "responsive", true);
        int referrerCount = redEnvelopSplitActivityService.getReferrerCount(loginName);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("referrerCount", referrerCount);
        modelAndView.addObject("redEnvelopAmount", redEnvelopSplitActivityService.getReferrerRedEnvelop(referrerCount));
        modelAndView.addObject("referrerUrl", redEnvelopSplitActivityService.getShareReferrerUrl(loginName));
        modelAndView.addObject("referrerList", redEnvelopSplitActivityService.getReferrerList(loginName));
        return modelAndView;
    }

    @RequestMapping(value = "/referrer", method = RequestMethod.GET)
    public ModelAndView referrer(@RequestParam(defaultValue = "微信红包", value = "loginName", required = false) String loginName,
                                 @RequestParam(defaultValue = "WX_FRIEND", value = "channel", required = false) UserChannel channel) {
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-referrer", "responsive", true);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("channels", channel);
        UserModel userModel = userMapper.findByLoginName(loginName);
        modelAndView.addObject("userName", userModel != null && !Strings.isNullOrEmpty(userModel.getUserName()) ? userModel.getUserName() : (userModel != null && !Strings.isNullOrEmpty(userModel.getMobile()) ? MobileEncryptor.encryptMiddleMobile(userModel.getMobile()) : loginName));
        modelAndView.addObject("registerStatus", "referrer");
        return modelAndView;
    }

    @RequestMapping(value = "/before-register", method = RequestMethod.POST)
    public ModelAndView beforeRegister(@RequestParam(value = "loginName", required = false) String loginName,
                                       @RequestParam(value = "mobile", required = false) String mobile,
                                       @RequestParam(value = "channel", required = false) UserChannel channel) {
        redEnvelopSplitActivityService.beforeRegisterUser(loginName, mobile, channel);
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-referrer", "responsive", true);
        modelAndView.addObject("registerStatus", "before");
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("channels", channel);
        modelAndView.addObject("userName", "");
        return modelAndView;
    }

    @RequestMapping(value = "/user-register", method = RequestMethod.POST)
    @ResponseBody
    public boolean userRegister(@RequestParam(value = "referrer", required = false) String loginName,
                                @RequestParam(value = "captcha", required = false) String captcha,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "channel", required = false) String channel) {

        boolean isRegisterSuccess;
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setMobile(mobile);
        registerUserDto.setChannel(channel);
        registerUserDto.setReferrer(loginName);
        registerUserDto.setCaptcha(captcha);
        registerUserDto.setPassword(password);
        logger.info(MessageFormat.format("[Register User {0}] controller starting...", registerUserDto.getMobile()));
        isRegisterSuccess = this.userService.registerUser(registerUserDto);
        logger.info(MessageFormat.format("[Register User {0}] controller invoked service ({0})", registerUserDto.getMobile(), String.valueOf(isRegisterSuccess)));


        if (isRegisterSuccess) {
            logger.info(MessageFormat.format("[Register User {0}] authenticate starting...", registerUserDto.getMobile()));
            myAuthenticationUtil.createAuthentication(registerUserDto.getMobile(), Source.WEB);
            logger.info(MessageFormat.format("[Register User {0}] authenticate completed", registerUserDto.getMobile()));

            if (!Strings.isNullOrEmpty(registerUserDto.getChannel()) && Lists.newArrayList(UserChannel.values()).contains(UserChannel.valueOf(registerUserDto.getChannel()))) {
                logger.info(MessageFormat.format("[Register User {0}] assign weiXin referrer 8.88 red envelop ", registerUserDto.getMobile()));
                couponAssignmentService.assignUserCoupon(registerUserDto.getMobile(), WEIXIN_REFERRER_COUPON_ID);
            }
        }
        return isRegisterSuccess;
    }


}
