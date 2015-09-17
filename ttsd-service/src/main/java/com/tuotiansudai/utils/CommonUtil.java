package com.tuotiansudai.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * Created by Administrator on 2015/9/17.
 */
public class CommonUtil {

    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                InetAddress inet=null;
                try{
                    inet=InetAddress.getLocalHost();
                }catch(Exception e){
                    e.printStackTrace();
                }
                ip=inet.getHostAddress();
            }
        }
        return ip.split(",")[0];
    }
}
