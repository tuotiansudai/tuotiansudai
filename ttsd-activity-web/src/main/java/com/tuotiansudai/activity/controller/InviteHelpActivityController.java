package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.ActivityWeChatDrawCouponService;
import com.tuotiansudai.activity.service.InviteHelpActivityService;
import com.tuotiansudai.enums.WeChatDrawCoupon;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/activity/invite-help")
public class InviteHelpActivityController {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private ActivityWeChatDrawCouponService activityWeChatService;

    @Autowired
    private InviteHelpActivityService inviteHelpActivityService;

    private String startTime = ETCDConfigReader.getReader().getValue("activity.invite.help.startTime");

    private String endTime = ETCDConfigReader.getReader().getValue("activity.invite.help.endTime");

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView inviteHelp(){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/invite-help", "responsive", false);
        String loginName = LoginUserInfo.getLoginName();
        if (loginName !=null){
            modelAndView.addAllObjects(inviteHelpActivityService.investHelp(loginName));
        }
        return modelAndView;
    }

    @RequestMapping(value = "/{id:^\\d+$}/invest/help", method = RequestMethod.GET)
    public ModelAndView inviteHelpDetail(@PathVariable long id){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/invite-help-detail", "responsive", false);
        String loginName = LoginUserInfo.getLoginName();
        if (loginName !=null){
            modelAndView.addAllObjects(inviteHelpActivityService.investHelp(loginName));
        }
        return modelAndView;
    }

    @RequestMapping(path = "/{id:^\\d+$}/right-away/help", method = RequestMethod.GET)
    public ModelAndView rightAwayHelp(@PathVariable long id, HttpServletRequest request){
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/invite-help");
        }
        return new ModelAndView("/activities/2017/invite-help", "responsive", false);

    }

    @RequestMapping(path = "/wechat", method = RequestMethod.GET)
    public ModelAndView startWorkActivityWechat(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/invite-help");
        }
        ModelAndView modelAndView = new ModelAndView("/wechat/invite-help");
        modelAndView.addObject("activityStartTime", startTime);
        modelAndView.addObject("activityEndTime", endTime);
        String loginName = LoginUserInfo.getLoginName();
        if (loginName != null) {
            modelAndView.addObject("drewCoupon", activityWeChatService.drewCoupon(loginName, WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT));
        }
        return modelAndView;
    }

    @RequestMapping(path = "/draw", method = RequestMethod.GET)
    public ModelAndView newYearActivityDrawCoupon(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/invite-help");
        }
        boolean duringActivities = activityWeChatService.duringActivities(WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT);
        if (!duringActivities) {
            return new ModelAndView("redirect:/activity/invite-help/wechat");
        }

        String loginName = LoginUserInfo.getLoginName();
        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView("redirect:/we-chat/entry-point?redirect=/activity/invite-help/draw");
        }
        if (!weChatService.isBound(loginName)) {
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/invite-help");
        boolean drewCoupon = activityWeChatService.drewCoupon(loginName, WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT);
        modelAndView.addObject("activityStartTime", startTime);
        modelAndView.addObject("activityEndTime", endTime);
        if (!drewCoupon) {
            activityWeChatService.sendDrawCouponMessage(loginName, WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT);
            modelAndView.addObject("drawSuccess", true);
        }
        modelAndView.addObject("drewCoupon", true);
        return modelAndView;
    }
}
