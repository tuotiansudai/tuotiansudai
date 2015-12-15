package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.freemarker.directive.AmountDirective;
import com.tuotiansudai.web.freemarker.directive.PercentFractionDirective;
import com.tuotiansudai.web.freemarker.directive.PercentIntegerDirective;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String KEYTEMPLATE = "web:{0}:showCoupon";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index", "responsive", true);
        List<HomeLoanDto> loans = homeService.getLoans();
        int userCount = userService.findUserCount();
        modelAndView.addObject("loans", loans);
        modelAndView.addObject("percentFraction",new PercentFractionDirective());
        modelAndView.addObject("percentInteger",new PercentIntegerDirective());
        modelAndView.addObject("amount",new AmountDirective());
        modelAndView.addObject("userCount",userCount);
        boolean showCoupon = false;
        if (StringUtils.isNotEmpty(LoginUserInfo.getLoginName())
                && CollectionUtils.isNotEmpty(userCouponService.getUserCouponDtoByLoginName(LoginUserInfo.getLoginName()))
                && !redisWrapperClient.exists(MessageFormat.format(KEYTEMPLATE, LoginUserInfo.getLoginName()))) {
            showCoupon = true;
            redisWrapperClient.set(MessageFormat.format(KEYTEMPLATE, LoginUserInfo.getLoginName()),"show");
            UserCouponDto userCouponDto = userCouponService.getUserCouponDtoByLoginName(LoginUserInfo.getLoginName()).get(0);
            modelAndView.addObject("amountCoupon",userCouponDto.getAmount());
            modelAndView.addObject("endTimeCoupon",userCouponDto.getEndTime());
            modelAndView.addObject("nameCoupon",userCouponDto.getName());
        }
        modelAndView.addObject("showCoupon",showCoupon);
        return modelAndView;
    }

}
