package com.tuotiansudai.web.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;

@Controller
@RequestMapping
public class UserCouponController {

    static Logger logger = Logger.getLogger(UserCouponController.class);

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @RequestMapping(value = "/assign-coupon", method = RequestMethod.POST)
    @ResponseBody
    public boolean assignUserCoupon() {
        String loginName = LoginUserInfo.getLoginName();
        logger.info(MessageFormat.format("pc assign coupon user:{0},begin time:{1}", loginName, DateTime.now().toString()));
        couponAssignmentService.asyncAssignUserCoupon(loginName, Lists.newArrayList(
                UserGroup.ALL_USER,
                UserGroup.INVESTED_USER,
                UserGroup.REGISTERED_NOT_INVESTED_USER,
                UserGroup.NOT_ACCOUNT_NOT_INVESTED_USER,
                UserGroup.STAFF,
                UserGroup.STAFF_RECOMMEND_LEVEL_ONE,
                UserGroup.AGENT,
                UserGroup.CHANNEL,
                UserGroup.NEW_REGISTERED_USER,
                UserGroup.IMPORT_USER,
                UserGroup.MEMBERSHIP_V0,
                UserGroup.MEMBERSHIP_V1,
                UserGroup.MEMBERSHIP_V2,
                UserGroup.MEMBERSHIP_V3,
                UserGroup.MEMBERSHIP_V4,
                UserGroup.MEMBERSHIP_V5));
        logger.info(MessageFormat.format("pc assign coupon user:{0},end time:{1}", loginName, DateTime.now().toString()));
        return true;
    }
}
