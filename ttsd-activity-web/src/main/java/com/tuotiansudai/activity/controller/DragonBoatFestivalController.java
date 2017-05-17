package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.DragonBoatFestivalService;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@Component
@RequestMapping(value = "/activity/wechat/dragon")
public class DragonBoatFestivalController {

    public static final Logger logger = LoggerFactory.getLogger(DragonBoatFestivalController.class);

    @Autowired
    private DragonBoatFestivalService dragonBoatFestivalService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;


    // 微信公众号回复打卡页面
    @RequestMapping(value = "/punchCard", method = RequestMethod.GET)
    public ModelAndView wechatFirstPage() {
        String loginName = LoginUserInfo.getLoginName();

        String resStr = dragonBoatFestivalService.getCouponExchangeCode(loginName);

        String exchangeCode = resStr == null ? null : resStr.split(":")[0];
        String shareUniqueCode = resStr == null ? null : resStr.split(":")[1];

        ModelAndView mav = new ModelAndView("/wechat/dragon-share");
        mav.addObject("exchangeCode", exchangeCode); // null 表示活动未开始或已结束，或者用户未登录
        mav.addObject("loginName", loginName);
        mav.addObject("shareUniqueCode", shareUniqueCode);
        return mav;
    }

    // 分享落地页
    @RequestMapping(value = "/shareLanding", method = RequestMethod.GET)
    public ModelAndView shareLanding(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String sharer = request.getParameter("sharer");
        String shareUniqueCode = request.getParameter("shareUniqueCode");

        ModelAndView mav = new ModelAndView("/wechat/dragon-invite");
        mav.addObject("loginName", loginName);
        mav.addObject("sharer", sharer);
        mav.addObject("shareUniqueCode", shareUniqueCode);
        return mav;
    }

    // 登录页面
    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public ModelAndView toLogin(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String sharer = request.getParameter("sharer");
        String shareUniqueCode = request.getParameter("shareUniqueCode");

        ModelAndView mav = new ModelAndView("/wechat/dragon-login");
        mav.addObject("loginName", loginName);
        mav.addObject("sharer", sharer);
        mav.addObject("shareUniqueCode", shareUniqueCode);
        return mav;
    }

    // 注册页面
    @RequestMapping(value = "/toRegister", method = RequestMethod.GET)
    public ModelAndView toRegister(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String sharer = request.getParameter("sharer");
        String shareUniqueCode = request.getParameter("shareUniqueCode");

        ModelAndView mav = new ModelAndView("/wechat/dragon-register");
        mav.addObject("loginName", loginName);
        mav.addObject("sharer", sharer);
        mav.addObject("shareUniqueCode", shareUniqueCode);
        return mav;
    }

    // 新用户注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public boolean userRegister(@RequestParam(value = "referrer", required = false) String referrer,
                                @RequestParam(value = "captcha", required = false) String captcha,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "channel", required = false) String channel,
                                @RequestParam(value = "shareUniqueCode", required = false) String shareUniqueCode) {

        boolean isRegisterSuccess;
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setMobile(mobile);
        registerUserDto.setChannel(channel);
        registerUserDto.setReferrer(referrer);
        registerUserDto.setCaptcha(captcha);
        registerUserDto.setPassword(password);
        logger.info(MessageFormat.format("[Dragon Boat Register User {}] controller starting...", registerUserDto.getMobile()));
        isRegisterSuccess = this.userService.registerUser(registerUserDto);
        logger.info(MessageFormat.format("[Dragon Boat Register User {}] controller invoked service ({})", registerUserDto.getMobile(), String.valueOf(isRegisterSuccess)));

        if (isRegisterSuccess) {
            logger.info(MessageFormat.format("[Dragon Boat Register User {}] authenticate starting...", registerUserDto.getMobile()));
            myAuthenticationUtil.createAuthentication(registerUserDto.getMobile(), Source.WEB);

            dragonBoatFestivalService.afterNewUserRegister(registerUserDto.getMobile(), referrer);
        }
        return isRegisterSuccess;
    }

    // 登录或注册后，领取红包
    @RequestMapping(value = "/fetchCoupon", method = RequestMethod.GET)
    public ModelAndView fetchCoupon(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String shareUniqueCode = request.getParameter("shareUniqueCode");
        ModelAndView mav = new ModelAndView("/wechat/dragon-success");

        if (dragonBoatFestivalService.sendCouponAfterRegisterOrLogin(loginName, shareUniqueCode)) {
            mav.addObject("hasCoupon", "0");
        } else {
            mav.addObject("hasCoupon", "1");
        }
        return mav;
    }

    // pc 活动页
    @RequestMapping(value = "/pcLanding", method = RequestMethod.GET)
    public ModelAndView pcLandingPage() {
        String loginName = LoginUserInfo.getLoginName();
        String supportGroup = dragonBoatFestivalService.getGroupByLoginName(loginName);
        long sweetAmount = dragonBoatFestivalService.getGroupInvestAmount("SWEET");
        long saltyAmount = dragonBoatFestivalService.getGroupInvestAmount("SALTY");

        ModelAndView mav = new ModelAndView("/pc-landing");
        mav.addObject("supportGroup", supportGroup);
        mav.addObject("sweetAmount", sweetAmount);
        mav.addObject("saltyAmount", saltyAmount);
        return mav;
    }

    // 加入甜/咸粽派 PK
    @RequestMapping(value = "/joinPK", method = RequestMethod.POST)
    @ResponseBody
    public boolean joinPK(@RequestParam(value = "group") String group) {
        String loginName = LoginUserInfo.getLoginName();
        return dragonBoatFestivalService.joinPK(loginName, group);
    }

}
