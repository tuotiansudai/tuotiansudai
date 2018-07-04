package com.tuotiansudai.fudian.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.BankRegisterDto;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
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

    @RequestMapping(path = "/register/source/{source}/role/{role}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> register(@PathVariable(name = "source") Source source, @PathVariable(name = "role") RegisterRoleType registerRoleType, @RequestBody BankRegisterDto params) {
        logger.info("[Fudian] call register, params: {}", params);

        RegisterRequestDto requestDto = registerService.register(source, registerRoleType, params);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.REGISTER);

        if (!bankAsyncData.isStatus()) {
            logger.error("[Fudian] call register, request data generation failure, data: {}", bankAsyncData.getData());
        }

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/card-bind/source/{source}/role/{role}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> cardBind(@PathVariable Source source, @PathVariable(name = "role") RegisterRoleType registerRoleType, @RequestBody BankBaseDto params) {
        logger.info("[Fudian] call bind card, params: {}", params);

        if (!params.isValid()) {
            logger.error("[Fudian] call bind card bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        CardBindRequestDto requestDto = cardBindService.bind(source, params, registerRoleType == RegisterRoleType.INVESTOR);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.CARD_BIND);

        if (!bankAsyncData.isStatus()) {
            logger.error("[Fudian] call bind card, request data generation failure, data: {}");
        }

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/cancel-card-bind/source/{source}/role/{role}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> cancelCardBind(@PathVariable Source source, @PathVariable(name = "role") RegisterRoleType registerRoleType, @RequestBody BankBaseDto params) {
        logger.info("[Fudian] call cancel bind card, params: {}", params);

        if (!params.isValid()) {
            logger.error("[Fudian] call bind card bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        CancelCardBindRequestDto requestDto = cancelCardBindService.cancel(source, params, registerRoleType == RegisterRoleType.INVESTOR);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.CANCEL_CARD_BIND);

        if (!bankAsyncData.isStatus()) {
            logger.error("[Fudian] call cancel bind card, request data generation failure, data: {}");
        }

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/authorization/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> authorization(@PathVariable Source source, @RequestBody BankBaseDto params) {
        logger.info("[Fudian] call authorization, params: {}", params);
        if (!params.isValid()) {
            logger.error("[Fudian] call authorization bad request, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        AuthorizationRequestDto requestDto = authorizationService.auth(source, params);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.AUTHORIZATION);

        if (!bankAsyncData.isStatus()) {
            logger.error("[Fudian] call authorization, request data generation failure, data: {}");
        }

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/password-reset/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> passwordReset(@PathVariable Source source, @RequestBody BankBaseDto params) {
        logger.info("[Fudian] call password reset, params: {}", params);

        if (!params.isValid()) {
            logger.error("[Fudian] call password reset, data: {}", params);
            return ResponseEntity.badRequest().build();
        }

        PasswordResetRequestDto requestDto = passwordResetService.reset(source, params);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.PASSWORD_RESET);

        if (!bankAsyncData.isStatus()) {
            logger.error("[Fudian] call password reset, request data generation failure, data: {}");
        }

        return ResponseEntity.ok(bankAsyncData);
    }

    @RequestMapping(path = "/phone-update/source/{source}", method = RequestMethod.POST)
    public ResponseEntity<BankAsyncMessage> phoneUpdate(@PathVariable Source source, @RequestBody Map<String, String> params) {
        logger.info("[Fudian] call phone update, params: {}", params);

        String loginName = params.get("loginName");
        String mobile = params.get("mobile");
        String bankUserName = params.get("bankUserName");
        String bankAccountNo = params.get("bankAccountNo");
        String newPhone = params.get("newPhone");

        if (isBadRequest(Lists.newArrayList(loginName, mobile, bankUserName, bankAccountNo, newPhone))) {
            return ResponseEntity.badRequest().build();
        }

        PhoneUpdateRequestDto requestDto = phoneUpdateService.update(source, loginName, mobile, bankUserName, bankAccountNo, newPhone);

        BankAsyncMessage bankAsyncData = this.generateAsyncRequestData(requestDto, ApiType.PHONE_UPDATE);

        if (!bankAsyncData.isStatus()) {
            logger.error("[Fudian] call phone update, request data generation failure, data: {}");
        }

        return ResponseEntity.ok(bankAsyncData);
    }


}
