package com.tuotiansudai.web.config.freemarker;

import com.tuotiansudai.web.config.interceptors.MobileAccessDecision;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Locale;

public class MobileDecisionFreeMarkerViewResolver extends FreeMarkerViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName.startsWith(REDIRECT_URL_PREFIX) || viewName.startsWith(FORWARD_URL_PREFIX)) {
            return super.resolveViewName(viewName, locale);
        }

        boolean isMobileAccess = MobileAccessDecision.isMobileAccess();

        if (isMobileAccess) {
            viewName = viewName.endsWith("_m") ? viewName : viewName + "_m";
            View view = super.resolveViewName(viewName, locale);
            if (view == null) {
                return super.resolveViewName("/error/404_m", locale);
            }
        }

        return super.resolveViewName(viewName, locale);
    }
}
