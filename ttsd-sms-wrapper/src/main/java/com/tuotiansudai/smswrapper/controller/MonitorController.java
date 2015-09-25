package com.tuotiansudai.smswrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.MonitorDataDto;
import com.tuotiansudai.smswrapper.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/monitor", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<MonitorDataDto> monitor() {
        boolean databaseStatus = monitorService.getDatabaseStatus();
        MonitorDataDto smsMonitorDataDto = new MonitorDataDto();
        smsMonitorDataDto.setService("sms-wrapper");
        smsMonitorDataDto.setDatabaseStatus(databaseStatus);
        smsMonitorDataDto.setStatus(databaseStatus);
        BaseDto<MonitorDataDto> smsMonitorBaseDto = new BaseDto<>();
        smsMonitorBaseDto.setData(smsMonitorDataDto);
        return smsMonitorBaseDto;
    }
}
