package com.ttsd.api.security;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.ttsd.api.dto.ReturnMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MySimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private Map<String, String> appHeaders = Maps.newHashMap();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (isContainsAppHeader(request)) {
            try {
                JSONObject root = new JSONObject();
                root.put("code", ReturnMessage.SUCCESS.getCode());
                root.put("message", ReturnMessage.SUCCESS.getMsg());
                response.setContentType("application/json; charset=UTF-8");
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                try (PrintWriter out = response.getWriter()) {
                    out.print(root);
                }
            } catch (JSONException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        } else {
            super.onLogoutSuccess(request, response, authentication);
        }
    }

    private boolean isContainsAppHeader(final HttpServletRequest httpServletRequest) {
        Optional<Map.Entry<String, String>> optional = Iterators.tryFind(appHeaders.entrySet().iterator(), new Predicate<Map.Entry<String, String>>() {
            @Override
            public boolean apply(Map.Entry<String, String> entry) {
                return entry.getValue().equals(httpServletRequest.getHeader(entry.getKey()));
            }
        });

        return optional.isPresent();
    }

    public void setAppHeaders(Map<String, String> appHeaders) {
        this.appHeaders = appHeaders;
    }
}
