package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.exception.CreateCouponException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/activity-manage")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @RequestMapping(value = "/coupon",method = RequestMethod.GET)
    public ModelAndView coupon(){
        return new ModelAndView("/coupon");
    }

    @RequestMapping(value = "/coupon",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createCoupon(@ModelAttribute CouponDto couponDto,RedirectAttributes redirectAttributes){
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        try {
            couponService.createCoupon(loginName, couponDto);
            modelAndView.setViewName("redirect:/activity-manage/coupons");
            return modelAndView;
        } catch (CreateCouponException e) {
            modelAndView.setViewName("redirect:/activity-manage/coupon");
            redirectAttributes.addFlashAttribute("coupon", couponDto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return modelAndView;
        }

    }


    @RequestMapping(value = "/coupon/{couponId}/active",method = RequestMethod.POST)
    @ResponseBody
    public String activeCoupon(@PathVariable String couponId){
        String loginName = LoginUserInfo.getLoginName();

        return "ok";
    }




}
