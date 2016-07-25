package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.RandomUtils;
import com.tuotiansudai.web.config.security.LoginUserInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private LoanRepayService loanRepayService;

    @Autowired
    private InvestRepayService investRepayService;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private SignInService signInService;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserMapper userMapper;
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView account() {
        ModelAndView modelAndView = new ModelAndView("/account");
        Date startTime = new DateTime().dayOfMonth().withMinimumValue().toDate();

        Date endTime = DateUtils.addMonths(startTime, 1);
        String loginName = LoginUserInfo.getLoginName();
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        modelAndView.addObject("mobile",userMapper.findUsersMobileByLoginName(loginName));
        modelAndView.addObject("userMembershipLevel", membershipModel != null?membershipModel.getLevel():0);
        modelAndView.addObject("balance",accountService.getBalance(loginName));
        modelAndView.addObject("collectedReward", userBillService.findSumRewardByLoginName(loginName));
        modelAndView.addObject("collectingPrincipal",investRepayService.findSumRepayingCorpusByLoginName(loginName));
        modelAndView.addObject("collectingInterest",investRepayService.findSumRepayingInterestByLoginName(loginName));
        modelAndView.addObject("collectedInterest",investRepayService.findSumRepaidInterestByLoginName(loginName));
        modelAndView.addObject("collectedBirthdayAndInterest",userCouponService.findSumBirthdayAndInterestByLoginName(loginName));
        modelAndView.addObject("collectedRedEnvelopeInterest",userCouponService.findSumRedEnvelopeByLoginName(loginName));

        modelAndView.addObject("freeze",accountService.getFreeze(loginName));
        if (userRoleService.judgeUserRoleExist(loginName, Role.LOANER)){
            modelAndView.addObject("successSumRepay",loanRepayService.findByLoginNameAndTimeSuccessRepay(loginName,startTime,endTime));
            modelAndView.addObject("repayList", loanRepayService.findLoanRepayInAccount(loginName, startTime, endTime, 0, 6));
        }
        modelAndView.addObject("successSumInvestRepay", investRepayService.findByLoginNameAndTimeAndSuccessInvestRepay(loginName, startTime, endTime));
        modelAndView.addObject("successSumInvestRepayList", investRepayService.findByLoginNameAndTimeSuccessInvestRepayList(loginName, startTime, endTime, 0, 6));
        modelAndView.addObject("notSuccessSumInvestRepay", investRepayService.findByLoginNameAndTimeAndNotSuccessInvestRepay(loginName, startTime, endTime));
        modelAndView.addObject("notSuccessSumInvestRepayList", investRepayService.findByLoginNameAndTimeNotSuccessInvestRepayList(loginName, startTime, endTime, 0, 6));
        modelAndView.addObject("latestInvestList", investRepayService.findLatestInvestByLoginName(loginName, 0, 6));
        modelAndView.addObject("signedIn",signInService.signInIsSuccess(loginName));
        modelAndView.addObject("myPoint", pointService.getAvailablePoint(loginName));
        modelAndView.addObject("isUsableCouponExist", userCouponService.isUsableUserCouponExist(loginName));
        return modelAndView;
    }
}
