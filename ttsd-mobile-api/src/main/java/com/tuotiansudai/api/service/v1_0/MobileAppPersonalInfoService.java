package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PersonalInfoRequestDto;
import com.tuotiansudai.api.dto.v1_0.PersonalInfoResponseDataDto;

public interface MobileAppPersonalInfoService {
    BaseResponseDto<PersonalInfoResponseDataDto> getPersonalInfoData(String loginName);
}
