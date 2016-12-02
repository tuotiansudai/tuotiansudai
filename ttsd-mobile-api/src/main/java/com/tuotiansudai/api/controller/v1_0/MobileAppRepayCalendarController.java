package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "回款日历")
public class MobileAppRepayCalendarController extends MobileAppBaseController {

    @Autowired
    private MobileAppRepayCalendarService mobileAppRepayCalendarService;

    @RequestMapping(value = "/get/year-repay-calendar", method = RequestMethod.POST)
    @ApiOperation("年回款日历")
    public BaseResponseDto<RepayCalendarListResponseDto> getYearRepayCalendar(@RequestBody RepayCalendarRequestDto repayCalendarRequestDto) {
        return mobileAppRepayCalendarService.getYearRepayCalendar(repayCalendarRequestDto);
    }


    @RequestMapping(value = "/get/month-repay-calendar", method = RequestMethod.POST)
    @ApiOperation("月回款日历")
    public BaseResponseDto<RepayCalendarMonthResponseDto> getMonthRepayCalendar(@RequestBody RepayCalendarRequestDto repayCalendarRequestDto) {
        return mobileAppRepayCalendarService.getMonthRepayCalendar(repayCalendarRequestDto);
    }

    @RequestMapping(value = "/get/date-repay-calendar", method = RequestMethod.POST)
    @ApiOperation("日回款日历")
    public BaseResponseDto<RepayCalendarDateListResponseDto> getDateRepayCalendar(@RequestBody RepayCalendarRequestDto repayCalendarRequestDto) {
        return mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
    }
}
