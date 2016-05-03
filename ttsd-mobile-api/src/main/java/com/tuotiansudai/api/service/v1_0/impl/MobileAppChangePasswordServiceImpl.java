package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ChangePasswordRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppChangePasswordService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppChangePasswordServiceImpl implements MobileAppChangePasswordService {

    @Autowired
    private UserService userService;

    @Override
    public BaseResponseDto changePassword(ChangePasswordRequestDto requestDto) {
        boolean success = userService.changePassword(
                requestDto.getBaseParam().getUserId(),
                requestDto.getOriginPassword(),
                requestDto.getNewPassword());
        if (success) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        } else {
            return new BaseResponseDto(ReturnMessage.CHANGEPASSWORD_INVALID_PASSWORD);
        }
    }
}
