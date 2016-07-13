package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarRequestDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarResponseDto;

public interface MobileAppRepayCalendarService {

    BaseResponseDto<RepayCalendarResponseDto> getYearRepayCalendarBy(RepayCalendarRequestDto repayCalendarRequestDto);
}
