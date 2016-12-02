package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.IdentityNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseResponseDto<CertificationResponseDataDto> validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) {
        RegisterAccountDto registerAccountDto = certificationRequestDto.convertToRegisterAccountDto();
        UserModel userModel = userMapper.findByLoginName(registerAccountDto.getLoginName());
        if (accountMapper.findByLoginName(registerAccountDto.getLoginName()) != null) {
            CertificationResponseDataDto certificationResponseDataDto = new CertificationResponseDataDto();
            certificationResponseDataDto.setUserIdCardNumber(userModel.getIdentityNumber());
            certificationResponseDataDto.setUserRealName(userModel.getUserName());
            BaseResponseDto<CertificationResponseDataDto> baseResponseDto = new BaseResponseDto<>();
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            baseResponseDto.setData(certificationResponseDataDto);
            return baseResponseDto;
        }

        if (!IdentityNumberValidator.validateIdentity(certificationRequestDto.getUserIdCardNumber())) {
            return new BaseResponseDto(ReturnMessage.CERTIFICATION_FAIL.getCode(), ReturnMessage.CERTIFICATION_FAIL.getMsg());
        }

        if (userService.isIdentityNumberExist(certificationRequestDto.getUserIdCardNumber())) {
            return new BaseResponseDto(ReturnMessage.ID_CARD_IS_EXIST.getCode(), ReturnMessage.ID_CARD_IS_EXIST.getMsg());
        }

        BaseDto<PayDataDto> dto = userService.registerAccount(registerAccountDto);
        if (dto.getData().getStatus()) {
            CertificationResponseDataDto certificationResponseDataDto = new CertificationResponseDataDto();
            certificationResponseDataDto.setUserIdCardNumber(certificationRequestDto.getUserIdCardNumber());
            certificationResponseDataDto.setUserRealName(certificationRequestDto.getUserRealName());
            BaseResponseDto<CertificationResponseDataDto> baseResponseDto = new BaseResponseDto<>();
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(dto.getData().getMessage());
            baseResponseDto.setData(certificationResponseDataDto);
            return baseResponseDto;
        } else {
            return new BaseResponseDto<>(dto.getData().getCode(), dto.getData().getMessage());
        }

    }
}
