package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v2.0")
public abstract class MobileAppBaseController {

    protected String getLoginName() {
        return LoginUserInfo.getLoginName();
    }
}
