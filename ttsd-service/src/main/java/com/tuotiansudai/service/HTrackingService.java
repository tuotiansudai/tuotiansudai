package com.tuotiansudai.service;


import com.tuotiansudai.dto.BaseDto;

public interface HTrackingService {

    BaseDto save(String mobile, String deviceId);
}
