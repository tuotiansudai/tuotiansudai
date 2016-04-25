package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;

public interface MobileAppRetrievePasswordService {
    /**
     * @function 修改密码
     * @param retrievePasswordRequestDto 修改密码参数封装类
     * @return BaseResponseDto
     */
    BaseResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto);

    /**
     * @function 发送手机验证码
     * @param retrievePasswordRequestDto 发送手机验证码参数封装类
     * @return BaseResponseDto
     */
    BaseResponseDto sendSMS(RetrievePasswordRequestDto retrievePasswordRequestDto, String remoteIp);
}
