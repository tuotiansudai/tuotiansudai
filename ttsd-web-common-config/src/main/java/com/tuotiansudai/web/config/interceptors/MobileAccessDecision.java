package com.tuotiansudai.web.config.interceptors;

public class MobileAccessDecision {

    private static ThreadLocal<Boolean> isMobileAccess = new ThreadLocal<>();

    public static void initDecision(Boolean isMobileAccess) {
        MobileAccessDecision.isMobileAccess.set(isMobileAccess);
    }

    public static boolean isMobileAccess() {
        return MobileAccessDecision.isMobileAccess.get() != null && MobileAccessDecision.isMobileAccess.get();
    }
}
