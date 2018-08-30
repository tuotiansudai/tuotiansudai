package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.RiskEstimateService;
import com.tuotiansudai.service.BankBindCardService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/personal-info")
public class PersonalInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankBindCardService bankBindCardService;

    @Autowired
    private RiskEstimateService riskEstimateService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView personalInfo() {
        ModelAndView mv = new ModelAndView("/personal-info");
        UserModel userModel = userService.findByMobile(LoginUserInfo.getMobile());
        Role role = LoginUserInfo.getBankRole();

        mv.addObject("loginName", userModel.getLoginName());
        mv.addObject("mobile", userModel.getMobile());
        mv.addObject("email", userModel.getEmail());
        mv.addObject("estimate", riskEstimateService.getEstimate(LoginUserInfo.getLoginName()));
        mv.addObject("hasLoanerAccount", bankAccountService.findBankAccount(userModel.getLoginName(), Role.LOANER) != null);

        if (role != null) {
            mv.addObject("userName", userModel.getUserName());
            mv.addObject("identityNumber", userModel.getIdentityNumber());
            BankAccountModel bankAccountModel = bankAccountService.findBankAccount(LoginUserInfo.getLoginName(), role);
            mv.addObject("authorization", bankAccountModel.isAuthorization());
            mv.addObject("autoInvest", bankAccountModel.isAutoInvest());
            mv.addObject("bankCard", bankBindCardService.findBankCard(LoginUserInfo.getLoginName(), role));
            mv.addObject("bankMobile", bankAccountModel.getBankMobile());
        }
        return mv;
    }

    @RequestMapping(path = "/email/{email}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> isEmailExist(@PathVariable String email) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(userService.emailIsExist(email));

        return baseDto;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> changePassword(String originalPassword, String newPassword, String newPasswordConfirm, HttpServletRequest request) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);

        dataDto.setStatus(newPassword.equals(newPasswordConfirm) && userService.changePassword(LoginUserInfo.getLoginName(),
                originalPassword, newPassword, RequestIPParser.parse(request), "WEB", ""));

        return baseDto;
    }

    @RequestMapping(value = "/reset-bank-password/source/{source}", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView resetBankPassword(@PathVariable(value = "source") Source source) {
        BankAsyncMessage bankAsyncData = bankAccountService.resetPassword(source, LoginUserInfo.getLoginName(), LoginUserInfo.getBankRole());
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }

    @RequestMapping(value = "/change-bank-mobile", method = RequestMethod.GET)
    public ModelAndView changeBankPhoneView() {
        ModelAndView modelAndView = new ModelAndView("/personal-change-bank-mobile");
        BankAccountModel bank=bankAccountService.findBankAccount(LoginUserInfo.getLoginName(), LoginUserInfo.getBankRole());
        modelAndView.addObject("originMobile",bank.getBankMobile());
        return modelAndView;
    }

    @RequestMapping(value = "/change-bank-mobile", method = RequestMethod.POST)
    public ModelAndView changeBankPhone(@RequestParam("newPhone") String newPhone,
                                        @RequestParam("type") String type) {
        BankAsyncMessage bankAsyncData = bankAccountService.changeBankMobile(LoginUserInfo.getLoginName(), newPhone, type, LoginUserInfo.getBankRole(), Source.WEB);
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }
}
