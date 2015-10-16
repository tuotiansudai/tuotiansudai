package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.MonitorDataDto;
import com.tuotiansudai.paywrapper.service.MonitorService;
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

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<MonitorDataDto> monitor() {
        boolean databaseStatus = monitorService.getAADatabaseStatus() && monitorService.getUMPDatabaseStatus();
        MonitorDataDto payMonitorDataDto = new MonitorDataDto();
        payMonitorDataDto.setService("pay-wrapper");
        payMonitorDataDto.setDatabaseStatus(databaseStatus);
        payMonitorDataDto.setStatus(databaseStatus);
        BaseDto<MonitorDataDto> payMonitorBaseDto = new BaseDto<>();
        payMonitorBaseDto.setData(payMonitorDataDto);

        BaseDto<MonitorDataDto> smsMonitorBaseDto = smsWrapperClient.monitor();

        payMonitorDataDto.getDependence().add(smsMonitorBaseDto.getData());

        return payMonitorBaseDto;
    }
}
