package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.CaptchaHelper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.RequestIPParser;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!StringUtils.isEmpty(request.getSession().getAttribute("weChatUserOpenid"))) {
            investDto.setSource(Source.WE_CHAT);
        } else {
            investDto.setSource(Source.WEB);
        }
        String errorMessage = "投资失败，请联系客服！";
        String errorType = "";
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
            errorType = e.getType().name();
        }

        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        redirectAttributes.addFlashAttribute("errorType", errorType);
        redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
        return new ModelAndView(MessageFormat.format("redirect:/loan/{0}", investDto.getLoanId()));
    }

    @RequestMapping(path = "/no-password-invest", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> invest(@Valid @ModelAttribute InvestDto investDto, HttpServletRequest request) {
        try {
            if (!StringUtils.isEmpty(request.getSession().getAttribute("weChatUserOpenid"))) {
                investDto.setSource(Source.WE_CHAT);
            } else {
                investDto.setSource(Source.WEB);
            }
            investDto.setLoginName(LoginUserInfo.getLoginName());
            return investService.noPasswordInvest(investDto);
        } catch (InvestException e) {
            BaseDto<PayDataDto> dto = new BaseDto<>();
            PayDataDto payDataDto = new PayDataDto();
            dto.setData(payDataDto);
            payDataDto.setMessage(e.getMessage());
            return dto;
        }
    }

    @ResponseBody
    @RequestMapping(path = "/no-password-invest/enabled", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> enabledNoPasswordInvest(HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(investService.switchNoPasswordInvest(loginName, true, ip));
        baseDto.setData(dataDto);
        return baseDto;
    }

    @ResponseBody
    @RequestMapping(path = "/no-password-invest/disabled", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> disabledNoPasswordInvest(HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(investService.switchNoPasswordInvest(loginName, false, ip));
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/no-password-invest/image-captcha", method = RequestMethod.GET)
    public void imageCaptcha(HttpServletRequest request, HttpServletResponse response) {
        int captchaWidth = 70;
        int captchaHeight = 38;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        captchaHelper.storeCaptcha(captcha.getAnswer(), request.getSession(false) != null ? request.getSession(false).getId() : null);
    }

    @RequestMapping(path = "/no-password-invest/send-captcha", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(HttpServletRequest httpServletRequest, @Valid @ModelAttribute RegisterCaptchaDto dto) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        boolean result = this.captchaHelper.captchaVerify(dto.getImageCaptcha(), httpServletRequest.getSession(false).getId(), httpServletRequest.getRemoteAddr());
        if (result) {
            return smsCaptchaService.sendNoPasswordInvestCaptcha(dto.getMobile(), RequestIPParser.parse(httpServletRequest));
        }
        return baseDto;
    }

    @RequestMapping(value = "/no-password-invest/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, CaptchaType.NO_PASSWORD_INVEST));
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

    @RequestMapping(value = "/calculate-expected-interest/loan/{loanId:^\\d+$}/amount/{amount:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateExpectedInterest(@PathVariable long loanId, @PathVariable long amount) {
        String loginName = LoginUserInfo.getLoginName();
        long expectedInterest = investService.estimateInvestIncome(loanId, loginName, amount);
        return AmountConverter.convertCentToString(expectedInterest);
    }

    @RequestMapping(value = "/calculate-expected-coupon-interest/loan/{loanId:^\\d+$}/amount/{amount:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateCouponExpectedInterest(@PathVariable long loanId,
                                                  @PathVariable long amount,
                                                  @RequestParam List<Long> couponIds) {
        String loginName = LoginUserInfo.getLoginName();
        long expectedInterest = couponService.estimateCouponExpectedInterest(loginName, loanId, couponIds, amount);
        return AmountConverter.convertCentToString(expectedInterest);
    }

    @RequestMapping(path = "/get-membership-preference", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<MembershipPreferenceDto> getMembershipPreference(@RequestParam(value = "loanId") long loanId,
                                                                    @RequestParam(value = "investAmount") String investAmount,
                                                                    @RequestParam(value = "couponIds", defaultValue = "") List<Long> couponIds) {
        String loginName = LoginUserInfo.getLoginName();
        MembershipPreferenceDto membershipPreferenceDto = new MembershipPreferenceDto(true);
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        if (StringUtils.isEmpty(loginName)) {
            membershipPreferenceDto.setValid(false);
        } else {
            double fee = membershipPrivilegePurchaseService.obtainServiceFee(loginName);
            membershipPreferenceDto.setValid(true);
            membershipPreferenceDto.setLevel(membershipModel.getLevel());
            membershipPreferenceDto.setRate((int) (fee * 100));
            membershipPreferenceDto.setMembershipPrivilege(membershipPrivilegePurchaseService.obtainMembershipPrivilege(loginName) != null);
            membershipPreferenceDto.setAmount(AmountConverter.convertCentToString(investService.calculateMembershipPreference(loginName, loanId, couponIds, AmountConverter.convertStringToCent(investAmount), Source.WEB)));
        }
        BaseDto<MembershipPreferenceDto> baseDto = new BaseDto<>();
        baseDto.setData(membershipPreferenceDto);
        return baseDto;
    }
}
