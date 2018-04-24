package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final RegisterService registerService;

    private final CardBindService cardBindService;

    private final CancelCardBindService cancelCardBindService;

    private final AuthorizationService authorizationService;

    private final PasswordResetService passwordResetService;

    private final PhoneUpdateService phoneUpdateService;

    @Autowired
    public UserController(RegisterService registerService, CardBindService cardBindService, CancelCardBindService cancelCardBindService, AuthorizationService authorizationService, PasswordResetService passwordResetService, PhoneUpdateService phoneUpdateService) {
        this.registerService = registerService;
        this.cardBindService = cardBindService;
        this.cancelCardBindService = cancelCardBindService;
        this.authorizationService = authorizationService;
        this.passwordResetService = passwordResetService;
        this.phoneUpdateService = phoneUpdateService;
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String recharge(Map<String, Object> model) {
        logger.info("[Fudian] call register");

        RegisterRequestDto requestDto = registerService.register("朱坤", "340322199503294631", "18895730992");
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.REGISTER.getPath());
        return "post";
    }

    @RequestMapping(path = "/card-bind", method = RequestMethod.GET)
    public String cardBind(Map<String, Object> model) {
        logger.info("[Fudian] bind card");

//        String data = cardBindService.bind("UU02615960791461001", "UA02615960791501001"); //GXD
//        String data = cardBindService.bind("UU02619471098561001", "UA02619471098591001"); //ZK
        CardBindRequestDto requestDto = cardBindService.bind("UU02624634769241001", "UA02624634769281001");//FZW
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.CARD_BIND.getPath());
        return "post";
    }

    @RequestMapping(path = "/cancel-card-bind", method = RequestMethod.GET)
    public String cancelCardBind(Map<String, Object> model) {
        logger.info("[Fudian] cancel bind card");

        CancelCardBindRequestDto requestDto = cancelCardBindService.cancel("UU02619471098561001", "UA02619471098591001");
        model.put("message", requestDto.getRequestData());
        model.put("path", ApiType.CANCEL_CARD_BIND.getPath());
        return "post";
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

    public String getRandomCode(int length) {
        if (length >= 10) {
            throw new RuntimeException("长度必须是10以内");
        }
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < length) {
            int a = random.nextInt(10);
            if (set.add(a)) {
                buffer.append(a);
            }
        }
        return buffer.toString();
    }
}
