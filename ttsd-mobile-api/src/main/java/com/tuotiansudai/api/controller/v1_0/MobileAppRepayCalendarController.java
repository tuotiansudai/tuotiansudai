package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.RepayCalendarRequestDto;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppRepayCalendarController extends MobileAppBaseController {

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @RequestMapping(value="/get/year-repay-calendar",method = RequestMethod.GET)
    public void getYearRepayCalendar(@RequestBody RepayCalendarRequestDto repayCalendarRequestDto){

    }
}
