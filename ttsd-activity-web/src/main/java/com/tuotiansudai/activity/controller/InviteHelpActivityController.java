package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.activity.service.ActivityWeChatDrawCouponService;
import com.tuotiansudai.activity.service.InviteHelpActivityService;
import com.tuotiansudai.enums.WeChatDrawCoupon;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public ModelAndView inviteHelp() {
        ModelAndView modelAndView = new ModelAndView("/activities/2018/invite-help", "responsive", false);
        String loginName = LoginUserInfo.getLoginName();
        if (loginName != null) {
            modelAndView.addObject("rewardRecords", inviteHelpActivityService.sendRewardRecord());
            modelAndView.addObject("everyoneHelp", inviteHelpActivityService.everyoneHelp(loginName));
        }
        return modelAndView;
    }

    @RequestMapping(value = "/my-invest/help", method = RequestMethod.GET)
    @ResponseBody
    public List<WeChatHelpModel> myInvestHelpList() {
        String loginName = LoginUserInfo.getLoginName();
        if (loginName != null) {
            return inviteHelpActivityService.myInvestHelp(loginName);
        }
        return null;
    }

    @RequestMapping(value = "/{id:^\\d+$}/invest/help", method = RequestMethod.GET)
    public ModelAndView inviteHelpDetail(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/activities/2018/invite-help-detail", "responsive", false);
        String loginName = LoginUserInfo.getLoginName();
        if (loginName != null) {
            modelAndView.addAllObjects(inviteHelpActivityService.investHelpDetail(id, loginName));
            modelAndView.addObject("userMobile", LoginUserInfo.getMobile());
        }
        return modelAndView;
    }

    @RequestMapping(path = "/create/everyone/help", method = RequestMethod.GET)
    @ResponseBody
    public Long createEveryoneHelp(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (loginName != null || openId != null) {
            return inviteHelpActivityService.createEveryoneHelp(loginName, LoginUserInfo.getMobile(), openId);
        }
        return null;
    }

    @RequestMapping(path = "/everyone/help/detail", method = RequestMethod.GET)
    public ModelAndView everyoneHelpDetail(@PathVariable long id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/activities/2018/everyone-help-detail", "responsive", false);
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        String loginName = LoginUserInfo.getLoginName();
        if (loginName != null || openId != null) {
            modelAndView.addAllObjects(inviteHelpActivityService.everyoneHelpDetail(loginName, openId));
        }
        return modelAndView;
    }

    @RequestMapping(path = "/wechat/{id:^\\d+$}/invest/help/", method = RequestMethod.GET)
    public ModelAndView wechatInvestHelpDetail(@PathVariable long id, HttpServletRequest request){
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)){
            return new ModelAndView("redirect:/activity/invite-help");
        }

        if (inviteHelpActivityService.isOwnHelp(LoginUserInfo.getLoginName(), openId, id)){
            return new ModelAndView(String.format("redirect:/activity/invite-help/%s/invest/help", id));
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/invite-help-detail");
        modelAndView.addAllObjects(inviteHelpActivityService.weChatInvestHelpDetail(id, openId));
        return modelAndView;
    }

    @RequestMapping(path = "/wechat/{id:^\\d+$}/everyone/help/", method = RequestMethod.GET)
    public ModelAndView wechatEveryoneHelpDetail(@PathVariable long id, HttpServletRequest request){
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)){
            return new ModelAndView("redirect:/activity/invite-help");
        }

        if (inviteHelpActivityService.isOwnHelp(LoginUserInfo.getLoginName(), openId, id)){
            return new ModelAndView("redirect:/activity/invite-help/everyone/help/detail");
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/everyone-help-detail");
        modelAndView.addAllObjects(inviteHelpActivityService.weChatEveryoneHelpDetail(id, openId));
        return modelAndView;
    }

    @RequestMapping(path = "/click-help/{id:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public boolean clickHelp(@PathVariable long id, HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        return !inviteHelpActivityService.isOwnHelp(LoginUserInfo.getLoginName(), openId, id) && !Strings.isNullOrEmpty(openId) && inviteHelpActivityService.clickHelp(id, openId);
    }

    @RequestMapping(path = "/wechat/{id:^\\d+$}/withdraw", method = RequestMethod.GET)
    public ModelAndView wechatWithDraw(@PathVariable long id, HttpServletRequest request){
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/invite-help/everyone/help/detail");
        }

        String loginName = LoginUserInfo.getLoginName();
        if (Strings.isNullOrEmpty(loginName)) {
            request.getSession().setAttribute("channel", "MayActivity");
            return new ModelAndView("redirect:/we-chat/entry-point?redirect=/activity/invite-help/wechat/withdraw");
        }
        inviteHelpActivityService.updateEveryOneHelp(id, loginName, LoginUserInfo.getMobile());
        return new ModelAndView("redirect:/account");
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
