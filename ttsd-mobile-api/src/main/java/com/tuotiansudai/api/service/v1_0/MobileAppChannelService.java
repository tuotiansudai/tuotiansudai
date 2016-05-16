package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParam;

public interface MobileAppChannelService {
    boolean recordDeviceId(String type, String data, String channel, String subChannel);

    String obtainChannelBySource(BaseParam param);

    void sendInstallNotify(BaseParam param);
}
