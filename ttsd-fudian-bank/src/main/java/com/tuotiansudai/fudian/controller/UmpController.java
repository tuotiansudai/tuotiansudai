package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.service.UmpRechargeService;
import com.tuotiansudai.fudian.service.UmpRegisterService;
import com.tuotiansudai.fudian.service.UmpWithdrawService;
import com.tuotiansudai.fudian.ump.asyn.request.RechargeRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.WithdrawRequestModel;
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

    @Autowired
    public UmpController(UmpRegisterService umpRegisterService, UmpRechargeService umpRechargeService, UmpWithdrawService umpWithdrawService){
        this.umpRegisterService = umpRegisterService;
        this.umpRechargeService = umpRechargeService;
        this.umpWithdrawService = umpWithdrawService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<BankBaseMessage> register(){
        BankBaseMessage message = umpRegisterService.register(null, null, null, null);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> recharge(){
        RechargeRequestModel model = umpRechargeService.recharge(null, null, false, 0, 0, null);
        return ResponseEntity.ok(generateAsyncRequestData(model));
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<UmpAsyncMessage> withdraw(){
        WithdrawRequestModel model = umpWithdrawService.withdraw(null, null, 0, 0);
        return ResponseEntity.ok(generateAsyncRequestData(model));
}

    private UmpAsyncMessage generateAsyncRequestData(BaseSyncRequestModel model) {
        if (model == null || Strings.isNullOrEmpty(model.getRequestData())) {
            return new UmpAsyncMessage(false, null, null, "请求数据生成失败");
        }
        return new UmpAsyncMessage(true, model.getRequestUrl(), model.getField(), "0000");
    }
}
