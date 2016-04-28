package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    private BindBankCardService bindBankCardService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView personalInfo() {
        ModelAndView mv = new ModelAndView("/personal-info");
        UserModel userModel = userMapper.findByLoginName(LoginUserInfo.getLoginName());

        mv.addObject("loginName", userModel.getLoginName());
        mv.addObject("mobile", userModel.getMobile());
        mv.addObject("email", userModel.getEmail());

        AccountModel accountModel = accountService.findByLoginName(userModel.getLoginName());
        if (accountModel != null) {
            mv.addObject("userName", accountModel.getUserName());
            mv.addObject("identityNumber", accountModel.getIdentityNumber());
            mv.addObject("noPasswordInvest",accountModel.isNoPasswordInvest());
            mv.addObject("autoInvest",accountModel.isAutoInvest());
            BankCardModel bankCard = bindBankCardService.getPassedBankCard(userModel.getLoginName());
            if (bankCard != null) {
                mv.addObject("bankCard", bankCard.getCardNumber());
            }
        }

        return mv;
    }

    @RequestMapping(path = "/password/{password}/is-exist", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> isOriginalPasswordExist(@PathVariable String password) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(userService.verifyPasswordCorrect(LoginUserInfo.getLoginName(), password));

        return baseDto;
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
    public BaseDto<BaseDataDto> changePassword(String originalPassword, String newPassword, String newPasswordConfirm) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);

        dataDto.setStatus(newPassword.equals(newPasswordConfirm) && userService.changePassword(LoginUserInfo.getLoginName(), originalPassword, newPassword));

        return baseDto;
    }

    @RequestMapping(value = "/reset-umpay-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> resetUmpayPassword(String identityNumber) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(userService.resetUmpayPassword(LoginUserInfo.getLoginName(), identityNumber));
        return baseDto;
    }
}
