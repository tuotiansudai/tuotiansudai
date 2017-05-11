package com.tuotiansudai.spring.security;

import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MyWebAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String remoteAddress;
    private final String sessionId;
    private final String username;
    private final String mobile;
    private final String source;
    private final String deviceId;
    private final String captcha;
    private final String openid;

    // ~ Constructors
    // ===================================================================================================

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public MyWebAuthenticationDetails(HttpServletRequest request) {
        this.remoteAddress = request.getRemoteAddr();

        HttpSession session = request.getSession(false);
        this.sessionId = (session != null) ? session.getId() : null;

        this.username = request.getParameter("username");
        this.mobile = request.getParameter("mobile");
        this.captcha = request.getParameter("captcha");
        this.source = request.getParameter("source");
        this.deviceId = request.getParameter("deviceId");
        this.openid = request.getParameter("openid");

    }

    // ~ Methods
    // ========================================================================================================

    public boolean equals(Object obj) {
        if (obj instanceof MyWebAuthenticationDetails) {
            MyWebAuthenticationDetails rhs = (MyWebAuthenticationDetails) obj;

            if ((remoteAddress == null) && (rhs.getRemoteAddress() != null)) {
                return false;
            }

            if ((remoteAddress != null) && (rhs.getRemoteAddress() == null)) {
                return false;
            }

            if (remoteAddress != null) {
                if (!remoteAddress.equals(rhs.getRemoteAddress())) {
                    return false;
                }
            }

            if ((sessionId == null) && (rhs.getSessionId() != null)) {
                return false;
            }

            if ((sessionId != null) && (rhs.getSessionId() == null)) {
                return false;
            }

            if (sessionId != null) {
                if (!sessionId.equals(rhs.getSessionId())) {
                    return false;
                }
            }

            if ((source == null) && (rhs.getSource() != null)) {
                return false;
            }

            if ((source != null) && (rhs.getSource() == null)) {
                return false;
            }

            if (source != null) {
                if (!source.equals(rhs.getSource())) {
                    return false;
                }
            }

            if ((deviceId == null) && (rhs.getDeviceId() != null)) {
                return false;
            }

            if ((deviceId != null) && (rhs.getDeviceId() == null)) {
                return false;
            }

            if (deviceId != null) {
                if (!deviceId.equals(rhs.getDeviceId())) {
                    return false;
                }
            }

            if ((captcha == null) && (rhs.getCaptcha() != null)) {
                return false;
            }

            if ((captcha != null) && (rhs.getCaptcha() == null)) {
                return false;
            }

            if (captcha != null) {
                if (!captcha.equals(rhs.getCaptcha())) {
                    return false;
                }
            }

            if ((mobile == null) && (rhs.getMobile() != null)) {
                return false;
            }

            if ((mobile != null) && (rhs.getMobile() == null)) {
                return false;
            }

            if (mobile != null) {
                if (!mobile.equals(rhs.getMobile())) {
                    return false;
                }
            }

            if ((username == null) && (rhs.getUsername() != null)) {
                return false;
            }

            if ((username != null) && (rhs.getUsername() == null)) {
                return false;
            }

            if (username != null) {
                if (!username.equals(rhs.getUsername())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSource() {
        return source;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUsername() {
        return username;
    }

    public String getOpenid() {
        return openid;
    }

    public int hashCode() {
        int code = 7654;

        if (this.remoteAddress != null) {
            code = code * (this.remoteAddress.hashCode() % 7);
        }

        if (this.sessionId != null) {
            code = code * (this.sessionId.hashCode() % 7);
        }

        if (this.source != null) {
            code = code * (this.source.hashCode() % 7);
        }

        if (this.deviceId != null) {
            code = code * (this.deviceId.hashCode() % 7);
        }

        if (this.captcha != null) {
            code = code * (this.captcha.hashCode() % 7);
        }

        if (this.mobile != null) {
            code = code * (this.mobile.hashCode() % 7);
        }

        if (this.username != null) {
            code = code * (this.username.hashCode() % 7);
        }

        if (this.openid != null) {
            code = code * (this.openid.hashCode() % 7);
        }

        return code;
    }

    public String toString() {
        return super.toString() + ": " +
                "RemoteIpAddress: " + this.getRemoteAddress() + "; " +
                "SessionId: " + this.getSessionId() + "; " +
                "Username: " + this.getUsername() + "; " +
                "Mobile: " + this.getMobile() + "; " +
                "Openid: " + this.getOpenid() + "; " +
                "Captcha: " + this.getCaptcha() + "; " +
                "Source: " + this.getSource() + "; " +
                "DeviceId: " + this.getDeviceId();
    }
}
