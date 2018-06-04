package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.CaptchaHelper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RequestIPParser;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Date;
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

    @RequestMapping(value = "/invest", method = RequestMethod.GET)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        investDto.setSource(request.getSession().getAttribute("weChatUserOpenid") == null ? investDto.getSource() : Source.WE_CHAT);

        if (!bindingResult.hasErrors()) {
            try {
                investDto.setLoginName(LoginUserInfo.getLoginName());
                BankAsyncMessage bankAsyncData = investService.invest(investDto);
                return new ModelAndView("/pay", "pay", bankAsyncData);
            } catch (InvestException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                redirectAttributes.addFlashAttribute("errorType", e.getType().name());
            }
        }

        redirectAttributes.addFlashAttribute("errorMessage", "投资失败，请联系客服");
        redirectAttributes.addFlashAttribute("errorType", "");
        redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());

        if (Source.M.equals(investDto.getSource())) {
            return new ModelAndView(MessageFormat.format("redirect:/m/loan/{0}#buyDetail", investDto.getLoanId()));
        }

        return new ModelAndView(MessageFormat.format("redirect:/loan/{0}", investDto.getLoanId()));
    }

    @RequestMapping(path = "/no-password-invest", method = RequestMethod.POST)
    @ResponseBody
    public BankReturnCallbackMessage invest(@Valid @ModelAttribute InvestDto investDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new BankReturnCallbackMessage(false, bindingResult.getFieldError().getDefaultMessage(), null);
        }

        try {
            investDto.setSource(request.getSession().getAttribute("weChatUserOpenid") == null ? investDto.getSource() : Source.WE_CHAT);
            investDto.setLoginName(LoginUserInfo.getLoginName());
            return investService.noPasswordInvest(investDto);
        } catch (InvestException e) {
            return new BankReturnCallbackMessage(false, e.getMessage(), null);
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
        int captchaWidth = 80;
        int captchaHeight = 33;
        Captcha captcha = this.captchaHelper.getCaptcha(request.getSession().getId(), captchaHeight, captchaWidth, true);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
    }

    @RequestMapping(path = "/no-password-invest/send-captcha", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(HttpServletRequest httpServletRequest, @Valid @ModelAttribute RegisterCaptchaDto dto) {
        BaseDto<SmsDataDto> baseDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        baseDto.setData(dataDto);
        boolean result = this.captchaHelper.captchaVerify(dto.getImageCaptcha(), httpServletRequest.getSession(false).getId(), httpServletRequest.getRemoteAddr());
        if (result) {
            return smsCaptchaService.sendNoPasswordInvestCaptcha(dto.getMobile(), dto.isVoice(), RequestIPParser.parse(httpServletRequest));
        }
        return baseDto;
    }

    @RequestMapping(value = "/no-password-invest/mobile/{mobile:^\\d{11}$}/captcha/{captcha:^\\d{6}$}/verify", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> verifyCaptchaIsValid(@PathVariable String mobile, @PathVariable String captcha) {
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(smsCaptchaService.verifyMobileCaptcha(mobile, captcha, SmsCaptchaType.NO_PASSWORD_INVEST));
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
        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);
        long expectedInterest = investService.estimateInvestIncome(loanId, investFeeRate, loginName, amount, new Date());
        return AmountConverter.convertCentToString(expectedInterest);
    }

    @RequestMapping(value = "/calculate-expected-coupon-interest/loan/{loanId:^\\d+$}/amount/{amount:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateCouponExpectedInterest(@PathVariable long loanId,
                                                  @PathVariable long amount,
                                                  @RequestParam List<Long> couponIds) {
        String loginName = LoginUserInfo.getLoginName();
        //根据loginNameName查询出当前会员的相关信息,需要判断是否为空,如果为空则安装在费率0.1计算
        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);
        long expectedInterest = couponService.estimateCouponExpectedInterest(loginName, investFeeRate, loanId, couponIds, amount, new Date());
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
