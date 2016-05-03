package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.service.MobileAppClientStatistics;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MobileAppClientStatisticsImpl implements MobileAppClientStatistics {

    private static final String MOBILE_APP_STAT_KEY_FORMAT = "MobileAppStat:{date}";
    private static final String MOBILE_APP_STAT_PARAM_ALL_KEY_FORMAT =
            "Mobile:{platform}:{appVersion}:{osVersion}:{deviceId}:{deviceModel}:{screenW}:{screenH}";

    @Autowired
    RedisWrapperClient redis;

    @Override
    public void statClientParams(BaseParam param) {
        String key = generateKey();
        increase(key, generateAHKey(param));
        increase(key, generateHKey("platform", param.getPlatform()));
        increase(key, generateHKey("appVersion", param.getAppVersion()));
        increase(key, generateHKey("osVersion", param.getOsVersion()));
        increase(key, generateHKey("deviceId", param.getDeviceId()));
        increase(key, generateHKey("deviceModel", param.getDeviceModel()));
        increase(key, generateHKey("screenW", param.getScreenW()));
        increase(key, generateHKey("screenH", param.getScreenH()));
    }

    private void increase(String key, String hkey) {
        String newValue = "1";
        String oldValue = redis.hget(key, hkey);
        if (StringUtils.isNumeric(oldValue)) {
            newValue = String.valueOf(Integer.parseInt(oldValue) + 1);
        }
        redis.hset(key, hkey, newValue);
    }

    private String generateKey() {
        return MOBILE_APP_STAT_KEY_FORMAT.replace("{date}",
                new SimpleDateFormat("yyyyMM").format(new Date()));
    }

    private String generateHKey(String paramName, String paramValue) {
        return paramName + ":" + paramValue;
    }

    private String generateAHKey(BaseParam param) {
        return MOBILE_APP_STAT_PARAM_ALL_KEY_FORMAT
                .replace("{platform}", param.getPlatform())
                .replace("{appVersion}", param.getAppVersion())
                .replace("{osVersion}", param.getOsVersion())
                .replace("{deviceId}", param.getDeviceId())
                .replace("{deviceModel}", param.getDeviceModel())
                .replace("{screenW}", param.getScreenW())
                .replace("{screenH}", param.getScreenH());
    }
}

