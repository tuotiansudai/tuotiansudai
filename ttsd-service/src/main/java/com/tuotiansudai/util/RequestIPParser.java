package com.tuotiansudai.util;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RequestIPParser {

    private static Logger logger = Logger.getLogger(RequestIPParser.class);

    private static String UNKNOWN = "unknown";

    public static String parse(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (Strings.isNullOrEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("proxy-client-ip");
        }

        if (Strings.isNullOrEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("wl-proxy-client-ip");
        }

        if (Strings.isNullOrEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();

            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {

                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }

        return Strings.isNullOrEmpty(ip) ? UNKNOWN : ip.split(",")[0];
    }
}
