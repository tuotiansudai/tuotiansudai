package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RepayCalendarRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppRepayCalendarController extends MobileAppBaseController {

    @Autowired
    private MobileAppRepayCalendarService mobileAppRepayCalendarService;

    @RequestMapping(value="/get/year-repay-calendar",method = RequestMethod.POST)
    public BaseResponseDto getYearRepayCalendar(@RequestBody RepayCalendarRequestDto repayCalendarRequestDto){
        return mobileAppRepayCalendarService.getYearRepayCalendar(repayCalendarRequestDto);
    }


    @RequestMapping(value="/get/month-repay-calendar",method = RequestMethod.POST)
    public BaseResponseDto getMonthRepayCalendar(@RequestBody RepayCalendarRequestDto repayCalendarRequestDto){
        return mobileAppRepayCalendarService.getMonthRepayCalendar(repayCalendarRequestDto);
    }

    @RequestMapping(value="/get/date-repay-calendar",method = RequestMethod.POST)
    public BaseResponseDto getDateRepayCalendar(@RequestBody RepayCalendarRequestDto repayCalendarRequestDto){
        return mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
    }
}
