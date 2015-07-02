package com.ttsd.util;

import com.ttsd.aliyun.PropertiesUtils;

/**
 * Created by Administrator on 2015/6/15.
 */
public class CommonUtils {

    public static boolean isDevEnvironment(String environment){
        return PropertiesUtils.getPro(environment).equals("dev");
    }

    public static String emailAddress(){
        return PropertiesUtils.getPro("email");
    }
}
