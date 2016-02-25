package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/point-manage")
public class PointController {

    @Autowired
    private PointService pointService;

    @RequestMapping(value = "/coupon-exchange", method = RequestMethod.GET)
    public ModelAndView couponExchange() {
        ModelAndView modelAndView = new ModelAndView("/coupon-exchange");
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("couponTypes", Lists.newArrayList(CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON));
        return modelAndView;
    }

    @RequestMapping(value = "/coupon-exchange", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createCouponExchange(@Valid @ModelAttribute ExchangeCouponDto exchangeCouponDto, RedirectAttributes redirectAttributes) {

        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/coupon-exchanges");
        Long id = exchangeCouponDto.getId();
        if (id != null) {
            //todo edit
        } else {
            pointService.createCouponAndExchange(loginName, exchangeCouponDto);
        }
        return modelAndView;
    }

}
