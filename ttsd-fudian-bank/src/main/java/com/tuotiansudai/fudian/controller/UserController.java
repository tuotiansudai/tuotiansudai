package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.request.*;
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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/user")
public class UserController extends AsyncRequestController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final RegisterService registerService;

    private final CardBindService cardBindService;

    private final CancelCardBindService cancelCardBindService;

    private final AuthorizationService authorizationService;

    private final PasswordResetService passwordResetService;

    private final PhoneUpdateService phoneUpdateService;

    @Autowired
    public UserController(BankConfig bankConfig, RegisterService registerService, CardBindService cardBindService, CancelCardBindService cancelCardBindService, AuthorizationService authorizationService, PasswordResetService passwordResetService, PhoneUpdateService phoneUpdateService) {
        super(bankConfig);
        this.registerService = registerService;
        this.cardBindService = cardBindService;
        this.cancelCardBindService = cancelCardBindService;
        this.authorizationService = authorizationService;
        this.passwordResetService = passwordResetService;
        this.phoneUpdateService = phoneUpdateService;
    }

    @RequestMapping(path = "/register/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<String> recharge(@PathVariable Source source, @RequestBody Map<String, String> params) {
        logger.info("[Fudian] call register, params: {}", params);

        String loginName = params.get("loginName");
        String mobile = params.get("mobile");
        String realName = params.get("realName");
        String identityCode = params.get("identityCode");

        if (isBadRequest(Lists.newArrayList(loginName, mobile, realName, identityCode))) {
            return ResponseEntity.badRequest().build();
        }

        RegisterRequestDto requestDto = registerService.register(source, loginName, mobile, realName, identityCode);
        return this.generateAsyncRequestData(requestDto, ApiType.REGISTER);
    }

    @RequestMapping(path = "/card-bind/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<String> cardBind(@PathVariable Source source, @RequestBody Map<String, String> params) {
        logger.info("[Fudian] bind card, params: {}", params);

        String loginName = params.get("loginName");
        String mobile = params.get("mobile");
        String bankUserName = params.get("bankUserName");
        String bankAccountNo = params.get("bankAccountNo");

        if (isBadRequest(Lists.newArrayList(loginName, mobile, bankUserName, bankAccountNo))) {
            return ResponseEntity.badRequest().build();
        }

        CardBindRequestDto requestDto = cardBindService.bind(source, params.get("loginName"), params.get("mobile"), params.get("bankUserName"), params.get("bankAccountNo"));
        return this.generateAsyncRequestData(requestDto, ApiType.CARD_BIND);
    }

    @RequestMapping(path = "/cancel-card-bind/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<String> cancelCardBind(@PathVariable Source source, @RequestBody Map<String, String> params) {
        logger.info("[Fudian] cancel bind card, params: {}", params);

        String loginName = params.get("loginName");
        String mobile = params.get("mobile");
        String bankUserName = params.get("bankUserName");
        String bankAccountNo = params.get("bankAccountNo");

        if (isBadRequest(Lists.newArrayList(loginName, mobile, bankUserName, bankAccountNo))) {
            return ResponseEntity.badRequest().build();
        }

        CancelCardBindRequestDto requestDto = cancelCardBindService.cancel(source, loginName, mobile, bankUserName, bankAccountNo);
        return this.generateAsyncRequestData(requestDto, ApiType.CANCEL_CARD_BIND);
    }

    @RequestMapping(path = "/authorization/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<String> authorization(@PathVariable Source source, @RequestBody Map<String, String> params) {
        logger.info("[Fudian] authorization, params: {}", params);

        String loginName = params.get("loginName");
        String mobile = params.get("mobile");
        String bankUserName = params.get("bankUserName");
        String bankAccountNo = params.get("bankAccountNo");

        if (isBadRequest(Lists.newArrayList(loginName, mobile, bankUserName, bankAccountNo))) {
            return ResponseEntity.badRequest().build();
        }

        AuthorizationRequestDto requestDto = authorizationService.auth(source, loginName, mobile, bankUserName, bankAccountNo);
        return this.generateAsyncRequestData(requestDto, ApiType.AUTHORIZATION);
    }

    @RequestMapping(path = "/password-reset/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<String> passwordReset(@PathVariable Source source, @RequestBody Map<String, String> params) {
        logger.info("[Fudian] password reset, params: {}", params);

        String loginName = params.get("loginName");
        String mobile = params.get("mobile");
        String bankUserName = params.get("bankUserName");
        String bankAccountNo = params.get("bankAccountNo");

        if (isBadRequest(Lists.newArrayList(loginName, mobile, bankUserName, bankAccountNo))) {
            return ResponseEntity.badRequest().build();
        }

        PasswordResetRequestDto requestDto = passwordResetService.reset(source, loginName, mobile, bankUserName, bankAccountNo);
        return this.generateAsyncRequestData(requestDto, ApiType.PASSWORD_RESET);
    }

    @RequestMapping(path = "/phone-update/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<String> phoneUpdate(@PathVariable Source source, @RequestBody Map<String, String> params) {
        logger.info("[Fudian] phone update, params: {}", params);

        String loginName = params.get("loginName");
        String mobile = params.get("mobile");
        String bankUserName = params.get("bankUserName");
        String bankAccountNo = params.get("bankAccountNo");
        String newPhone = params.get("newPhone");

        if (isBadRequest(Lists.newArrayList(loginName, mobile, bankUserName, bankAccountNo, newPhone))) {
            return ResponseEntity.badRequest().build();
        }

        PhoneUpdateRequestDto requestDto = phoneUpdateService.update(source, loginName, mobile, bankUserName, bankAccountNo, newPhone);
        return this.generateAsyncRequestData(requestDto, ApiType.PHONE_UPDATE);
    }

    private boolean isBadRequest(List<String> values) {
        return Lists.newArrayList(values).stream().anyMatch(Strings::isNullOrEmpty);
    }
}
