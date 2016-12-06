package com.tuotiansudai.push.service;


import com.tuotiansudai.push.dto.PushCreateDto;

public interface PushService {
    Long createOrUpdate(String createdOrUpdatedBy, PushCreateDto pushCreateDto);

    void delete(long id);

    void storeJPushId(String loginName, String platform, String jPushId);

    void removeJPushId(String loginName);
}
