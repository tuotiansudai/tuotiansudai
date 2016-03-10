package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.service.AgreementService;
import com.tuotiansudai.service.NoPasswordInvestService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/no-password-invest")
public class NoPasswordInvestController {
    @Autowired
    private NoPasswordInvestService noPasswordInvestService;
    @Autowired
    private AgreementService agreementService;

    @RequestMapping(path = "/enabled",method = RequestMethod.POST)
    public BaseDto<BaseDataDto> enabledNoPasswordInvest(){
        String loginName = LoginUserInfo.getLoginName();
        noPasswordInvestService.enabledNoPasswordInvest(loginName);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        baseDto.setData(dataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }
    @RequestMapping(path = "/disabled",method = RequestMethod.POST)
    public BaseDto<BaseDataDto> disabledNoPasswordInvest(){
        String loginName = LoginUserInfo.getLoginName();
        noPasswordInvestService.disabledNoPasswordInvest(loginName);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        baseDto.setData(dataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView agreement(@Valid @ModelAttribute AgreementDto agreementDto){
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(LoginUserInfo.getLoginName(), agreementDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }

}
