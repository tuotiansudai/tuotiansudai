package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleInvestService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.util.CalculateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/finance-manage")
public class InvestController {

    @Autowired
    private ConsoleInvestService consoleInvestService;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private RepayService repayService;

    @RequestMapping(value = "/invests", method = RequestMethod.GET)
    public ModelAndView getInvestList(@RequestParam(name = "loanId", required = false) Long loanId,
                                      @RequestParam(name = "mobile", required = false) String investorMobile,
                                      @RequestParam(name = "usedPreferenceType", required = false) PreferenceType preferenceType,
                                      @RequestParam(name = "channel", required = false) String channel,
                                      @RequestParam(name = "source", required = false) Source source,
                                      @RequestParam(name = "role", required = false) Role role,
                                      @RequestParam(name = "investStatus", required = false) InvestStatus investStatus,
                                      @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(name = "transferStatus", required = false) TransferStatus transferStatus,
                                      @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        InvestPaginationDataDto dataDto = consoleInvestService.getInvestPagination(loanId, investorMobile, channel, source, role,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                investStatus, preferenceType, null, transferStatus, index, pageSize);
        List<String> channelList = consoleInvestService.findAllChannel();
        ModelAndView mv = new ModelAndView("/invest-list");
        mv.addObject("data", dataDto);
        mv.addObject("mobile", investorMobile);
        mv.addObject("channel", channel);
        mv.addObject("loanId", loanId);
        mv.addObject("source", source);
        mv.addObject("role", role);
        mv.addObject("preferenceTypes", PreferenceType.values());
        mv.addObject("selectedPreferenceType", preferenceType);
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("investStatus", investStatus);
        mv.addObject("investStatusList", InvestStatus.values());
        mv.addObject("channelList", channelList);
        mv.addObject("sourceList", Source.values());
        mv.addObject("roleList", Lists.newArrayList(Role.values()).stream().filter(r -> !Lists.newArrayList(Role.AGENT).contains(r)).collect(Collectors.toList()));
        mv.addObject("transferStatus", transferStatus);
        return mv;
    }

    @RequestMapping(value = "/invest-repay/{investId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getInvestRepayList(@PathVariable long investId) {
        InvestModel investModel = investService.findById(investId);

        BaseDto<InvestRepayDataDto> investRepayDto = repayService.findInvestorInvestRepay(investModel.getLoginName(), investModel.getId());
        InvestRepayDataDto data = investRepayDto.getData();

        ModelAndView mv = new ModelAndView("invest-repay-list");
        mv.addObject("data", data);
        mv.addObject("invest", investModel);
        mv.addObject("loan", loanService.findLoanById(investModel.getLoanId()));
        return mv;
    }

    @RequestMapping(value = "/update/invest/{investId:^\\d+$}/transfer-status", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto updateTransfer(@PathVariable long investId) {
        return new BaseDataDto(consoleInvestService.updateInvestTransferStatus(investId));
    }
}
