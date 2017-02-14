package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.AuditLogQueryService;
import com.tuotiansudai.console.service.LoginLogService;
import com.tuotiansudai.console.service.UserOpLogQueryService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoginLogPaginationItemDataDto;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.Source;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;

@Controller
@RequestMapping(path = "/security-log")
public class SecurityLogController {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private AuditLogQueryService auditLogQueryService;

    @Autowired
    private UserOpLogQueryService userOpLogQueryService;

    @RequestMapping(path = "/login-log", method = RequestMethod.GET)
    public ModelAndView loginLog(@RequestParam(name = "mobile", required = false) String mobile,
                                 @RequestParam(name = "selectedYear", required = false) Integer selectedYear,
                                 @RequestParam(name = "selectedMonth", required = false) Integer selectedMonth,
                                 @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                 @RequestParam(name = "success", required = false) Boolean success,
                                 @RequestParam(name = "source", required = false) Source source) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/login-log");

        DateTime now = new DateTime().withTimeAtStartOfDay();
        if (selectedYear == null) {
            selectedYear = now.getYear();
        }
        if (selectedMonth == null) {
            selectedMonth = now.getMonthOfYear();
        }

        if (new DateTime().withDate(selectedYear, selectedMonth, 1).isBeforeNow() &&
                new DateTime().withDate(selectedYear, selectedMonth, new DateTime().withDate(selectedYear, selectedMonth, 1).dayOfMonth().getMaximumValue()).isAfter(new DateTime(2015, 11, 1, 0, 0, 0))) {
            BasePaginationDataDto<LoginLogPaginationItemDataDto> data = loginLogService.getLoginLogPaginationData(mobile, success, source, index, pageSize, selectedYear, selectedMonth);
            modelAndView.addObject("data", data);
        } else {
            modelAndView.addObject("data", new BasePaginationDataDto<>(1, pageSize, 0, Lists.<LoginLogPaginationItemDataDto>newArrayList()));
        }

        modelAndView.addObject("years", Lists.newArrayList("2015", "2016", "2017", "2018", "2019", "2020"));
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("selectedYear", String.valueOf(selectedYear));
        modelAndView.addObject("selectedMonth", String.valueOf(selectedMonth));
        modelAndView.addObject("success", success);
        modelAndView.addObject("sourceList", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.IOS, Source.MOBILE));
        modelAndView.addObject("source", source);

        return modelAndView;
    }

    @RequestMapping(path = "/audit-log", method = RequestMethod.GET)
    public ModelAndView auditLog(@RequestParam(name = "operationType", required = false) OperationType operationType,
                                 @RequestParam(name = "targetId", required = false) String targetId,
                                 @RequestParam(name = "operatorMobile", required = false) String operatorMobile,
                                 @RequestParam(name = "auditorMobile", required = false) String auditorMobile,
                                 @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                 @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                 @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        BasePaginationDataDto<AuditLogModel> data = auditLogQueryService.getAuditLogPaginationData(operationType, targetId, operatorMobile, auditorMobile, startTime, endTime, index, pageSize);

        ModelAndView modelAndView = new ModelAndView("/audit-log");

        modelAndView.addObject("data", data);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("targetId", targetId);
        modelAndView.addObject("operatorMobile", operatorMobile);
        modelAndView.addObject("auditorMobile", auditorMobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);

        modelAndView.addObject("operationTypes", Lists.newArrayList(OperationType.values()));

        return modelAndView;
    }

    @RequestMapping(path = "/user-op-log", method = RequestMethod.GET)
    public ModelAndView userOpLog(@RequestParam(name = "mobile", required = false) String mobile,
                                  @RequestParam(name = "opType", required = false) UserOpType opType,
                                  @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                  @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                  @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        BasePaginationDataDto<UserOpLogModel> data = userOpLogQueryService.getUserOpLogPaginationData(mobile, opType, startTime, endTime, index, pageSize);

        ModelAndView modelAndView = new ModelAndView("/user-op-log");

        modelAndView.addObject("data", data);
        modelAndView.addObject("opType", opType);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);

        modelAndView.addObject("opTypes", Lists.newArrayList(UserOpType.values()));
        return modelAndView;
    }

}
