package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestPaginationItemDataDto;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.InvestService;
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
public class InvestController {

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/invests", method = RequestMethod.GET)
    public ModelAndView getInvestList(@RequestParam(name = "loanId", required = false, defaultValue = "0") long loanId,
                                      @RequestParam(name = "loginName", required = false) String loginName,
                                      @RequestParam(name = "status", required = false) InvestStatus status,
                                      @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                      @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                      @RequestParam(name = "startTime", required = false, defaultValue = "1970-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(name = "endTime", required = false, defaultValue = "9999-12-31") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {

        BasePaginationDataDto<InvestPaginationItemDataDto> dataDto = investService.getInvestPagination(loginName, loanId, index, pageSize, startTime, endTime, status, null);

        ModelAndView mv = new ModelAndView("/invest-list");
        mv.addObject("data", dataDto);
        mv.addObject("loanId", loanId);
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("investStatus", status);
        mv.addObject("investStatusList", InvestStatus.values());
        return mv;
    }
}
