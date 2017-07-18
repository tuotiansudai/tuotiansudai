package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/current")
public class CurrentDepositController {

    private final AccountService accountService;

    @Autowired
    public CurrentDepositController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/deposit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView showDeposit() {
        AccountModel accountModel = accountService.findByLoginName(LoginUserInfo.getLoginName());

        ModelAndView modelAndView = new ModelAndView("/day-turn-in");
        modelAndView.addObject("balance", accountModel == null ? 0 : accountModel.getBalance());
        return modelAndView;
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ModelAndView deposit(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        boolean isWeChatSource = StringUtils.isEmpty(request.getSession().getAttribute("weChatUserOpenid"));
//        investDto.setSource(isWeChatSource ? Source.WEB : Source.WE_CHAT );

        redirectAttributes.addFlashAttribute("errorMessage", "errorMessage");
        redirectAttributes.addFlashAttribute("depositAmount", "1.00");
        return new ModelAndView("redirect:/current/deposit");
    }
}
