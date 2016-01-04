package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.AnnounceService;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.service.UserService;
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

    @Autowired
    private AnnounceService announceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index", "responsive", true);
        List<HomeLoanDto> loans = homeService.getLoans();
        BaseDto<BasePaginationDataDto> baseDto = announceService.getAnnouncementList(1, 3);
        int userCount = userService.findUserCount();
        modelAndView.addObject("loans", loans);
        modelAndView.addObject("announces", baseDto.getData().getRecords());
        modelAndView.addObject("userCount", userCount);
        boolean showCoupon = false;
        if (StringUtils.isNotEmpty(LoginUserInfo.getLoginName())
                && CollectionUtils.isNotEmpty(userCouponService.getUserCouponDtoByLoginName(LoginUserInfo.getLoginName()))
                && !redisWrapperClient.exists(MessageFormat.format(KEYTEMPLATE, LoginUserInfo.getLoginName()))) {
            showCoupon = true;
            redisWrapperClient.set(MessageFormat.format(KEYTEMPLATE, LoginUserInfo.getLoginName()), "show");
            UserCouponDto userCouponDto = userCouponService.getUserCouponDtoByLoginName(LoginUserInfo.getLoginName()).get(0);
            modelAndView.addObject("amountCoupon", userCouponDto.getAmount());
            modelAndView.addObject("endTimeCoupon", userCouponDto.getEndTime());
            modelAndView.addObject("nameCoupon", userCouponDto.getName());
        }
        modelAndView.addObject("showCoupon", showCoupon);
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        return modelAndView;
    }
}
