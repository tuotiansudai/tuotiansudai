package com.ttsd.util;

import com.ttsd.aliyun.PropertiesUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/6/15.
 */
public class CommonUtils {

    public static boolean isDevEnvironment(String environment){
        return PropertiesUtils.getPro(environment).equals("dev");
    }

    public static String administratorEmailAddress(){
        return PropertiesUtils.getPro("administratorEmail");
    }

    public static String getRemoteHost(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
}
