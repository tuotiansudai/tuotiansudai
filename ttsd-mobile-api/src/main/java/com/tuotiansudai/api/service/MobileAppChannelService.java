package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParam;

public interface MobileAppChannelService {
    boolean recordDeviceId(String type, String data, String channel, String subChannel);

    String obtainChannelBySource(BaseParam param);

    void sendInstallNotify(BaseParam param);
}
