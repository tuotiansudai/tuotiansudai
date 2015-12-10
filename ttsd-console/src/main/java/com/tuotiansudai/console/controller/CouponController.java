package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-manage", method = RequestMethod.GET)
public class CouponController {
    @Autowired
    private CouponService couponService;
    @RequestMapping(value = "/coupon",method = RequestMethod.GET)
    public ModelAndView coupon(){
        return new ModelAndView("/coupon");
    }

    @RequestMapping(value = "/coupon",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> createCoupon(@RequestBody CouponDto couponDto){
        String loginName = LoginUserInfo.getLoginName();
        return couponService.createCoupon(loginName,couponDto);
    }


    @RequestMapping(value = "/coupon/{couponId}/active",method = RequestMethod.POST)
    @ResponseBody
    public String activeCoupon(@PathVariable String couponId){
        String loginName = LoginUserInfo.getLoginName();

        return "ok";
    }




}
