package com.tuotiansudai.api.service.v3_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;

public interface MobileAppInvestListsV3Service {

    BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto userInvestListRequestDto);

}
