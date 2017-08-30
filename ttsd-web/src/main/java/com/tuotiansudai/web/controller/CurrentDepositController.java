package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.AccountRequestDto;
import com.tuotiansudai.current.dto.DepositDto;
import com.tuotiansudai.current.dto.DepositStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentDepositRequestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.support.client.dto.ErrorResponse;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
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

    private static Logger logger = Logger.getLogger(CurrentDepositController.class);

    private final UserService userService;

    private final AccountService accountService;

    private final CurrentRestClient currentRestClient;

    @Autowired
    public CurrentDepositController(AccountService accountService, CurrentRestClient currentRestClient, UserService userService) {
        this.accountService = accountService;
        this.currentRestClient = currentRestClient;
        this.userService = userService;
    }

    @RequestMapping(path = "/deposit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView showDeposit() {
        AccountModel accountModel = accountService.findByLoginName(LoginUserInfo.getLoginName());
        long personalMaxDeposit = 0;
        try {
            personalMaxDeposit = currentRestClient.getAccount(LoginUserInfo.getLoginName()).getPersonalMaxDeposit();
        } catch (RestException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        ModelAndView modelAndView = new ModelAndView("/current-deposit");
        modelAndView.addObject("balance", accountModel == null ? 0 : accountModel.getBalance());
        modelAndView.addObject("personalMaxDeposit", personalMaxDeposit);
        return modelAndView;
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ModelAndView deposit(@Valid @ModelAttribute CurrentDepositRequestDto currentDepositRequestDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        boolean isWeChatSource = StringUtils.isEmpty(request.getSession().getAttribute("weChatUserOpenid"));
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        DepositDto depositRequestDto = new DepositDto(null, loginName, currentDepositRequestDto.getAmount(), isWeChatSource ? Source.WEB : Source.WE_CHAT, DepositStatus.WAITING_PAY, accountModel.isNoPasswordInvest());

        try {
            if (Strings.isNullOrEmpty(currentRestClient.getAccount(loginName).getUsername())) {
                UserModel userModel = userService.findByMobile(LoginUserInfo.getMobile());
                currentRestClient.createAccount(new AccountRequestDto(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile()));
            }
            if (accountModel.isNoPasswordInvest()) {
                BaseDto<PayDataDto> payData = currentRestClient.noPasswordDeposit(depositRequestDto);
                if (payData.getData().getStatus()) {
                    return new ModelAndView(MessageFormat.format("redirect:/{0}?order_id={1}",
                            AsyncUmPayService.CURRENT_DEPOSIT_PROJECT_TRANSFER_NOPWD.getWebRetCallbackPath(), payData.getData().getExtraValues().get("order_id")));
                }
                redirectAttributes.addFlashAttribute("errorMessage", payData.getData().getMessage());
            } else {
                BaseDto<PayFormDataDto> payFormData = currentRestClient.deposit(depositRequestDto);
                return new ModelAndView("/pay", "pay", payFormData);
            }
        } catch (RestException e) {
            ErrorResponse response = e.getResponse();
            redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
        }

        redirectAttributes.addFlashAttribute("depositAmount", AmountConverter.convertCentToString(currentDepositRequestDto.getAmount()));
        return new ModelAndView("redirect:/current/deposit");
    }
}
