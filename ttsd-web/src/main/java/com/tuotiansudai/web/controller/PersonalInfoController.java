package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.*;
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
@RequestMapping(path = "/personal-info")
public class PersonalInfoController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserBindBankCardService userBindBankCardService;

    @Autowired
    private RiskEstimateService riskEstimateService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView personalInfo() {
        ModelAndView mv = new ModelAndView("/personal-info");
        UserModel userModel = userMapper.findByLoginName(LoginUserInfo.getLoginName());

        BankAccountModel bankAccountModel = bankAccountService.findBankAccount(userModel.getLoginName());

        mv.addObject("loginName", userModel.getLoginName());
        mv.addObject("mobile", userModel.getMobile());
        mv.addObject("email", userModel.getEmail());
        mv.addObject("authorization", bankAccountModel != null && bankAccountModel.isAuthorization());
        mv.addObject("autoInvest", bankAccountModel != null && bankAccountModel.isAutoInvest());
        mv.addObject("estimate", riskEstimateService.getEstimate(LoginUserInfo.getLoginName()));

        if (bankAccountModel != null) {
            mv.addObject("userName", userModel.getUserName());
            mv.addObject("identityNumber", userModel.getIdentityNumber());

        }

        UserBankCardModel bankCard = userBindBankCardService.findBankCard(userModel.getLoginName());
        if (bankCard != null) {
            mv.addObject("bankCard", bankCard.getCardNumber());
            mv.addObject("bankName", bankCard.getBank());
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

    @RequestMapping(value = "/reset-umpay-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> resetUmpayPassword(String identityNumber) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(accountService.resetUmpayPassword(LoginUserInfo.getLoginName(), identityNumber));
        return baseDto;
    }

    @RequestMapping(value = "/reset-umpay-password", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView resetUmpayPasswordPage() {
        return new ModelAndView("reset-password");
    }
}
