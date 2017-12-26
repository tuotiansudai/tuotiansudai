package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.DragonBoatFestivalService;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
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

@Component
@RequestMapping(value = "/activity/dragon")
public class DragonBoatFestivalController {

    public static final Logger logger = LoggerFactory.getLogger(DragonBoatFestivalController.class);

    @Autowired
    private DragonBoatFestivalService dragonBoatFestivalService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyAuthenticationUtil myAuthenticationUtil;

    @Autowired
    private MQWrapperClient mqWrapperClient;


    // 微信公众号回复打卡页面
    @RequestMapping(value = "/wechat/punchCard", method = RequestMethod.GET)
    public ModelAndView wechatFirstPage() {
        String loginName = LoginUserInfo.getLoginName();

        String resStr = dragonBoatFestivalService.getCouponExchangeCode(loginName);

        String exchangeCode = resStr == null ? null : resStr.split(":")[0];
        String unique = resStr == null ? null : resStr.split(":")[1];

        ModelAndView mav = new ModelAndView("/wechat/dragon-share");
        mav.addObject("activityEnd", !dragonBoatFestivalService.inActivityPeriod());
        mav.addObject("exchangeCode", exchangeCode); // null 表示活动未开始或已结束，或者用户未登录
        mav.addObject("loginName", loginName);
        mav.addObject("unique", unique);
        return mav;
    }

    // 分享落地页
    @RequestMapping(value = "/wechat/shareLanding", method = RequestMethod.GET)
    public ModelAndView shareLanding(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String sharerUnique = request.getParameter("sharerUnique");

        ModelAndView mav = new ModelAndView("/wechat/dragon-invite");
        mav.addObject("loginName", loginName);
        mav.addObject("sharerUnique", sharerUnique);
        mav.addObject("activityEnd", !dragonBoatFestivalService.inActivityPeriod());
        return mav;
    }

    // 登录页面
    @RequestMapping(value = "/wechat/toLogin", method = RequestMethod.GET)
    public ModelAndView toLogin(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String sharerUnique = request.getParameter("sharerUnique");

        ModelAndView mav = new ModelAndView("/wechat/dragon-login");
        mav.addObject("loginName", loginName);
        mav.addObject("sharerUnique", sharerUnique);
        return mav;
    }

    // 注册页面
    @RequestMapping(value = "/wechat/toRegister", method = RequestMethod.GET)
    public ModelAndView toRegister(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String sharerUnique = request.getParameter("sharerUnique");
        String sharer = sharerUnique.split("-")[0];
        String unique = sharerUnique.split("-")[1];

        ModelAndView mav = new ModelAndView("/wechat/dragon-register");
        mav.addObject("loginName", loginName);
        mav.addObject("sharer", sharer);
        mav.addObject("unique", unique);
        return mav;
    }

    // 新用户注册
    @RequestMapping(value = "/wechat/register", method = RequestMethod.POST)
    public ModelAndView userRegister(@RequestParam(value = "referrer", required = false) String referrer,
                                     @RequestParam(value = "captcha", required = false) String captcha,
                                     @RequestParam(value = "password", required = false) String password,
                                     @RequestParam(value = "mobile", required = false) String mobile,
                                     @RequestParam(value = "unique", required = false) String unique) {

        boolean isRegisterSuccess;
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setMobile(mobile);
        registerUserDto.setChannel("wechat");
        registerUserDto.setReferrer(referrer);
        registerUserDto.setCaptcha(captcha);
        registerUserDto.setPassword(password);
        logger.info("[Dragon Boat Register User {}] controller starting...", registerUserDto.getMobile());
        isRegisterSuccess = this.userService.registerUser(registerUserDto);
        logger.info("[Dragon Boat Register User {}] controller invoked service ({})", registerUserDto.getMobile(), String.valueOf(isRegisterSuccess));

        if (isRegisterSuccess) {
            logger.info("[Dragon Boat Register User {}] authenticate starting...", registerUserDto.getMobile());
            myAuthenticationUtil.createAuthentication(registerUserDto.getMobile(), Source.WEB);

            dragonBoatFestivalService.afterNewUserRegister(registerUserDto.getMobile(), referrer);
        }

        return new ModelAndView("redirect:/activity/dragon/wechat/fetchCouponAfterRegister?unique=" + unique);
    }

    // 注册后，领取红包
    @RequestMapping(value = "/wechat/fetchCouponAfterRegister", method = RequestMethod.GET)
    public ModelAndView fetchCouponAfterRegister(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String unique = request.getParameter("unique");
        ModelAndView mav = new ModelAndView("/wechat/dragon-success");

        mav.addObject("fetchResult", dragonBoatFestivalService.sendCouponAfterRegisterOrLogin(loginName, unique));
        return mav;
    }

    // 登录后，领取红包
    @RequestMapping(value = "/wechat/fetchCouponAfterLogin", method = RequestMethod.GET)
    public ModelAndView fetchCouponAfterLogin(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String sharerUnique = request.getParameter("sharerUnique");
        String sharer = sharerUnique.split("-")[0];
        String unique = sharerUnique.split("-")[1];
        ModelAndView mav = new ModelAndView("/wechat/dragon-success");

        int ret = dragonBoatFestivalService.sendCouponAfterRegisterOrLogin(loginName, unique);
        mav.addObject("fetchResult", ret);
        return mav;
    }

    // pc 活动页
    @RequestMapping(value = "/pcLanding", method = RequestMethod.GET)
    public ModelAndView pcLandingPage() {
        String loginName = LoginUserInfo.getLoginName();
        String supportGroup = dragonBoatFestivalService.getGroupByLoginName(loginName);
        long sweetAmount = dragonBoatFestivalService.getGroupPKInvestAmount("SWEET");
        long saltyAmount = dragonBoatFestivalService.getGroupPKInvestAmount("SALTY");

        long sweetSupportCount = dragonBoatFestivalService.getGroupSupportCount("SWEET");
        long saltySupportCount = dragonBoatFestivalService.getGroupSupportCount("SALTY");

        int champagnePrizeLevel = dragonBoatFestivalService.getChampagnePrizeLevel(loginName);

        long investAmount = dragonBoatFestivalService.getActivityInvestAmount(loginName);

        ModelAndView mav = new ModelAndView("/activities/dragon-boat", "responsive", true);
        mav.addObject("supportGroup", supportGroup); // 如果没有登录或者没有选择过阵营，则为null
        mav.addObject("sweetAmount", sweetAmount);
        mav.addObject("saltyAmount", saltyAmount);
        mav.addObject("sweetSupportCount", sweetSupportCount);
        mav.addObject("saltySupportCount", saltySupportCount);
        mav.addObject("champagnePrizeLevel", champagnePrizeLevel);
        mav.addObject("investAmount", investAmount);
        return mav;
    }

    // 加入甜/咸粽派 PK，若已经选择过阵营，则返回已经加入的阵营名（"SWEET", "SALTY"），否则返回"SUCCESS"
    @RequestMapping(value = "/joinPK", method = RequestMethod.POST)
    @ResponseBody
    public String joinPK(@RequestParam(value = "group") String group) {
        String loginName = LoginUserInfo.getLoginName();
        return dragonBoatFestivalService.joinPK(loginName, group);
    }

}
