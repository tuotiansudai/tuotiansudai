package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.UserBindBankCardService;
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
@RequestMapping(path = "/bank-card")
public class BindBankCardController {

    private final UserBindBankCardService userBindBankCardService;

    @Autowired
    public BindBankCardController(UserBindBankCardService userBindBankCardService) {
        this.userBindBankCardService = userBindBankCardService;
    }

    @RequestMapping(path = "/bind", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView bindBankCard(HttpServletRequest request) {
        BaseDto<PayFormDataDto> baseDto = userBindBankCardService.bind(LoginUserInfo.getLoginName(), Source.WEB, RequestIPParser.parse(request), null);
        ModelAndView view = new ModelAndView("/pay");
        view.addObject("pay", baseDto);
        return view;
    }

    @RequestMapping(path = "/unbind", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView unbindBankCard(HttpServletRequest request) {
        BaseDto<PayFormDataDto> baseDto = userBindBankCardService.unbind(LoginUserInfo.getLoginName(), Source.WEB, RequestIPParser.parse(request), null);
        ModelAndView view = new ModelAndView("/pay");
        view.addObject("pay", baseDto);
        return view;
    }

}
