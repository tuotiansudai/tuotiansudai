package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.*;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.message.*;
import com.tuotiansudai.fudian.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class PayController extends AsyncRequestController {

    private static Logger logger = LoggerFactory.getLogger(PayController.class);

    private final RechargeService rechargeService;

    private final WithdrawService withdrawService;

    private final LoanCreateService loanCreateService;

    private final LoanInvestService loanInvestService;

    private final LoanCreditInvestService loanCreditInvestService;

    private final LoanFullService loanFullService;

    private final LoanRepayService loanRepayService;

    private final MerchantTransferService merchantTransferService;

    @Autowired
    public PayController(BankConfig bankConfig, RechargeService rechargeService, WithdrawService withdrawService, LoanCreateService loanCreateService, LoanInvestService loanInvestService, LoanCreditInvestService loanCreditInvestService, LoanFullService loanFullService, LoanRepayService loanRepayService, MerchantTransferService merchantTransferService) {
        super(bankConfig);
        this.rechargeService = rechargeService;
        this.withdrawService = withdrawService;
        this.loanCreateService = loanCreateService;
        this.loanInvestService = loanInvestService;
        this.loanCreditInvestService = loanCreditInvestService;
        this.loanFullService = loanFullService;
        this.loanRepayService = loanRepayService;
        this.merchantTransferService = merchantTransferService;
    }

    @RequestMapping(path = "/recharge/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> recharge(@PathVariable Source source,
                                                     @RequestBody BankRechargeDto params) {
        logger.info("[Fudian] call recharge");

        RechargeRequestDto requestDto = rechargeService.recharge(source, params);

        BankAsyncMessage bankAsyncMessage = this.generateAsyncRequestData(requestDto, ApiType.RECHARGE);

        if (!bankAsyncMessage.isStatus()) {
            logger.error("[Fudian] call recharge, request data generation failure, data: {}", params);
        }

        return ResponseEntity.ok(bankAsyncMessage);

    }

    @RequestMapping(path = "/merchant-recharge", method = RequestMethod.GET)
    public String merchantRecharge(Map<String, Object> model) {
        logger.info("[Fudian] call merchant recharge");

        RechargeRequestDto requestDto = rechargeService.merchantRecharge(Source.WEB, null, null, "1000000.00");
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.RECHARGE.getPath());
        return "post";
    }

    @RequestMapping(path = "/withdraw/source/{source}", method = RequestMethod.POST)
    @SuppressWarnings(value = "unchecked")
    public ResponseEntity<BankAsyncMessage> withdraw(@PathVariable Source source, @RequestBody BankWithdrawDto params) {
        logger.info("[Fudian] call withdraw");

        if (!params.isValid()) {
            logger.error("[Fudian] call withdraw bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        WithdrawRequestDto requestDto = withdrawService.withdraw(source, params);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.WITHDRAW);

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/loan-create", method = RequestMethod.POST)
    public ResponseEntity<BankLoanCreateMessage> loanCreate(@RequestBody BankLoanCreateDto params) {
        logger.info("[Fudian] call loan create");

        if (!params.isValid()) {
            logger.error("[Fudian] call loan bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        BankLoanCreateMessage bankLoanCreateMessage = loanCreateService.create(params);

        return ResponseEntity.ok(bankLoanCreateMessage);
    }

    @RequestMapping(path = "/loan-full", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> loanFull(@RequestBody BankLoanFullDto params) {
        logger.info("[Fudian] call loan full");

        if (!params.isValid()) {
            logger.error("[Fudian] call loan full bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(loanFullService.full(params));
    }

    @RequestMapping(path = "/loan-invest/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> loanInvest(@PathVariable(name = "source") Source source, @RequestBody BankInvestDto params) {
        logger.info("[Fudian] call loan invest");

        if (!params.isValid()) {
            logger.error("[Fudian] call loan invest bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        LoanInvestRequestDto requestDto = loanInvestService.invest(source, params);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.LOAN_INVEST);

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/loan-credit-invest", method = RequestMethod.GET)
    public String loanCreditInvest(Map<String, Object> model) {
        logger.info("[Fudian] call loan credit invest");

        LoanCreditInvestRequestDto requestDto = loanCreditInvestService.invest(Source.WEB, "UU02624634769241001", "UA02624634769281001", "LU02625453517541001", "20180427000000000002", "20180427", "3", "1.00", "1.00", "100.00", null, null);
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.LOAN_CREDIT_INVEST.getPath());
        return "post";
    }

    @RequestMapping(path = "/loan-fast-invest/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<BankReturnCallbackMessage> loanFastInvest(@PathVariable(name = "source") Source source, @RequestBody BankInvestDto params) {
        logger.info("[Fudian] call loan fast invest");

        if (!params.isValid()) {
            logger.error("[Fudian] call loan fast invest bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        BankReturnCallbackMessage bankReturnCallbackMessage = loanInvestService.fastInvest(source, params);
        return ResponseEntity.ok(bankReturnCallbackMessage);
    }

    @RequestMapping(path = "/loan-repay/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> loanRepay(@PathVariable(name = "source") Source source, @RequestBody BankLoanRepayDto params) {
        logger.info("[Fudian] call loan repay");

        if (!params.isValid()) {
            logger.error("[Fudian] call loan repay bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        LoanRepayRequestDto requestDto = loanRepayService.repay(source, params);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.LOAN_REPAY);

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/merchant-transfer", method = RequestMethod.POST)
    public ResponseEntity<BankMerchantTransferMessage> merchantTransfer(@RequestBody BankMerchantTransferDto params) {
        logger.info("[Fudian] call merchant transfer");

        BankMerchantTransferMessage message = merchantTransferService.transfer(params);

        return ResponseEntity.ok(message);
    }
}
