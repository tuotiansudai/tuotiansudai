package com.ttsd.api.service.impl;

import com.google.common.base.Strings;
import com.ttsd.api.dto.AccessSource;
import com.ttsd.api.dto.BaseParam;
import com.ttsd.api.service.MobileAppChannelService;
import com.ttsd.api.util.CommonUtils;
import com.ttsd.redis.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppChannelServiceImpl implements MobileAppChannelService {
    public static final String MOBILE_APP_ID = "1039233966";

    private static final String MOBILE_APP_CHANNEL_KEY = "MobileAppChannel";
    private static final String MOBILE_APP_CHANNEL_PARAM_KEY_FORMAT = "{type}:{data}";
    private static final String MOBILE_APP_CHANNEL_PARAM_VALUE_FORMAT = "{channel}:{subChannel}:{hasNotified}";

    private static final String INVALID_MAC = "02:00:00:00:00:00";
    private static final String INVALID_MAC_MD5 = CommonUtils.md5Hash(INVALID_MAC);

    @Autowired
    private RedisClient redis;

    private MobileAppChannelDomobService domobService = new MobileAppChannelDomobService(MOBILE_APP_ID);

    @Override
    public boolean recordDeviceId(String type, String data, String channel, String subChannel) {
        if (isValidDeviceId(type, data)) {
            if (!isExistDeviceId(type, data)) {
                redis.hset(MOBILE_APP_CHANNEL_KEY,
                        generateDeviceKey(type, data),
                        new AppChannel(channel, subChannel, false).toString());
                return true;
            }
        }
        return false;
    }

    @Override
    public String obtainChannelBySource(BaseParam baseParam) {
        if (baseParam == null) {
            return "";
        }
        AccessSource source = AccessSource.valueOf(baseParam.getPlatform().toUpperCase());
        if (AccessSource.IOS.equals(source)) {
            String deviceId = baseParam.getDeviceId();
            return findChannelNameByIfa(deviceId);
        }
        return StringUtils.isNotEmpty(baseParam.getChannel()) ? baseParam.getChannel() : "";
    }

    @Override
    public void sendInstallNotify(BaseParam param) {
        if (AccessSource.IOS.name().equalsIgnoreCase(param.getPlatform())) {
            String ifa = param.getDeviceId();
            AppChannel appChannel = findChannelByIfa(ifa);
            if (appChannel == null || appChannel.hasNotified) {
                return;
            }

            String channel = obtainChannelBySource(param);
            if (domobService.isDomobChannel(channel)) {
                domobService.sendInstallNotifyDomob(param);
            }

            appChannel.hasNotified = true;
            redis.hset(MOBILE_APP_CHANNEL_KEY, generateDeviceKey("ifa", ifa), appChannel.toString());
        }
    }

    private AppChannel findChannelByIfa(String ifa) {
        String fullChannel = redis.hget(MOBILE_APP_CHANNEL_KEY, generateDeviceKey("ifa", ifa));
        if (StringUtils.isEmpty(fullChannel)) {
            return null;
        } else {
            return new AppChannel(fullChannel);
        }
    }

    private String findChannelNameByIfa(String ifa) {
        AppChannel channel = findChannelByIfa(ifa);
        if (channel != null) {
            return channel.channel;
        }
        return null;
    }


    private boolean isExistDeviceId(String type, String data) {
        return !Strings.isNullOrEmpty(
                redis.hget(MOBILE_APP_CHANNEL_KEY, generateDeviceKey(type, data)));
    }

    private String generateDeviceKey(String type, String data) {
        return MOBILE_APP_CHANNEL_PARAM_KEY_FORMAT
                .replace("{type}", type)
                .replace("{data}", data)
                .toLowerCase();
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

    private static class AppChannel {
        String channel;
        String subChannel;
        boolean hasNotified;

        AppChannel(String channel, String subChannel, boolean hasNotified) {
            this.channel = channel;
            this.subChannel = subChannel;
            this.hasNotified = hasNotified;
        }

        AppChannel(String channelString) {
            String[] channelParts = channelString.split(":");
            channel = channelParts[0];
            subChannel = channelParts[1];
            hasNotified = "1".equalsIgnoreCase(channelParts[2]);
        }

        @Override
        public String toString() {
            return MOBILE_APP_CHANNEL_PARAM_VALUE_FORMAT
                    .replace("{channel}", channel)
                    .replace("{subChannel}", subChannel)
                    .replace("{hasNotified}", hasNotified ? "1" : "0");
        }
    }
}
