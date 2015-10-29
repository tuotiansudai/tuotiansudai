package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RetrievePasswordRequestDto;
import com.tuotiansudai.api.service.MobileAppRetrievePasswordService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppRetrievePasswordServiceImpl implements MobileAppRetrievePasswordService {
    @Override
    public BaseResponseDto retrievePassword(RetrievePasswordRequestDto retrievePasswordRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto validateAuthCode(RetrievePasswordRequestDto retrievePasswordRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto sendSMS(RetrievePasswordRequestDto retrievePasswordRequestDto, String remoteIp) {
        throw new NotImplementedException(getClass().getName());
    }
}
