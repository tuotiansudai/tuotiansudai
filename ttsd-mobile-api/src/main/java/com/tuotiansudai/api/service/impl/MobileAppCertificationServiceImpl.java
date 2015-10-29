package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CertificationRequestDto;
import com.tuotiansudai.api.service.MobileAppCertificationService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService {
    @Override
    public BaseResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto assembleResult(String code, String message, String userRealName, String idCardNumber) {
        throw new NotImplementedException(getClass().getName());
    }
}
