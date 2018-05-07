package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserRegisterBankAccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import com.tuotiansudai.util.IdentityNumberValidator;
import com.tuotiansudai.util.RequestIPParser;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(path = "/register/account")
public class RegisterAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRegisterBankAccountService bankAccountService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerAccount(@RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        ModelAndView modelAndView = new ModelAndView("/register-account", "responsive", true);
        modelAndView.addObject("redirect", redirect);
        return modelAndView;
    }

    @RequestMapping(value = "/identity-number/{identityNumber:^[1-9]\\d{13,16}[a-zA-Z0-9]{1}$}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> isIdentityNumberExist(@PathVariable String identityNumber) {
        boolean isExist = userService.isIdentityNumberExist(identityNumber);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(isExist);
        return baseDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerAccount(@Valid @ModelAttribute RegisterAccountDto registerAccountDto, HttpServletRequest request) {
//        if (IdentityNumberValidator.validateIdentity(registerAccountDto.getIdentityNumber())) {
//        }
        registerAccountDto.setMobile(LoginUserInfo.getMobile());
        registerAccountDto.setLoginName(LoginUserInfo.getLoginName());
        BaseDto<PayFormDataDto> baseDto = bankAccountService.registerAccount(registerAccountDto, Source.WEB, RequestIPParser.parse(request), null);
        ModelAndView view = new ModelAndView("/pay");
        view.addObject("pay", baseDto);
        return view;

    }
}
