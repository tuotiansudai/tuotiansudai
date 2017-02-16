package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class ExperienceInvestController {

    @Autowired
    private ExperienceInvestService experienceInvestService;

//    @RequestMapping(path = "/experience-invest", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setSource(Source.WEB);
        investDto.setLoginName(LoginUserInfo.getLoginName());
        return experienceInvestService.invest(investDto);
    }
}
