package com.tuotiansudai.api.controller.v3_0;

import com.tuotiansudai.api.security.MobileAppTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class MobileAppBaseController {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    protected String getLoginName() {
        return mobileAppTokenProvider.getLoginName(httpServletRequest);
    }
}
