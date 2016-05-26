package com.tuotiansudai.api.service.v1_0.impl;

import com.google.gson.JsonObject;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.util.GsonUtil;
import com.tuotiansudai.api.util.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class MobileAppChannelDomobService {

    static Log log = LogFactory.getLog(MobileAppChannelDomobService.class);

    private final String appKey;
    private static final String MOBILE_APP_SIGN_KEY_DOMOB = "558f57f870db841d28db071c3a99483d";
    private static final String MOBILE_APP_CHANNEL_DOMOB_CALLBACK_URL_FORMAT =
            "http://e.domob.cn/track/ow/api/callback?appkey={appKey}&acttype=2&ifa={ifa}&acttime={actTime}&sign={sign}";
    private static final int MOBILE_APP_CHANNEL_DOMOB_CALLBACK_RETRY_COUNT = 3;

    static final String CHANNEL_NAME = "domob";

    MobileAppChannelDomobService(String appKey) {
        this.appKey = appKey;
    }

    boolean isDomobChannel(String channelName) {
        return CHANNEL_NAME.equalsIgnoreCase(channelName);
    }

    void sendInstallNotifyDomob(BaseParam param) {
        String ifa = param.getDeviceId();
        String actTime = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = md5(appKey, "", "", ifa, "", MOBILE_APP_SIGN_KEY_DOMOB);

        String url = MOBILE_APP_CHANNEL_DOMOB_CALLBACK_URL_FORMAT
                .replace("{appKey}", appKey)
                .replace("{ifa}", ifa)
                .replace("{actTime}", actTime)
                .replace("{sign}", sign);
        int retryCount = MOBILE_APP_CHANNEL_DOMOB_CALLBACK_RETRY_COUNT;
        while (retryCount > 0) {
            retryCount--;
            if (log.isInfoEnabled()) {
                log.info("send install notify to domob:" + url);
            }
            String respJson = HttpClientUtil.getResponseBodyAsString(url, "utf-8");
            if (log.isInfoEnabled()) {
                log.info("get response from domob:" + respJson);
            }
            JsonObject json = GsonUtil.stringToJsonObject(respJson);
            if (json.get("success").getAsBoolean()) {
                retryCount = 0;
            }
        }
    }


    private String md5(String appkey, String mac, String macmd5, String ifa, String ifamd5, String sign_key) {
        String s = appkey + "," + mac + "," + macmd5 + "," + ifa + "," + ifamd5 + "," + sign_key;
        return DigestUtils.md5Hex(s);
    }

}
