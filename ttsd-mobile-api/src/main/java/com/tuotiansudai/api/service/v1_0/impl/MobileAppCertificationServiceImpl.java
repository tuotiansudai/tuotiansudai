package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CertificationRequestDto;
import com.tuotiansudai.api.dto.v1_0.CertificationResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public BaseResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) {
        RegisterAccountDto registerAccountDto = certificationRequestDto.convertToRegisterAccountDto();
        AccountModel accountModel = accountMapper.findByLoginName(registerAccountDto.getLoginName());
        if(accountModel != null){
            CertificationResponseDataDto certificationResponseDataDto = new CertificationResponseDataDto();
            certificationResponseDataDto.setUserIdCardNumber(accountModel.getIdentityNumber());
            certificationResponseDataDto.setUserRealName(accountModel.getUserName());
            BaseResponseDto<CertificationResponseDataDto> baseResponseDto = new BaseResponseDto();
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            baseResponseDto.setData(certificationResponseDataDto);
            return baseResponseDto;
        }
        if (accountService.isIdentityNumberExist(certificationRequestDto.getUserIdCardNumber())) {
            return new BaseResponseDto(ReturnMessage.ID_CARD_IS_EXIST.getCode(), ReturnMessage.ID_CARD_IS_EXIST.getMsg());
        }
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
