package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CertificationResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.IdentityNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService {

    private final UserService userService;

    private final BankAccountService bankAccountService;

    @Autowired
    public MobileAppCertificationServiceImpl(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> certificate(String loginName, String mobile, String userName, String identityNumber, String token, String ip, String deviceId) {
        if (!IdentityNumberValidator.validateIdentity(identityNumber)) {
            return new BaseResponseDto<>(ReturnMessage.CERTIFICATION_FAIL);
        }

        if (userService.isIdentityNumberExist(identityNumber)) {
            return new BaseResponseDto<>(ReturnMessage.ID_CARD_IS_EXIST);
        }

        RegisterAccountDto registerAccountDto = new RegisterAccountDto(loginName, mobile, userName, identityNumber, Source.MOBILE);

        BankAsyncMessage bankAsyncMessage = bankAccountService.registerAccount(registerAccountDto, token, ip, deviceId);

        if (bankAsyncMessage.isStatus()) {
            BaseResponseDto<BankAsynResponseDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
            responseDto.setData(new BankAsynResponseDto(bankAsyncMessage.getUrl(), bankAsyncMessage.getData()));
            return responseDto;
        }

        return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG);
    }
}
