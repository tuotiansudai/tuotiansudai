package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ChangePasswordRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppChangePasswordService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MobileAppChangePasswordServiceImpl implements MobileAppChangePasswordService {

    @Autowired
    private UserService userService;

    @Override
    public BaseResponseDto changePassword(ChangePasswordRequestDto requestDto, String ip) {
        boolean success = userService.changePassword(
                requestDto.getBaseParam().getUserId(),
                requestDto.getOriginPassword(),
                requestDto.getNewPassword(),
                ip,
                requestDto.getBaseParam().getPlatform(),
                requestDto.getBaseParam().getDeviceId());
        if (success) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        } else {
            return new BaseResponseDto(ReturnMessage.CHANGEPASSWORD_INVALID_PASSWORD);
        }
    }
}
