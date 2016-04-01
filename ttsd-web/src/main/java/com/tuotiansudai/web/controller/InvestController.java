package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import com.tuotiansudai.util.RequestIPParser;
import com.tuotiansudai.web.util.LoginUserInfo;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto, RedirectAttributes redirectAttributes) {
        investDto.setSource(Source.WEB);
        String errorMessage = "投资失败，请联系客服！";
        try {
            investDto.setLoginName(LoginUserInfo.getLoginName());
            BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
            if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                return new ModelAndView("/pay", "pay", baseDto);
            }
            if (baseDto.getData() != null) {
                errorMessage = baseDto.getData().getMessage();
            }
        } catch (InvestException e) {
            errorMessage = e.getMessage();
        }

        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
        return new ModelAndView(MessageFormat.format("redirect:/loan/{0}", investDto.getLoanId()));
    }

    @RequestMapping(path = "/no-password-invest", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> invest(@Valid @ModelAttribute InvestDto investDto) {
        try {
            investDto.setSource(Source.WEB);
            return investService.noPasswordInvest(investDto);
        } catch (InvestException e) {
            BaseDto<PayDataDto> dto = new BaseDto<>();
            PayDataDto payDataDto = new PayDataDto();
            dto.setData(payDataDto);
            payDataDto.setMessage(e.getMessage());
            return dto;
        }
    }

    @RequestMapping(path = "/invest-success", method = RequestMethod.GET)
    public ModelAndView investSuccess() {
        String loginName = LoginUserInfo.getLoginName();
        InvestModel latestSuccessInvest = investService.findLatestSuccessInvest(loginName);
        if (latestSuccessInvest == null) {
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/invest-success", "responsive", true);

        return modelAndView;
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

    @ResponseBody
    @RequestMapping(path = "/no-password-invest/enabled", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> enabledNoPasswordInvest() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(investService.switchNoPasswordInvest(loginName, true));
        baseDto.setData(dataDto);
        return baseDto;
    }

    @ResponseBody
    @RequestMapping(path = "/no-password-invest/disabled", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> disabledNoPasswordInvest() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(investService.switchNoPasswordInvest(loginName, false));
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/no-password-invest/image-captcha", method = RequestMethod.GET)
    public void imageCaptcha(HttpServletResponse response) {
        int captchaWidth = 70;
        int captchaHeight = 38;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        captchaHelper.storeCaptcha(CaptchaHelper.TURN_OFF_NO_PASSWORD_INVEST, captcha.getAnswer());
    }

    @RequestMapping(path = "/no-password-invest/send-captcha", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(HttpServletRequest httpServletRequest, @Valid @ModelAttribute RegisterCaptchaDto dto) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        boolean result = this.captchaHelper.captchaVerify(CaptchaHelper.TURN_OFF_NO_PASSWORD_INVEST, dto.getImageCaptcha());
        if (result) {
            return smsCaptchaService.sendNoPasswordInvestCaptcha(dto.getMobile(), RequestIPParser.parse(httpServletRequest));
        }
        return baseDto;
    }

    @RequestMapping(value = "/no-password-invest/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.TURN_OFF_NO_PASSWORD_INVEST));
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @ResponseBody
    @RequestMapping(value = "/no-password-invest/mark-remind", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> markRemind() {
        String loginName = LoginUserInfo.getLoginName();
        investService.markNoPasswordRemind(loginName);
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        dto.setData(baseDataDto);
        return dto;
    }
}
