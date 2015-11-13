package com.ttsd.api.service;

import com.ttsd.api.dto.BaseParam;

public interface MobileAppChannelService {
    boolean recordChannelDomob(String mac, String macmd5, String ifa, String ifamd5, String subSource);

    String obtainChannelBySource(BaseParam param);

    void sendInstallNotify(BaseParam param);
}
