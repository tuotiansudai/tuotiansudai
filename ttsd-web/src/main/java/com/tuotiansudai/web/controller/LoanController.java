package com.tuotiansudai.web.controller;


import com.tuotiansudai.coupon.dto.UserInvestingCouponDto;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.List;


@Controller
@RequestMapping(value = "/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserCouponService userCouponService;

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable long loanId) {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/loan");
        BaseDto<LoanDto> dto = loanService.getLoanDetail(loginName, loanId);
        if (dto.getData() == null) {
            return new ModelAndView("/error/404");
        }
        addCouponInfo(modelAndView, loginName, loanId);
        modelAndView.addObject("loan",dto.getData());
        return modelAndView;
    }

    @RequestMapping(value = "/{loanId:^\\d+$}/invests", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getInvestList(@PathVariable long loanId,
                                                        @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                        @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return loanService.getInvests(LoginUserInfo.getLoginName(), loanId, index, pageSize);
    }


    private void addCouponInfo(ModelAndView mv, String loginName, long loanId) {
        List<UserInvestingCouponDto> couponDtos = userCouponService.getValidCoupons(loginName, loanId);
        mv.addObject("coupons", couponDtos);
    }

}
