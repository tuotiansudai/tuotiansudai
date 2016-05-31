package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.JpushRequestDto;

public interface MobileAppJpushService {

    BaseResponseDto storeJPushId(JpushRequestDto JPushRequestDto);


}
