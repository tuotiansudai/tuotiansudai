package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CertificationRequestDto;
import com.tuotiansudai.api.dto.v1_0.CertificationResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
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
    private AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseResponseDto<CertificationResponseDataDto> validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) {
        UserModel userModel = userMapper.findByLoginName(certificationRequestDto.getBaseParam().getUserId());
        if (userModel == null) {
            userModel = userMapper.findByLoginNameOrMobile(certificationRequestDto.getBaseParam().getPhoneNum());
        }
        RegisterAccountDto registerAccountDto = certificationRequestDto.convertToRegisterAccountDto(userModel);
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
            return new BaseResponseDto<>(ReturnMessage.CERTIFICATION_FAIL.getCode(), ReturnMessage.CERTIFICATION_FAIL.getMsg());
        }

        if (userService.isIdentityNumberExist(certificationRequestDto.getUserIdCardNumber())) {
            return new BaseResponseDto<>(ReturnMessage.ID_CARD_IS_EXIST.getCode(), ReturnMessage.ID_CARD_IS_EXIST.getMsg());
        }

        BaseDto<PayDataDto> dto = accountService.registerAccount(registerAccountDto);
        if (dto.getData().getStatus()) {
            CertificationResponseDataDto certificationResponseDataDto = new CertificationResponseDataDto();
            certificationResponseDataDto.setUserIdCardNumber(certificationRequestDto.getUserIdCardNumber());
            certificationResponseDataDto.setUserRealName(certificationRequestDto.getUserRealName());
            certificationResponseDataDto.setPayUserId(dto.getData().getExtraValues().get("payUserId"));
            certificationResponseDataDto.setPayAccountId(dto.getData().getExtraValues().get("payAccountId"));
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
