package com.tuotiansudai.web.controller;

import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BankBindCardService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/bank-card")
public class BindBankCardController {

    private final BankBindCardService bankBindCardService;

    @Autowired
    public BindBankCardController(BankBindCardService bankBindCardService) {
        this.bankBindCardService = bankBindCardService;
    }

    @RequestMapping(path = "/bind/source/{source}", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView bindBankCard(HttpServletRequest request, @PathVariable(value = "source") Source source) {
        BankAsyncMessage bankAsyncData = bankBindCardService.bind(LoginUserInfo.getLoginName(), source, RequestIPParser.parse(request), null, true);
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }

    @RequestMapping(path = "/unbind/source/{source}", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView unbindBankCard(HttpServletRequest request, @PathVariable(value = "source") Source source) {
        BankAsyncMessage bankAsyncData = bankBindCardService.unbind(LoginUserInfo.getLoginName(), source, RequestIPParser.parse(request), null, true);
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }

}
