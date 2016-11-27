package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCertificationService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    private final static Pattern pattern = Pattern.compile("^\\d{17}[0-9xX]$");

    @Override
    public BaseResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) {
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
        if (!idCardNumberIsLegal(certificationRequestDto.getUserIdCardNumber())) {
            return new BaseResponseDto(ReturnMessage.ID_CARD_FORMAT_ERROR.getCode(), ReturnMessage.ID_CARD_FORMAT_ERROR.getMsg());
        }
        if (userService.isIdentityNumberExist(certificationRequestDto.getUserIdCardNumber())) {
            return new BaseResponseDto(ReturnMessage.ID_CARD_IS_EXIST.getCode(), ReturnMessage.ID_CARD_IS_EXIST.getMsg());
        }
        if (!ageIsLegal(certificationRequestDto.getUserIdCardNumber())) {
            return new BaseResponseDto(ReturnMessage.AGE_IS_ILLEGAL.getCode(), ReturnMessage.AGE_IS_ILLEGAL.getMsg());
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
            return new BaseResponseDto(dto.getData().getCode(), dto.getData().getMessage());
        }

    }

    private boolean idCardNumberIsLegal(String idCardNumber) {
        return pattern.matcher(idCardNumber).matches();
    }

    private boolean ageIsLegal(String idCardNumber) {
        final int LEGAL_AGE = 18;
        DateTime birthday = UserBirthdayUtil.getUserBirthday(idCardNumber);
        if(null == birthday) {
            return false;
        }
        Period period = new Period(birthday, DateTime.now());
        return period.getYears() >= LEGAL_AGE;
    }
}
