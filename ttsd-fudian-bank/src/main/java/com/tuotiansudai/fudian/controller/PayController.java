package com.tuotiansudai.fudian.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.*;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankLoanCreateMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.service.*;
import com.tuotiansudai.fudian.util.AmountUtils;
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

    @RequestMapping(path = "/recharge", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> recharge(@RequestBody Map<String, String> params) {
        logger.info("[Fudian] call recharge");

        RechargeRequestDto requestDto = rechargeService.recharge(Source.valueOf(params.get("source")), params.get("loginName"), params.get("mobile"), params.get("bankUserName"), params.get("bankAccountNo"), AmountUtils.toYuan(params.get("amount")), RechargePayType.GATE_PAY);

        return this.generateResponseJson(requestDto, ApiType.RECHARGE);
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

        if (!bankAsyncData.isStatus()) {
            logger.error("[Fudian] call withdraw, request data generation failure, data: {}", params);
        }

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
    public ResponseEntity<BankBaseMessage> loanFull(@RequestBody BankLoanFullDto bankLoanFullDto) {
        logger.info("[Fudian] call loan full");

        loanFullService.full(bankLoanFullDto);

        return ResponseEntity.ok(loanFullService.full(bankLoanFullDto));
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

    @RequestMapping(path = "/loan-repay", method = RequestMethod.GET)
    public String loanRepay(Map<String, Object> model) {
        logger.info("[Fudian] call loan repay");

        LoanRepayRequestDto requestDto = loanRepayService.repay(Source.WEB, "UU02615960791461001", "UA02615960791501001", "LU02625453517541001", "0.00", "1.00", null, null);
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.LOAN_REPAY.getPath());
        return "post";
    }

    @RequestMapping(path = "/loan-fast-repay", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> loanFastRepay(Map<String, Object> model) {
        logger.info("[Fudian] call loan fast repay");

        ResponseDto responseDto = loanRepayService.fastRepay(Source.WEB, "UU02615960791461001", "UA02615960791501001", "LU02619459384521001", "1.00", "0.00", null, null);

        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping(path = "/merchant-transfer", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> merchantTransfer(Map<String, Object> model) {
        logger.info("[Fudian] call merchant transfer");

        ResponseDto responseDto = merchantTransferService.transfer("UU02615960791461001", "UA02615960791501001", "0.01", null, null);

        return ResponseEntity.ok(responseDto);
    }

    private ResponseEntity<Map<String, String>> generateResponseJson(BaseRequestDto requestDto, ApiType apiType) {
        return ResponseEntity.ok(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("data", requestDto.getRequestData())
                .put("url", bankConfig.getBankUrl() + apiType.getPath())
                .build()));
    }
}
