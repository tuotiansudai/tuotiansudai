package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @Autowired
    private CouponService couponService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto, RedirectAttributes redirectAttributes) {
        investDto.setSource(Source.WEB);
        String errorMessage = null;
        try {
            investDto.setLoginName(LoginUserInfo.getLoginName());
            BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
            if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                return new ModelAndView("/pay", "pay", baseDto);
            } else {
                if(baseDto.getData() != null) {
                    errorMessage = baseDto.getData().getMessage();
                }
            }
        } catch (InvestException e) {
            errorMessage = e.getMessage();
        }

        if (StringUtils.isEmpty(errorMessage)) {
            errorMessage = "投资失败";
        }
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
        return new ModelAndView(MessageFormat.format("redirect:/loan/{0}", investDto.getLoanId()));
    }

    @RequestMapping(value = "/calculate-expected-interest/loan/{loanId:^\\d+$}/amount/{amount:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateExpectedInterest(@PathVariable long loanId, @PathVariable long amount) {
        long expectedInterest = investService.estimateInvestIncome(loanId, amount);
        return AmountConverter.convertCentToString(expectedInterest);
    }

    @RequestMapping(value = "/calculate-expected-coupon-interest/loan/{loanId:^\\d+$}/amount/{amount:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateCouponExpectedInterest(@PathVariable long loanId,
                                                  @PathVariable long amount,
                                                  @RequestParam List<Long> couponIds) {
        long expectedInterest = couponService.estimateCouponExpectedInterest(loanId, couponIds, amount);
        return AmountConverter.convertCentToString(expectedInterest);
    }
}
