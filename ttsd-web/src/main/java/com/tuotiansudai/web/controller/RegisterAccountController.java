package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.IdentityNumberValidator;
import com.tuotiansudai.util.RequestIPParser;
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
    private BankAccountService bankAccountService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerAccount(@RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        ModelAndView modelAndView = new ModelAndView("/register-account", "responsive", true);
        modelAndView.addObject("redirect", redirect);
        return modelAndView;
    }

    @RequestMapping(value = "/verify/identity-number/{identityNumber:^[1-9]\\d{13,16}[a-zA-Z0-9]{1}$}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> isIdentityNumberExist(@PathVariable String identityNumber) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        boolean isLegal = IdentityNumberValidator.validateIdentity(identityNumber);
        if (!isLegal) {
            dataDto.setMessage("身份证不合法");
            return baseDto;
        }
        boolean isExist = userService.isIdentityNumberExist(identityNumber);
        if (isExist){
            dataDto.setMessage("身份证已存在");
            return baseDto;
        }
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerAccount(@Valid @ModelAttribute RegisterAccountDto registerAccountDto, HttpServletRequest request) {
        registerAccountDto.setMobile(LoginUserInfo.getMobile());
        registerAccountDto.setLoginName(LoginUserInfo.getLoginName());
        BankAsyncMessage bankAsyncData = bankAccountService.registerAccount(registerAccountDto, Source.WEB, RequestIPParser.parse(request), null);
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }
}
