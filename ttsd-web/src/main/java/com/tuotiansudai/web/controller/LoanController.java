package com.tuotiansudai.web.controller;


import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;


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
        BaseDto<LoanDto> dto = loanService.getLoanDetail(loginName, loanId);
        if (dto.getData() == null) {
            return new ModelAndView("/error/404");
        }
        return new ModelAndView("/loan", "loan", dto.getData());
    }

    @RequestMapping(value = "/{loanId:^\\d+$}/amount/{amount:^\\d+(?:\\.\\d{1,2})?$}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseListDataDto> getUsableCoupons(@PathVariable long loanId, @PathVariable String amount) {
        BaseDto<BaseListDataDto> dto = new BaseDto<>();
        BaseListDataDto<UserCouponDto> dataDto = new BaseListDataDto<>();
        dataDto.setRecords(userCouponService.getUsableCoupons(LoginUserInfo.getLoginName(), loanId, AmountConverter.convertStringToCent(amount)));
        return dto;
    }

    @RequestMapping(value = "/{loanId:^\\d+$}/invests", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getInvestList(@PathVariable long loanId,
                                                        @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                        @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return loanService.getInvests(LoginUserInfo.getLoginName(), loanId, index, pageSize);
    }
}
