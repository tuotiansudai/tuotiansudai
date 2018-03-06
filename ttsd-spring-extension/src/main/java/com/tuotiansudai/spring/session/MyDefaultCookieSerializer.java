package com.tuotiansudai.spring.session;

import org.springframework.session.web.http.CookieSerializer;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class MyDefaultCookieSerializer implements CookieSerializer {

    private String cookieName = "TSID";

    private String cookiePath;

    private int cookieMaxAge = -1;

    private static Pattern IP_PATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public List<String> readCookieValues(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<String> matchingCookieValues = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (this.cookieName.equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    if (sessionId == null) {
                        continue;
                    }
                    matchingCookieValues.add(sessionId);
                }
            }
        }
        return matchingCookieValues;
    }

    public void writeCookieValue(CookieValue cookieValue) {
        HttpServletRequest request = cookieValue.getRequest();
        HttpServletResponse response = cookieValue.getResponse();

        String requestedCookieValue = cookieValue.getCookieValue();

        Cookie sessionCookie = new Cookie(this.cookieName, requestedCookieValue);
        sessionCookie.setSecure(request.isSecure());
        sessionCookie.setPath(getCookiePath(request));
        String domainName = getDomainName(request);
        if (domainName != null) {
            sessionCookie.setDomain(domainName);
        }

        sessionCookie.setHttpOnly(true);

        if ("".equals(requestedCookieValue)) {
            sessionCookie.setMaxAge(0);
        } else {
            sessionCookie.setMaxAge(this.cookieMaxAge);
        }

        response.addCookie(sessionCookie);
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    private String getDomainName(HttpServletRequest request) {
        String serverName = request.getServerName();
        if (IP_PATTERN.matcher(serverName).matches()) {
            return serverName;
        }

        String[] split = serverName.split("\\.");
        if (split.length == 1) {
            return serverName;
        }

        return MessageFormat.format(".{0}.{1}", split[split.length - 2], split[split.length - 1]);
    }

    private String getCookiePath(HttpServletRequest request) {
        if (this.cookiePath == null) {
            return "/";
        }
        return this.cookiePath;
    }
}
