package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.MonitorDataDto;
import com.tuotiansudai.service.MonitorService;
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

    @Autowired
    private PayWrapperClient payWrapperClient;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<MonitorDataDto> monitor() {
        boolean databaseStatus = monitorService.getDatabaseStatus();
        boolean redisStatus = monitorService.getRedisStatus();
        MonitorDataDto webMonitorDataDto = new MonitorDataDto();
        webMonitorDataDto.setService("web");
        webMonitorDataDto.setDatabaseStatus(databaseStatus);
        webMonitorDataDto.setRedisStatus(redisStatus);
        webMonitorDataDto.setStatus(databaseStatus);
        BaseDto<MonitorDataDto> webMonitorBaseDto = new BaseDto<>();
        webMonitorBaseDto.setData(webMonitorDataDto);

        BaseDto<MonitorDataDto> smsMonitorBaseDto = smsWrapperClient.monitor();
        BaseDto<MonitorDataDto> payMonitorBaseDto = payWrapperClient.monitor();

        webMonitorDataDto.getDependence().add(smsMonitorBaseDto.getData());
        webMonitorDataDto.getDependence().add(payMonitorBaseDto.getData());

        return webMonitorBaseDto;
    }
}
