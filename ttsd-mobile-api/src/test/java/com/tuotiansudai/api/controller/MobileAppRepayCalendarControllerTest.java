package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.controller.v1_0.MobileAppRepayCalendarController;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppRepayCalendarControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppRepayCalendarController mobileAppRepayCalendarController;

    @Mock
    private MobileAppRepayCalendarService mobileAppRepayCalendarService;

    @Override
    protected Object getControllerObject() {
        return mobileAppRepayCalendarController;
    }

    @Test
    public void shouldGetYearRepayCalendarIsOk() throws Exception {
        when(mobileAppRepayCalendarService.getYearRepayCalendar(any(RepayCalendarRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/year-repay-calendar",
                new RepayCalendarRequestDto());
    }

    @Test
    public void shouldGetMonthRepayCalendarIsOk() throws Exception {
        when(mobileAppRepayCalendarService.getMonthRepayCalendar(any(RepayCalendarRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/month-repay-calendar",
                new RepayCalendarRequestDto());
    }

    @Test
    public void shouldGetDateRepayCalendarIsOk() throws Exception {
        when(mobileAppRepayCalendarService.getDateRepayCalendar(any(RepayCalendarRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/date-repay-calendar",
                new RepayCalendarRequestDto());
    }

}
