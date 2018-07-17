package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.ump.asyn.request.*;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.fudian.umpservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/ump")
public class UmpController {

    private final UmpRechargeService umpRechargeService;

    private final UmpWithdrawService umpWithdrawService;

    private final UmpBindCardService umpBindCardService;

    private final UmpReplaceBindCardService umpReplaceBindCardService;

    private final UmpLoanRepayService umpLoanRepayService;

    private final UmpInvestRepayService umpInvestRepayService;

    private final UmpInvestRepayFeeService umpInvestRepayFeeService;

    private final UmpCouponRepayService umpCouponRepayService;

    private final UmpExtraRateRepayService umpExtraRateRepayService;

    @Autowired
    public UmpController(UmpRechargeService umpRechargeService, UmpWithdrawService umpWithdrawService,
                         UmpBindCardService umpBindCardService, UmpReplaceBindCardService umpReplaceBindCardService,
                         UmpLoanRepayService umpLoanRepayService, UmpInvestRepayService umpInvestRepayService,
                         UmpInvestRepayFeeService umpInvestRepayFeeService, UmpCouponRepayService umpCouponRepayService,
                         UmpExtraRateRepayService umpExtraRateRepayService){
        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
        this.umpBindCardService = umpBindCardService;
        this.umpReplaceBindCardService = umpReplaceBindCardService;
        this.umpLoanRepayService = umpLoanRepayService;
        this.umpInvestRepayService = umpInvestRepayService;
        this.umpInvestRepayFeeService = umpInvestRepayFeeService;
        this.umpCouponRepayService = umpCouponRepayService;
        this.umpExtraRateRepayService = umpExtraRateRepayService;
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
        MerRechargePersonRequestModel model = umpRechargeService.recharge("asdd", "asdasd", false, 123, 100, "ICBC");
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

    @RequestMapping(value = "/invest-repay", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> investRepay(){
        BankBaseMessage message = umpInvestRepayService.investRepay(0, 0, "asd", 0, false);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/invest-repay-fee", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> investRepayFee(){
        BankBaseMessage message = umpInvestRepayFeeService.investRepayFee(0, 0, 0, true);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/coupon-repay", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> couponRepay(){
        BankBaseMessage message = umpCouponRepayService.couponRepay(0, "", "", 0);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/extra-rate-repay", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> extraRateRepay(){
        BankBaseMessage message = umpExtraRateRepayService.extraRateRepay(0, "", "", 0);
        return ResponseEntity.ok(message);
    }

    private UmpAsyncMessage generateAsyncRequestData(BaseSyncRequestModel model) {
        if (model == null || Strings.isNullOrEmpty(model.getRequestData())) {
            return new UmpAsyncMessage(false, null, null, "请求数据生成失败");
        }
        return new UmpAsyncMessage(true, model.getRequestUrl(), model.getField(), null);
    }
}
