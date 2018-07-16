package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.service.*;
import com.tuotiansudai.fudian.ump.asyn.request.*;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/ump")
public class UmpController {

    private final UmpRegisterService umpRegisterService;

    private final UmpRechargeService umpRechargeService;

    private final UmpWithdrawService umpWithdrawService;

    private final UmpBindCardService umpBindCardService;

    private final UmpReplaceBindCardService umpReplaceBindCardService;

    private final UmpLoanRepayService umpLoanRepayService;

    @Autowired
    public UmpController(UmpRegisterService umpRegisterService, UmpRechargeService umpRechargeService, UmpWithdrawService umpWithdrawService, UmpBindCardService umpBindCardService, UmpReplaceBindCardService umpReplaceBindCardService, UmpLoanRepayService umpLoanRepayService){
        this.umpRegisterService = umpRegisterService;
        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
        this.umpBindCardService = umpBindCardService;
        this.umpReplaceBindCardService = umpReplaceBindCardService;
        this.umpLoanRepayService = umpLoanRepayService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> register(){
        BankBaseMessage message = umpRegisterService.register(null, null, null, null);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/bind-card", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> bindCard(){
        PtpMerBindCardRequestModel model = umpBindCardService.bindCard(null, 0, null, null, null, null, true);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/replace-bind-card", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> replaceBindCard(){
        PtpMerReplaceCardRequestModel model = umpReplaceBindCardService.replaceBindCard(null, 0, null, null, null, null);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> recharge(){
        MerRechargePersonRequestModel model = umpRechargeService.recharge(null, null, false, 0, 0, null);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> withdraw(){
        CustWithdrawalsRequestModel model = umpWithdrawService.withdraw(null, null, 0, 0);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/loan-repay", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> loanRepay(){
        ProjectTransferRequestModel model = umpLoanRepayService.loanRepay(null, null, 0, 0, 0, false);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    private UmpAsyncMessage generateAsyncRequestData(BaseSyncRequestModel model) {
        if (model == null || Strings.isNullOrEmpty(model.getRequestData())) {
            return new UmpAsyncMessage(false, null, null, "请求数据生成失败");
        }
        return new UmpAsyncMessage(true, model.getRequestUrl(), model.getField(), "0000");
    }
}
