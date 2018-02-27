package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.repository.mapper.UserFundMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserFundView;
import com.tuotiansudai.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/account-anonymous")
public class AccountAnonymousController {


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView anonymousAccount() {
        return new ModelAndView("/account-anonymous");
    }
}
