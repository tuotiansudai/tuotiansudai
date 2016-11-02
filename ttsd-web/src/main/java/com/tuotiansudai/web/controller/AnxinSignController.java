package com.tuotiansudai.web.controller;

import cfca.sadk.algorithm.common.PKIException;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/anxinSign")
public class AnxinSignController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AnxinSignService anxinSignService;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView anxinSignPage() {
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        if (accountModel != null && accountModel.getAnxinUserId() != null) {
            return new ModelAndView("/myAccount/anxin-sign-list", "account", accountModel);
        } else {
            return new ModelAndView("/myAccount/anxin-sign-init", "account", accountModel);
        }
    }

    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    private BaseDto<BaseDataDto> createAccount() throws PKIException {
        String loginName = LoginUserInfo.getLoginName();
        return anxinSignService.createAccount3001(loginName);
    }

    @RequestMapping(value = "/sendCaptcha", method = RequestMethod.POST)
    private BaseDto<BaseDataDto> sendCaptcha() throws PKIException {
        String loginName = LoginUserInfo.getLoginName();
        return anxinSignService.sendCaptcha3101(loginName);
    }

    @RequestMapping(value = "/verifyCaptcha", method = RequestMethod.POST, params = {"captcha", "skipAuth"})
    private BaseDto<BaseDataDto> verifyCaptcha(String captcha, boolean skipAuth) throws PKIException {
        String loginName = LoginUserInfo.getLoginName();
        return anxinSignService.verifyCaptcha3102(loginName, captcha, skipAuth);
    }

}
