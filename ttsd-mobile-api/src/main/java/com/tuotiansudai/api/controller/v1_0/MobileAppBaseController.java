package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1.0")
public abstract class MobileAppBaseController {

    protected String getLoginName() {
        return LoginUserInfo.getLoginName();
    }
}
