package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.ump.asyn.request.CustWithdrawalsRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.MerRechargePersonRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.ProjectTransferRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.PtpMerBindCardRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.*;
import com.tuotiansudai.fudian.ump.sync.response.ProjectAccountSearchResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.PtpMerQueryResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.TransferSearchResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.UserSearchResponseModel;
import com.tuotiansudai.fudian.umpdto.*;
import com.tuotiansudai.fudian.umpservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/ump")
public class UmpController {

    private final UmpRechargeService umpRechargeService;

    private final UmpWithdrawService umpWithdrawService;

    private final UmpBindCardService umpBindCardService;

    private final UmpLoanRepayService umpLoanRepayService;

    private final UmpSynQueryService umpSynQueryService;

    private final UmpUpdateMobileService umpUpdateMobileService;

    @Autowired
    public UmpController(UmpRechargeService umpRechargeService,
                         UmpWithdrawService umpWithdrawService,
                         UmpBindCardService umpBindCardService,
                         UmpLoanRepayService umpLoanRepayService,
                         UmpSynQueryService umpSynQueryService,
                         UmpUpdateMobileService umpUpdateMobileService) {

        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
        this.umpBindCardService = umpBindCardService;
        this.umpLoanRepayService = umpLoanRepayService;
        this.umpSynQueryService = umpSynQueryService;
        this.umpUpdateMobileService = umpUpdateMobileService;
    }

    @RequestMapping(value = "/bind-card", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> bindCard(@RequestBody UmpBindCardDto dto) {
        if (!dto.isValid()) {
            return ResponseEntity.badRequest().build();
        }
        PtpMerBindCardRequestModel model = umpBindCardService.bindCard(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> recharge(@RequestBody UmpRechargeDto dto) {
        if (!dto.isValid()) {
            return ResponseEntity.badRequest().build();
        }
        MerRechargePersonRequestModel model = umpRechargeService.recharge(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> withdraw(@RequestBody UmpWithdrawDto dto) {
        if (!dto.isValid()) {
            return ResponseEntity.badRequest().build();
        }
        CustWithdrawalsRequestModel model = umpWithdrawService.withdraw(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/loan-repay", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> loanRepay(@RequestBody UmpLoanRepayDto dto) {
        if (!dto.isValid()) {
            return ResponseEntity.badRequest().build();
        }
        ProjectTransferRequestModel model = umpLoanRepayService.loanRepay(dto);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/update-mobile", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> loanRepay(@RequestBody UmpUpdateMobileDto dto) {
        if (!dto.isValid()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(umpUpdateMobileService.updateMobile(dto));
    }

    private UmpAsyncMessage generateAsyncRequestData(BaseSyncRequestModel model) {
        if (model == null || Strings.isNullOrEmpty(model.getRequestData())) {
            return new UmpAsyncMessage(false, null, null, "请求数据生成失败");
        }
        return new UmpAsyncMessage(true, model.getRequestUrl(), model.getField(), null);
    }

    @RequestMapping(path = "/user/{payUserId}")
    @ResponseBody
    public Map<String, String> getRealTimeUserStatus(@PathVariable String payUserId) {
        UserSearchResponseModel responseModel = umpSynQueryService.queryUmpInfo(new UserSearchRequestModel(payUserId), UserSearchResponseModel.class);
        if (responseModel == null) {
            return null;
        }
        return responseModel.generateHumanReadableInfo();
    }

    @RequestMapping(path = "/loan/{loanId}")
    @ResponseBody
    public Map<String, String> getRealTimeLoanStatus(@PathVariable long loanId) {
        ProjectAccountSearchResponseModel responseModel = umpSynQueryService.queryUmpInfo(new ProjectAccountSearchRequestModel(String.valueOf(loanId)), ProjectAccountSearchResponseModel.class);
        if (responseModel == null) {
            return null;
        }
        return responseModel.generateHumanReadableInfo();
    }

    @RequestMapping(path = "/transfer/order-id/{orderId}/mer-date/{merDate}/business-type/{businessType}")
    @ResponseBody
    public Map<String, String> getRealTimeTransferStatus(@PathVariable String orderId,
                                                         @PathVariable String merDate,
                                                         @PathVariable String businessType) {
        TransferSearchResponseModel responseModel = umpSynQueryService.queryUmpInfo(new TransferSearchRequestModel(orderId, merDate, businessType), TransferSearchResponseModel.class);
        if (responseModel == null) {
            return null;
        }
        return responseModel.generateHumanReadableInfo();
    }

    @RequestMapping(path = "/platform")
    @ResponseBody
    public Map<String, String> getRealTimePlatformStatus() {
        PtpMerQueryResponseModel responseModel = umpSynQueryService.queryUmpInfo(new PtpMerQueryRequestModel(), PtpMerQueryResponseModel.class);
        if (responseModel == null) {
            return null;
        }
        return responseModel.generateHumanReadableInfo();
    }

    @RequestMapping(path = "/transfer-bill/{payAccountId}/start-date/{startDate}/end-date/{endDate}")
    @ResponseBody
    public List<List<String>> getTransferBill(@PathVariable String payAccountId,
                                              @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
                                              @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
        return umpSynQueryService.getUmpTransferBill(payAccountId, startDate, endDate);
    }
}
