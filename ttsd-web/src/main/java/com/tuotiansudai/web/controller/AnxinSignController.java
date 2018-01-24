package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/anxinSign")
public class AnxinSignController {

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView anxinSignPage() {
        String loginName = LoginUserInfo.getLoginName();
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        anxinProp = anxinProp != null ? anxinProp : new AnxinSignPropertyModel();

        if (anxinProp != null && anxinProp.getAnxinUserId() != null && anxinProp.getProjectCode() != null) {
            // 如果以前授权过，则进入列表页
            return new ModelAndView("/myAccount/anxin-sign-list", "anxinProp", anxinProp);
        } else {
            // 否则进入开通安心签账户的初始页面
            return new ModelAndView("/myAccount/anxin-sign-init", "anxinProp", anxinProp);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public BaseDto createAccount() throws Exception {
        String loginName = LoginUserInfo.getLoginName();
        return anxinWrapperClient.createAccount(loginName);
    }

    @ResponseBody
    @RequestMapping(value = "/sendCaptcha", method = RequestMethod.POST)
    public BaseDto sendCaptcha(boolean isVoice) throws Exception {
        String loginName = LoginUserInfo.getLoginName();
        return anxinWrapperClient.sendCaptcha(new AnxinSendCaptchaDto(loginName, isVoice));
    }

    @ResponseBody
    @RequestMapping(value = "/verifyCaptcha", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> verifyCaptcha(String captcha, boolean skipAuth, HttpServletRequest request) throws Exception {
        String ip = RequestIPParser.parse(request);
        String loginName = LoginUserInfo.getLoginName();
        return anxinWrapperClient.verifyCaptcha(new AnxinVerifyCaptchaDto(loginName, ip, captcha, skipAuth));
    }

    @ResponseBody
    @RequestMapping(value = "/switchSkipAuth", method = RequestMethod.POST)
    public BaseDto switchSkipAuth(boolean open) {
        String loginName = LoginUserInfo.getLoginName();
        return anxinWrapperClient.switchSkipAuth(new AnxinSwitchSkipAuthDto(loginName, open));
    }

    @ResponseBody
    @RequestMapping(value = "/createAccountSuccess", method = RequestMethod.GET)
    public ModelAndView createAccountSuccess() {
        return new ModelAndView("/myAccount/anxin-electro-success");
    }
}
