package com.tuotiansudai.web.controller;

import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.MembershipExperienceBillService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/membership")
public class MembershipController {

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MembershipExperienceBillService membershipExperienceBillService;

    @Autowired
    private UserMembershipService userMembershipService;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/membership-index");

        String loginName = LoginUserInfo.getLoginName();
        if(loginName != null){
            MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
            MembershipModel NextLevelMembershipModel = userMembershipService.getMembershipByLevel(membershipModel.getLevel() + 1);
            AccountModel accountModel = accountService.findByLoginName(loginName);

            modelAndView.addObject("membershipLevel", membershipModel != null?membershipModel.getLevel():"");
            modelAndView.addObject("membershipNextLevel", NextLevelMembershipModel != null?NextLevelMembershipModel.getLevel():"");
            modelAndView.addObject("membershipNextLevelValue", NextLevelMembershipModel != null?(NextLevelMembershipModel.getExperience() - accountModel.getMembershipPoint()):"");
            modelAndView.addObject("membershipPoint", accountModel != null?accountModel.getMembershipPoint():"");
            modelAndView.addObject("progressBarPercent", userMembershipService.getProgressBarPercent(loginName));
            modelAndView.addObject("privilegeList", userMembershipService.getPrivilege(loginName));
            modelAndView.addObject("privilegeShow", userMembershipService.showDisable(loginName));
            //modelAndView.addObject("expireDay",);
        }
        modelAndView.addObject("loginName", loginName);

        return modelAndView;

    }

    @RequestMapping(path = "/structure", method = RequestMethod.GET)
    public ModelAndView structure() {
        ModelAndView modelAndView = new ModelAndView("/membership-structure");

        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);

        modelAndView.addObject("membershipLevel", membershipModel != null?membershipModel.getLevel():"");
        modelAndView.addObject("membershipPoint", accountModel != null?accountModel.getMembershipPoint():"");

        return modelAndView;
    }

    @RequestMapping(path = "/privilege", method = RequestMethod.GET)
    public ModelAndView privilege() {
        ModelAndView modelAndView = new ModelAndView("/membership-privilege");

        return modelAndView;
    }
}
