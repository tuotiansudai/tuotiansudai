package com.ttsd.api.service;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.RetrievePasswordRequestDto;

/**
 * Created by tuotian on 15/7/29.
 */
public interface MobileAppRetrievePasswordService {
    /**
     * @function 修改密码
     * @param retrievePasswordRequestDto 修改密码参数封装类
     * @return BaseResponseDto
     */
    BaseResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto) throws UserNotFoundException;

    /**
     * @function 校验验证码
     * @param retrievePasswordRequestDto 校验验证码参数封装类
     * @return BaseResponseDto
     */
    BaseResponseDto validateAuthCode(RetrievePasswordRequestDto retrievePasswordRequestDto);

    /**
     * @function 发送手机验证码
     * @param retrievePasswordRequestDto 发送手机验证码参数封装类
     * @return BaseResponseDto
     */
    BaseResponseDto sendSMS(RetrievePasswordRequestDto retrievePasswordRequestDto,String remoteIp);
}
