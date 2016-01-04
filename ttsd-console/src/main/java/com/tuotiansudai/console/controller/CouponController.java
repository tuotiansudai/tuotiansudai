package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/activity-manage")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponActivationService couponActivationService;

    @RequestMapping(value = "/coupon",method = RequestMethod.GET)
    public ModelAndView coupon(){
        ModelAndView modelAndView = new  ModelAndView("/coupon");
        modelAndView.addObject("couponTypes", Lists.newArrayList(CouponType.values()));
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        return modelAndView;
    }

    @RequestMapping(value = "/coupon",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createCoupon(@Valid @ModelAttribute CouponDto couponDto,RedirectAttributes redirectAttributes){
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

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/active",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> activeCoupon(@PathVariable long couponId){
        BaseDataDto dataDto = new BaseDataDto();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        String loginName = LoginUserInfo.getLoginName();
        couponActivationService.active(loginName, couponId);
        return baseDto;
    }

    @RequestMapping(value = "/coupons",method = RequestMethod.GET)
    public ModelAndView coupons(@RequestParam(value = "index",required = false,defaultValue = "1") int index,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/coupons");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("coupons", couponService.findCoupons(index, pageSize));
        int couponsCount = couponService.findCouponsCount();
        modelAndView.addObject("couponsCount", couponsCount);
        long totalPages = couponsCount / pageSize + (couponsCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        return modelAndView;
    }

    @RequestMapping(value = "/coupon/detail", method = RequestMethod.GET)
    public ModelAndView couponDetail(@RequestParam(value = "couponId") long couponId,
                                      @RequestParam(value = "isUsed",required = false) Boolean isUsed) {
        ModelAndView modelAndView = new ModelAndView("/coupon-detail");
        List<UserCouponModel> userCouponModels = couponService.findCouponDetail(couponId, isUsed);
        modelAndView.addObject("userCouponModels", userCouponModels);
        modelAndView.addObject("isUsed", isUsed);
        return modelAndView;
    }

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> couponDetele(@PathVariable long couponId) {
        BaseDataDto dataDto = new BaseDataDto();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        String loginName = LoginUserInfo.getLoginName();
        couponService.deleteCoupon(loginName, couponId, true);
        return baseDto;
    }

}
