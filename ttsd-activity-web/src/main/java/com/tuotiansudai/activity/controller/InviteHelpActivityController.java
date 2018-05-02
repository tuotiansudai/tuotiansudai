package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        ModelAndView modelAndView = new ModelAndView("/activities/2018/rebate-station", "responsive", false);
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("investHelp", inviteHelpActivityService.myInvestHelp(loginName));
        modelAndView.addObject("everyoneHelp", inviteHelpActivityService.everyoneHelp(loginName));
        modelAndView.addObject("rewardRecords", inviteHelpActivityService.sendRewardRecord());
        modelAndView.addObject("activityStartTime", startTime);
        modelAndView.addObject("activityEndTime", endTime);
        boolean existOwnHelp = false;
        if (loginName !=null){
            Map<String, Object> ownHelpModel = inviteHelpActivityService.findOwnEveryoneHelp(loginName, null);
            existOwnHelp = ownHelpModel.containsKey("helpModel");
        }
        modelAndView.addObject("existOwnHelp", existOwnHelp);
        return modelAndView;
    }

    @RequestMapping(value = "/{id:^\\d+$}/invest/help", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> inviteHelpDetail(@PathVariable long id) {
        WeChatHelpModel weChatHelpModel = inviteHelpActivityService.getHelpModel(id);
        return inviteHelpActivityService.investHelpDetail(weChatHelpModel);
    }

    @RequestMapping(value = "/wechat/{id:^\\d+$}/invest/help", method = RequestMethod.GET)
    public ModelAndView wechatInviteHelpDetail(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/wechat/invite_friends_sharing");
        String loginName = LoginUserInfo.getLoginName();
        WeChatHelpModel weChatHelpModel = inviteHelpActivityService.getHelpModel(id);
        Map<String, Object> map = inviteHelpActivityService.investHelpDetail(weChatHelpModel);
        if (map.isEmpty()){
            return new ModelAndView("redirect:/activity/invite-help");
        }
        if (loginName == null || !weChatHelpModel.getLoginName().equals(loginName)){
            return new ModelAndView(String.format("redirect:/we-chat/active/authorize?redirect=/activity/invite-help/wechat/share/%s/invest/help", id));
        }
        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    @RequestMapping(path = "/everyone/help/detail", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> everyoneHelpDetail(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        String loginName = LoginUserInfo.getLoginName();
        if (loginName != null || openId != null) {
            return inviteHelpActivityService.everyoneHelpDetail(loginName, openId);
        }
        return null;
    }

    @RequestMapping(value = "/wechat/everyone/help/detail", method = RequestMethod.GET)
    public ModelAndView wechatEveryoneHelpDetail(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/wechat/everyone_receive_sharing");
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        String loginName = LoginUserInfo.getLoginName();
        if (loginName == null && openId == null){
            return new ModelAndView("redirect:/activity/invite-help");
        }
        modelAndView.addAllObjects(inviteHelpActivityService.everyoneHelpDetail(loginName, openId));
        return modelAndView;
    }

    @RequestMapping(path = "/wechat/share/{id:^\\d+$}/invest/help", method = RequestMethod.GET)
    public ModelAndView wechatShareInvestHelpDetail(@PathVariable long id, HttpServletRequest request){
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)){
            return new ModelAndView("redirect:/activity/invite-help");
        }

        if (inviteHelpActivityService.isOwnHelp(LoginUserInfo.getLoginName(), openId, id)){
            return new ModelAndView(String.format("redirect:/activity/invite-help/wechat/%s/invest/help", id));
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/invite_friends_shared");
        modelAndView.addObject("activityStartTime", startTime);
        modelAndView.addObject("activityEndTime", endTime);
        modelAndView.addAllObjects(inviteHelpActivityService.weChatInvestHelpDetail(id, openId));
        Map<String, Object> ownHelpModel = inviteHelpActivityService.findOwnEveryoneHelp(LoginUserInfo.getLoginName(), openId);
        modelAndView.addObject("existOwnHelp", ownHelpModel.containsKey("helpModel"));
        return modelAndView;
    }

    @RequestMapping(path = "/wechat/share/{id:^\\d+$}/everyone/help", method = RequestMethod.GET)
    public ModelAndView wechatShareEveryoneHelpDetail(@PathVariable long id, HttpServletRequest request){
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)){
            return new ModelAndView("redirect:/activity/invite-help");
        }

        if (inviteHelpActivityService.isOwnHelp(LoginUserInfo.getLoginName(), openId, id)){
            return new ModelAndView("redirect:/activity/invite-help/wechat/everyone/help/detail");
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/everyone_receive_shared");
        modelAndView.addObject("activityStartTime", startTime);
        modelAndView.addObject("activityEndTime", endTime);
        modelAndView.addAllObjects(inviteHelpActivityService.weChatEveryoneHelpDetail(id, openId));
        Map<String, Object> ownHelpModel = inviteHelpActivityService.findOwnEveryoneHelp(LoginUserInfo.getLoginName(), openId);
        modelAndView.addObject("existOwnHelp", ownHelpModel.containsKey("helpModel"));
        return modelAndView;
    }

    @RequestMapping(path = "/click-help/{id:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public boolean clickHelp(@PathVariable long id, HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        return !inviteHelpActivityService.isOwnHelp(LoginUserInfo.getLoginName(), openId, id) && !Strings.isNullOrEmpty(openId) && inviteHelpActivityService.clickHelp(id, openId);
    }

    @RequestMapping(path = "/{isOwn}/wechat/{id:^\\d+$}/withdraw", method = RequestMethod.GET)
    public ModelAndView wechatWithDraw(@PathVariable boolean isOwn, @PathVariable long id, HttpServletRequest request){
        String loginName = LoginUserInfo.getLoginName();
        if (Strings.isNullOrEmpty(loginName)) {
            request.getSession().setAttribute("channel", "fanlijiayouzhan");
            return new ModelAndView(String.format("redirect:/we-chat/entry-point?redirect=/activity/invite-help/%s/wechat/%s/withdraw", isOwn, id));
        }
        if (isOwn){
            inviteHelpActivityService.updateEveryOneHelp(id, loginName, LoginUserInfo.getMobile());
        }
        return new ModelAndView("redirect:/m/account");
    }

    @RequestMapping(path = "/wechat", method = RequestMethod.GET)
    public ModelAndView startWorkActivityWechat(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/invite-help");
        }
        ModelAndView modelAndView = new ModelAndView("/wechat/rebate-station-coupons");
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

        ModelAndView modelAndView = new ModelAndView("/wechat/rebate-station-coupons");
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
