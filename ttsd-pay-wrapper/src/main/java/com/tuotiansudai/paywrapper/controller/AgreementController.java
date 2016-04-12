package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class AgreementController {

    @Autowired
    private AgreementService agreementService;

    @RequestMapping(value = "/agreement",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> agreement(@Valid @RequestBody AgreementDto dto){
        return agreementService.agreement(dto);
    }

}
