package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.api.security.MobileAppTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/v2.0")
public abstract class MobileAppBaseController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    protected String getLoginName() {
        return mobileAppTokenProvider.getLoginName(httpServletRequest);
    }
}
