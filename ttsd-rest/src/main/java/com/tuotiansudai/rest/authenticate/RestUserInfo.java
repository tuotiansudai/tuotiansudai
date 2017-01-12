package com.tuotiansudai.rest.authenticate;

public class RestUserInfo {
    private static ThreadLocal<String> loginName = new ThreadLocal<>();

    public static void setCurrentLoginName(String currentLoginName) {
        loginName.set(currentLoginName);
    }

    public static String getCurrentLoginName() {
        return loginName.get();
    }

    public static void clearCurrentLoginName() {
        loginName.remove();
    }
}
