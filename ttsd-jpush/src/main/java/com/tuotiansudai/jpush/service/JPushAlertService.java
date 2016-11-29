package com.tuotiansudai.jpush.service;


import com.tuotiansudai.jpush.dto.JPushAlertDto;

public interface JPushAlertService {
    void buildJPushAlert(String editBy, JPushAlertDto jPushAlertDto);

    void manualJPushAlert(long id);

    void reject(String loginName, long id, String ip);

    void delete(String loginName, long id);

    void storeJPushId(String loginName, String platform, String jPushId);

    void delStoreJPushId(String loginName);
}
