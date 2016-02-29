package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/point-manage")
public class PointController {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = "/user-point-list")
    public ModelAndView usersAccountPointList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                       @RequestParam(value = "loginName", required = false) String loginName,
                                       @RequestParam(value = "userName", required = false) String userName,
                                       @RequestParam(value = "mobile", required = false) String mobile){

            ModelAndView modelAndView = new ModelAndView("/user-point-list");
            modelAndView.addObject("index", index);
            modelAndView.addObject("pageSize", pageSize);
            List<UserItemDataDto> userItemDataDtoList = userService.findUsersAccountPoint(loginName, userName, mobile, index, pageSize);
            modelAndView.addObject("userPointList", userItemDataDtoList);
            int count = userService.findUsersAccountPointCount(loginName, userName, mobile);
            long totalPages = count / pageSize + (count % pageSize > 0 ? 1 : 0);
            boolean hasPreviousPage = index > 1 && index <= totalPages;
            boolean hasNextPage = index < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            modelAndView.addObject("count", count);
            modelAndView.addObject("loginName", loginName);
            modelAndView.addObject("userName", userName);
            modelAndView.addObject("mobile", mobile);
            return modelAndView;
    }
}
