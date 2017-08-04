package com.tuotiansudai.paywrapper.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.current.dto.DepositDto;
import com.tuotiansudai.current.dto.LoanOutHistoryDto;
import com.tuotiansudai.current.dto.LoanOutHistoryStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.current.CurrentDepositService;
import com.tuotiansudai.paywrapper.current.CurrentLoanOutService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/current")
public class CurrentController {

    private final static Logger logger = Logger.getLogger(CurrentController.class);

    private final CurrentDepositService currentDepositService;
    private final CurrentLoanOutService currentLoanOutService;

    @Autowired
    public CurrentController(CurrentDepositService currentDepositService, CurrentLoanOutService currentLoanOutService) {
        this.currentDepositService = currentDepositService;
        this.currentLoanOutService = currentLoanOutService;
    }

    @RequestMapping(path = "/deposit-with-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> deposit(@Valid @RequestBody DepositDto depositRequestDto) {
        return currentDepositService.deposit(depositRequestDto);
    }

    @RequestMapping(value = "/deposit-with-no-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> noPasswordDeposit(@Valid @RequestBody DepositDto depositRequestDto) {
        return currentDepositService.noPasswordDeposit(depositRequestDto);
    }

    @RequestMapping(value = "/over-deposit", method = RequestMethod.POST)
    public void overDeposit(@Valid @RequestBody DepositDto depositRequestDto) {
        currentDepositService.overDeposit(depositRequestDto);
    }

    @RequestMapping(value = "/loan-out", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<LoanOutHistoryDto> loanOut(@Valid @RequestBody LoanOutHistoryDto loanOutHistoryDto) throws Exception {
        logger.info(MessageFormat.format("[current loan out] request is coming, request data is {0}", loanOutHistoryDto.toString()));

        if (Lists.newArrayList(LoanOutHistoryStatus.RESERVE_TRANSFER_WAITING_PAY, LoanOutHistoryStatus.WAITING_PAY).contains(loanOutHistoryDto.getStatus())) {
            LoanOutHistoryDto out = currentLoanOutService.loanOut(loanOutHistoryDto);
            if (out != null) {
                logger.info(MessageFormat.format("[current loan out] request finished, request data is {0}", loanOutHistoryDto.toString()));
                return ResponseEntity.ok().body(out);
            }
        }

        logger.error(MessageFormat.format("[current loan out] request is invalid, request data is {0}", loanOutHistoryDto.toString()));
        return ResponseEntity.badRequest().body(null);
    }

}
