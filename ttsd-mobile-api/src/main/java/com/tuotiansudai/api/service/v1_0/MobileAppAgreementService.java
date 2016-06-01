package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppAgreementService {

    BaseResponseDto generateAgreementRequest(AgreementOperateRequestDto requestDto);

}
