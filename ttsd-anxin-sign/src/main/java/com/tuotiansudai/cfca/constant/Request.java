package com.tuotiansudai.cfca.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Request {
    public static final String CHANNEL = "channel";
    public static final String LOCALE = "locale";
    public static final String DATA = "data";
    public static final String SIGNATURE = "signature";

    public static String PLAT_ID = null;

    @Value("${anxin.plat.id}")
    public void setPlatId(String platId) {
        if (Request.PLAT_ID == null) {
            Request.PLAT_ID = platId;
        }
    }
}
