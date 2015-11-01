package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.dto.CertificationResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService {
    @Autowired
    private UserService userService;

    @Override
    public BaseResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) {
        RegisterAccountDto registerAccountDto = certificationRequestDto.convertToRegisterAccountDto();
        BaseDto<PayDataDto> dto = userService.registerAccount(registerAccountDto);
        if(dto.getData().getStatus()){
            CertificationResponseDataDto certificationResponseDataDto = new CertificationResponseDataDto();
            certificationResponseDataDto.setUserIdCardNumber(certificationRequestDto.getUserIdCardNumber());
            certificationResponseDataDto.setUserRealName(certificationRequestDto.getUserRealName());
            BaseResponseDto<CertificationResponseDataDto> baseResponseDto = new BaseResponseDto();
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(dto.getData().getMessage());
            baseResponseDto.setData(certificationResponseDataDto);
            return baseResponseDto;
        }else{
            return new BaseResponseDto(dto.getData().getCode(),dto.getData().getMessage());
        }

    }
}
