package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-manage", method = RequestMethod.GET)
public class CouponController {

    @RequestMapping(value = "/coupon",method = RequestMethod.GET)
    public ModelAndView coupon(){
        return new ModelAndView("/coupon");
    }

    @RequestMapping(value = "/coupon",method = RequestMethod.POST)
    public ModelAndView createCoupon(){
        return new ModelAndView("/coupon");
    }


}
