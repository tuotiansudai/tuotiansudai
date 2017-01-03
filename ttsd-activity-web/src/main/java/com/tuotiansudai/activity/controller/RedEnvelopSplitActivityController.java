package com.tuotiansudai.activity.controller;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.service.RedEnvelopSplitActivityService;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/activity/red-envelop-split")
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
        modelAndView.addObject("referrerCount", redEnvelopSplitActivityService.getReferrerCount(loginName));
        modelAndView.addObject("redEnvelopAmount", redEnvelopSplitActivityService.getReferrerRedEnvelop(loginName));
        modelAndView.addObject("referrerUrl", redEnvelopSplitActivityService.getShareReferrerUrl(loginName));
        return modelAndView;
    }

    @RequestMapping(value = "/referrer", method = RequestMethod.GET)
    public ModelAndView referrer(@RequestParam(value = "loginName", required = false) String loginName,
                                 @RequestParam(value = "channel", required = false) String channel) {
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-referrer", "responsive", true);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("channels", channel);
        UserModel userModel = userMapper.findByLoginName(loginName);
        modelAndView.addObject("userName", userModel != null ? userModel.getUserName() : null);
        modelAndView.addObject("registerStatus", "referrer");
        return modelAndView;
    }

    @RequestMapping(value = "/before-register", method = RequestMethod.POST)
    public ModelAndView beforeRegister(@RequestParam(value = "loginName", required = false) String loginName,
                                 @RequestParam(value = "mobile", required = false) String mobile,
                                 @RequestParam(value = "channel", required = false) String channel) {
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

        boolean isRegisterSuccess = false;
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setMobile(mobile);
        registerUserDto.setChannel(channel);
        registerUserDto.setReferrer(loginName);
        registerUserDto.setCaptcha(captcha);
        registerUserDto.setPassword(password);
        try {
            logger.info(MessageFormat.format("[Register User {0}] controller starting...", registerUserDto.getMobile()));
            isRegisterSuccess = this.userService.registerUser(registerUserDto);
            logger.info(MessageFormat.format("[Register User {0}] controller invoked service ({0})", registerUserDto.getMobile(), String.valueOf(isRegisterSuccess)));
        } catch (ReferrerRelationException e) {
            logger.error(e.getLocalizedMessage(), e);
        }


        if (isRegisterSuccess) {
            logger.info(MessageFormat.format("[Register User {0}] authenticate starting...", registerUserDto.getMobile()));
            myAuthenticationUtil.createAuthentication(registerUserDto.getMobile(), Source.WEB);
            logger.info(MessageFormat.format("[Register User {0}] authenticate completed", registerUserDto.getMobile()));

            if(!Strings.isNullOrEmpty(registerUserDto.getChannel()) && Lists.newArrayList(UserChannel.values()).contains(UserChannel.valueOf(registerUserDto.getChannel()))){
                logger.info(MessageFormat.format("[Register User {0}] assign weiXin referrer 8.88 red envelop ",registerUserDto.getMobile()));
                couponAssignmentService.assignUserCoupon(registerUserDto.getMobile(), WEIXIN_REFERRER_COUPON_ID);
            }
        }
        return isRegisterSuccess;
    }

}
