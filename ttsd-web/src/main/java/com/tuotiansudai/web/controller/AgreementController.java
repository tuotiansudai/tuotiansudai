package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.AuthorizationDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.service.BankAuthorizationService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/agreement")
public class AgreementController {

    @Autowired
    private BankAuthorizationService bankAuthorizationService;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView agreement(@Valid @ModelAttribute AuthorizationDto authorizationDto, HttpServletRequest request){
        authorizationDto.setIp(RequestIPParser.parse(request));
        BankAsyncMessage baseDto = bankAuthorizationService.authorization(
                authorizationDto.getSource(),
                LoginUserInfo.getLoginName(),
                LoginUserInfo.getMobile(),
                authorizationDto.getIp(),
                authorizationDto.getDeviceId());
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
