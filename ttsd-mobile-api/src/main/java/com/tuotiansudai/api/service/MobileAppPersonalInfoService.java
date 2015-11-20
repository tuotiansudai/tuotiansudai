package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;

public interface MobileAppPersonalInfoService {
    BaseResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto);
}
