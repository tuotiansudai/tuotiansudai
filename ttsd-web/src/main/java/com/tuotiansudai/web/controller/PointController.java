package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/point")
public class PointController {
    @Autowired
    private SignInService signInService;
    @Autowired
    private PointService pointService;

    @Autowired
    private CouponService couponService;

    @RequestMapping(path = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> signIn() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = signInService.signIn(loginName);
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView myPoint(){
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/point");

        
        return modelAndView;

    }

    @RequestMapping(value = "/exchange_coupon_list", method = RequestMethod.GET)
    public ModelAndView exchangeCouponList(){
        ModelAndView modelAndView = new ModelAndView("/point_exchange_list");
        return modelAndView;

    }


}
