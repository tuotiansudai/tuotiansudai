package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppRepayCalendarService {
    BaseResponseDto<RepayCalendarListResponseDto> getYearRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto);

    BaseResponseDto<RepayCalendarMonthResponseDto> getMonthRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto);

    BaseResponseDto<RepayCalendarDateListResponseDto> getDateRepayCalendar(RepayCalendarRequestDto repayCalendarRequestDto);
}
