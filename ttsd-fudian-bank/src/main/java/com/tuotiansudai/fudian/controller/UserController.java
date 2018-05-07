package com.tuotiansudai.fudian.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final BankConfig bankConfig;

    private final RegisterService registerService;

    private final CardBindService cardBindService;

    private final CancelCardBindService cancelCardBindService;

    private final AuthorizationService authorizationService;

    private final PasswordResetService passwordResetService;

    private final PhoneUpdateService phoneUpdateService;

    @Autowired
    public UserController(BankConfig bankConfig, RegisterService registerService, CardBindService cardBindService, CancelCardBindService cancelCardBindService, AuthorizationService authorizationService, PasswordResetService passwordResetService, PhoneUpdateService phoneUpdateService) {
        this.bankConfig = bankConfig;
        this.registerService = registerService;
        this.cardBindService = cardBindService;
        this.cancelCardBindService = cancelCardBindService;
        this.authorizationService = authorizationService;
        this.passwordResetService = passwordResetService;
        this.phoneUpdateService = phoneUpdateService;
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> recharge(Map<String, String> params) {
        logger.info("[Fudian] call register");
        RegisterRequestDto requestDto = registerService.register(params.get("realName"), params.get("identityCode"), params.get("mobilePhone"));
        return this.generateResponseJson(requestDto, ApiType.REGISTER);
    }

    @RequestMapping(path = "/card-bind", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> cardBind(@RequestBody Map<String, String> params) {
        logger.info("[Fudian] bind card");

//        String data = cardBindService.bind("UU02615960791461001", "UA02615960791501001"); //GXD
//        String data = cardBindService.bind("UU02619471098561001", "UA02619471098591001"); //ZK
        CardBindRequestDto requestDto = cardBindService.bind(params.get("bankUserName"), params.get("bankAccountNo"));//FZW

        return this.generateResponseJson(requestDto, ApiType.CARD_BIND);
    }

    @RequestMapping(path = "/cancel-card-bind", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> cancelCardBind(@RequestBody Map<String, String> params) {
        logger.info("[Fudian] cancel bind card");

        CancelCardBindRequestDto requestDto = cancelCardBindService.cancel(params.get("bankUserName"), params.get("bankAccountNo"));
        return this.generateResponseJson(requestDto, ApiType.CANCEL_CARD_BIND);
    }

    @RequestMapping(path = "/authorization", method = RequestMethod.GET)
    public String authorization(Map<String, Object> model) {
        logger.info("[Fudian] authorization");

        AuthorizationRequestDto requestDto = authorizationService.auth("UU02619471098561001", "UA02619471098591001");
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.AUTHORIZATION.getPath());
        return "post";
    }

    @RequestMapping(path = "/password-reset", method = RequestMethod.GET)
    public String passwordReset(Map<String, Object> model) {
        logger.info("[Fudian] password reset");

        PasswordResetRequestDto requestDto = passwordResetService.reset("UU02619471098561001", "UA02619471098591001");
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.PASSWORD_RESET.getPath());
        return "post";
    }

    @RequestMapping(path = "/phone-update", method = RequestMethod.GET)
    public String phoneUpdate(Map<String, Object> model) {
        logger.info("[Fudian] phone update");

        PhoneUpdateRequestDto requestDto = phoneUpdateService.update("UU02619471098561001", "UA02619471098591001", "18611112222", "2");
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.PHONE_UPDATE.getPath());
        return "post";
    }

    private ResponseEntity<Map<String, String>> generateResponseJson(BaseRequestDto requestDto, ApiType apiType) {
        return ResponseEntity.ok(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("data", requestDto.getRequestData())
                .put("url", bankConfig.getBankUrl() + apiType.getPath())
                .build()));
    }

}
