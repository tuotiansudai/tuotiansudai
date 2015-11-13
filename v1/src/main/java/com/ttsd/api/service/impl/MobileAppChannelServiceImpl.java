package com.ttsd.api.service.impl;

import com.esoft.core.util.HttpClientUtil;
import com.google.common.base.Strings;
import com.ttsd.api.dto.BaseParam;
import com.ttsd.api.service.MobileAppChannelService;
import com.ttsd.redis.RedisClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class MobileAppChannelServiceImpl implements MobileAppChannelService {
    public static final String MOBILE_APP_ID_DOMOB = "3232323";
    private static final String MOBILE_APP_SIGN_KEY_DOMOB = "123123";
    private static final String MOBILE_APP_CHANNEL_DOMOB = "domob";
    private static final String MOBILE_APP_CHANNEL_DOMOB_CALLBACK_URL_FORMAT =
            "http://e.domob.cn/track/ow/api/callback?appkey=" + MOBILE_APP_ID_DOMOB +
                    "&acttype=2&ifa={ifa}&acttime={actTime}&sign={sign}";
    private static final int MOBILE_APP_CHANNEL_DOMOB_CALLBACK_RETRY_COUNT = 3;

    private static final String MOBILE_APP_CHANNEL_KEY = "MobileAppChannel";
    private static final String MOBILE_APP_CHANNEL_PARAM_KEY_FORMAT = "{type}:{data}";
    private static final String MOBILE_APP_CHANNEL_PARAM_VALUE_FORMAT = "{channel}:{subChannel}";

    private static final String INVALID_MAC = "02:00:00:00:00:00";
    private static final String INVALID_MAC_MD5 = md5("02:00:00:00:00:00");

    private static final Md5PasswordEncoder md5Encoder = new Md5PasswordEncoder();

    @Autowired
    private RedisClient redis;


    @Override
    public boolean recordChannelDomob(String mac, String macmd5, String ifa, String ifamd5, String subChannel) {
        boolean r1 = recordDeviceId("mac", mac, subChannel);
        boolean r2 = recordDeviceId("macmd5", macmd5, subChannel);
        boolean r3 = recordDeviceId("ifa", ifa, subChannel);
        boolean r4 = recordDeviceId("ifamd5", ifamd5, subChannel);
        return (r1 || r2 || r3 || r4);
    }

    @Override
    public String obtainChannelBySource(BaseParam param) {
        //findChannelByIfa(param.getDeviceId());
        return null;
    }

    @Override
    public void sendInstallNotify(BaseParam param) {
        String channel = obtainChannelBySource(param);
        if (MOBILE_APP_CHANNEL_DOMOB.equalsIgnoreCase(channel)) {
            sendInstallNotifyDomob(param);
        }
    }

    private void sendInstallNotifyDomob(BaseParam param) {
        String ifa = param.getDeviceId();
        String actTime = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = md5(MOBILE_APP_ID_DOMOB, "", "", ifa, "", MOBILE_APP_SIGN_KEY_DOMOB);

        String url = MOBILE_APP_CHANNEL_DOMOB_CALLBACK_URL_FORMAT
                .replace("{ifa}", ifa)
                .replace("{actTime}", actTime)
                .replace("{sign}", sign);
        int retryCount = MOBILE_APP_CHANNEL_DOMOB_CALLBACK_RETRY_COUNT;
        while (retryCount > 0) {
            retryCount--;
            String respJson = HttpClientUtil.getResponseBodyAsString(url, "utf-8");
            JSONObject json = JSONObject.fromObject(respJson);
            if (json.getBoolean("success")) {
                retryCount = 0;
            }
        }
    }

    public String findChannelByIfa(String ifa) {
        String channelString = redis.hget(MOBILE_APP_CHANNEL_KEY, generateDeviceKey("ifa", ifa));
        if (Strings.isNullOrEmpty(channelString)) {
            return null;
        } else {
            return channelString.split(":")[0];
        }
    }

    private boolean recordDeviceId(String type, String data, String subChannel) {
        if (isValidDeviceId(type, data)) {
            if (!isExistDeviceId(type, data)) {
                redis.hset(MOBILE_APP_CHANNEL_KEY,
                        generateDeviceKey(type, data),
                        generateChannelValue(MOBILE_APP_CHANNEL_DOMOB, subChannel));
                return true;
            }
        }
        return false;
    }

    private boolean isValidDeviceId(String type, String data) {
        if (Strings.isNullOrEmpty(data)) {
            return false;
        }
        if ("mac".equalsIgnoreCase(type) && INVALID_MAC.equalsIgnoreCase(data)) {
            return false;
        }
        if ("macmd5".equalsIgnoreCase(type) && INVALID_MAC_MD5.equalsIgnoreCase(data)) {
            return false;
        }
        return true;
    }

    private boolean isExistDeviceId(String type, String data) {
        return !Strings.isNullOrEmpty(
                redis.hget(MOBILE_APP_CHANNEL_KEY, generateDeviceKey(type, data)));
    }

    private String generateDeviceKey(String type, String data) {
        return MOBILE_APP_CHANNEL_PARAM_KEY_FORMAT
                .replace("{type}", type)
                .replace("{data}", data);
    }

    private String generateChannelValue(String channel, String subChannel) {
        return MOBILE_APP_CHANNEL_PARAM_VALUE_FORMAT
                .replace("channel", channel)
                .replace("subChannel", subChannel);
    }

    private static String md5(String appkey, String mac, String macmd5, String ifa, String ifamd5, String sign_key) {
        String s = appkey + "," + mac + "," + macmd5 + "," + ifa + "," + ifamd5 + "," + sign_key;
        return md5(s);
    }

    private static String md5(String string) {
        return md5Encoder.encodePassword(string, null);
    }
}
