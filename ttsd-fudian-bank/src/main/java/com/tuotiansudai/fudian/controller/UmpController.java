package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.ump.asyn.request.*;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.fudian.umpdto.*;
import com.tuotiansudai.fudian.umpservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/ump")
public class UmpController {

    private final UmpRechargeService umpRechargeService;

    private final UmpWithdrawService umpWithdrawService;

    private final UmpBindCardService umpBindCardService;

    private final UmpReplaceBindCardService umpReplaceBindCardService;

    private final UmpLoanRepayService umpLoanRepayService;

    private final UmpExperienceRepayService umpExperienceRepayService;

    @Autowired
    private UmpUserService umpUserService;
    @Autowired
    private UmpLoanStatusService umpLoanStatusService;

    @Autowired
    private UmpTransferService umpTransferService;

    @Autowired
    public UmpController(UmpRechargeService umpRechargeService, UmpWithdrawService umpWithdrawService,
                         UmpBindCardService umpBindCardService, UmpReplaceBindCardService umpReplaceBindCardService,
                         UmpLoanRepayService umpLoanRepayService, UmpExperienceRepayService umpExperienceRepayService){
        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
        this.umpBindCardService = umpBindCardService;
        this.umpReplaceBindCardService = umpReplaceBindCardService;
        this.umpLoanRepayService = umpLoanRepayService;
        this.umpExperienceRepayService = umpExperienceRepayService;
    }

    @RequestMapping(value = "/bind-card", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> bindCard(@RequestBody UmpBindCardDto dto){
        PtpMerBindCardRequestModel model = umpBindCardService.bindCard(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/replace-bind-card", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> replaceBindCard(@RequestBody UmpReplaceBindCardDto dto){
        PtpMerReplaceCardRequestModel model = umpReplaceBindCardService.replaceBindCard(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> recharge(@RequestBody UmpRechargeDto dto){
        MerRechargePersonRequestModel model = umpRechargeService.recharge(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> withdraw(@RequestBody UmpWithdrawDto dto){
        CustWithdrawalsRequestModel model = umpWithdrawService.withdraw(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/loan-repay", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> loanRepay(@RequestBody UmpLoanRepayDto dto){
        ProjectTransferRequestModel model = umpLoanRepayService.loanRepay(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/experience-repay", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> experienceRepay(@RequestBody UmpExperienceRepayDto dto){
        BankBaseMessage message = umpExperienceRepayService.experienceRepay(dto);
        return ResponseEntity.ok(message);
    }

    private UmpAsyncMessage generateAsyncRequestData(BaseSyncRequestModel model) {
        if (model == null || Strings.isNullOrEmpty(model.getRequestData())) {
            return new UmpAsyncMessage(false, null, null, "请求数据生成失败");
        }
        return new UmpAsyncMessage(true, model.getRequestUrl(), model.getField(), null);
    }

    @RequestMapping(path = "/user/{loginName}")
    @ResponseBody
    public Map<String, String> getRealTimeUserStatus(@PathVariable String loginName) {
        return umpUserService.getUserStatus(loginName);
    }

    @RequestMapping(path = "/loan/{loanId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getRealTimeLoanStatus(@PathVariable long loanId) {

        return umpLoanStatusService.getLoanStatus(loanId);
    }

    @RequestMapping(path = "/transfer/order-id/{orderId}/mer-date/{merDate}/business-type/{businessType}")
    @ResponseBody
    public Map<String, String> getRealTimeTransferStatus(@PathVariable String orderId,
                                                         @PathVariable String merDate,
                                                         @PathVariable String businessType) {

        return umpTransferService.getTransferStatus(orderId,merDate,businessType);
    }
}
