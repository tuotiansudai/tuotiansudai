package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Administrator on 2015/9/15.
 */
@Controller
@RequestMapping(value = "/agreement")
public class AgreementController {

    @Autowired
    private AgreementService agreementService;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView agreement(@Valid @ModelAttribute AgreementDto agreementDto){
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(agreementDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }

}
