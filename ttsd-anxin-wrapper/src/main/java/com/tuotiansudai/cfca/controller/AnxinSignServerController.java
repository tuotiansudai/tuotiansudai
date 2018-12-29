package com.tuotiansudai.cfca.controller;

import com.tuotiansudai.cfca.service.AnxinSignService;
import com.tuotiansudai.cfca.contract.ContractService;
import com.tuotiansudai.dto.*;
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
public class AnxinSignServerController {

    @Autowired
    private AnxinSignService anxinSignService;

    @Autowired
    private ContractService contractService;

    @ResponseBody
    @RequestMapping(value = "/create-loan-contract", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> createLoanContracts(@Valid @RequestBody long loanId, HttpServletRequest request) {
        return anxinSignService.createLoanContracts(loanId);
    }

    @ResponseBody
    @RequestMapping(value = "/create-loan-service-agreement", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> createLoanServiceAgreement(@Valid @RequestBody long loanId, HttpServletRequest request) {
        return anxinSignService.createLoanServiceAgreement(loanId);
    }

    @ResponseBody
    @RequestMapping(value = "/create-transfer-contract", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> createTransferContracts(@Valid @RequestBody long transferId, HttpServletRequest request) {
        return anxinSignService.createTransferContracts(transferId);
    }

    @ResponseBody
    @RequestMapping(value = "/query-contract", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> queryContract(@Valid @RequestBody AnxinQueryContractDto anxinQueryContractDto) {
        return anxinSignService.queryContract(anxinQueryContractDto);
    }

    @ResponseBody
    @RequestMapping(value = "/create-account", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> createAccount(@Valid @RequestBody String loginName) {
        return anxinSignService.createAccount3001(loginName);
    }

    @ResponseBody
    @RequestMapping(value = "/send-captcha", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> sendCaptcha(@Valid @RequestBody AnxinSendCaptchaDto anxinSendCaptchaDto) {
        return anxinSignService.sendCaptcha3101(anxinSendCaptchaDto.getLoginName(), anxinSendCaptchaDto.isVoice());
    }

    @ResponseBody
    @RequestMapping(value = "/verify-captcha", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> verifyCaptcha(@Valid @RequestBody AnxinVerifyCaptchaDto anxinVerifyCaptchaDto) {
        return anxinSignService.verifyCaptcha3102(anxinVerifyCaptchaDto.getLoginName(), anxinVerifyCaptchaDto.getCaptcha(),
                anxinVerifyCaptchaDto.isSkipAuth(), anxinVerifyCaptchaDto.getIp());
    }

    @ResponseBody
    @RequestMapping(value = "/switch-skip-auth", method = RequestMethod.POST)
    public BaseDto<AnxinDataDto> switchSkipAuth(@Valid @RequestBody AnxinSwitchSkipAuthDto anxinSwitchSkipAuthDto) {
        return anxinSignService.switchSkipAuth(anxinSwitchSkipAuthDto.getLoginName(), anxinSwitchSkipAuthDto.isOpen());
    }

    @ResponseBody
    @RequestMapping(value = "/print-contract")
    public byte[] printContract(@Valid @RequestBody AnxinLookContractDto anxinLookContractDto){
        return contractService.printContractPdf(anxinLookContractDto.getAnxinContractType(), anxinLookContractDto.getLoginName(),
                anxinLookContractDto.getLoanId(), anxinLookContractDto.getInvestId());
    }

    @ResponseBody
    @RequestMapping(value = "/print-anxin-contract")
    public byte[] printAnxinContract(@Valid @RequestBody String contractNo){
        return anxinSignService.downContractByContractNo(contractNo);
    }

    @ResponseBody
    @RequestMapping(value = "/is-authentication-required")
    public BaseDto<AnxinDataDto> isAuthenticationRequired(@Valid @RequestBody String loginName){
        return new BaseDto(true, new AnxinDataDto(anxinSignService.isAuthenticationRequired(loginName), ""));
    }

}
