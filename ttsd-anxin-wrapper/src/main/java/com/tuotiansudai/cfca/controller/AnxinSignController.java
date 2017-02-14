package com.tuotiansudai.cfca.controller;

import com.tuotiansudai.cfca.dto.AnxinSignCreateDto;
import com.tuotiansudai.cfca.service.AnxinSignService;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/anxin-sign")
public class AnxinSignController {

    @Autowired
    private AnxinSignService anxinSignService;


    @RequestMapping(value = "/{loanId}/create-loan-contract", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> createLoanContracts(@Valid @RequestBody Long loanId, HttpServletRequest request) {
        return anxinSignService.createLoanContracts(loanId);
    }

    @RequestMapping(value = "/{transferId}/create-transfer-contract", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> createTransferContracts(@Valid @RequestBody Long transferId, HttpServletRequest request) {
        return anxinSignService.createTransferContracts(transferId);
    }
}
