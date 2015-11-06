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
    public ModelAndView getInvestList(@RequestParam(name = "loanId", required = false) Long loanId,
                                      @RequestParam(name = "loginName", required = false) String investorLoginName,
                                      @RequestParam(name = "investStatus", required = false) InvestStatus investStatus,
                                      @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                      @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                      @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {

        BasePaginationDataDto<InvestPaginationItemDataDto> dataDto = investService.getInvestPagination(loanId, investorLoginName, index, pageSize, startTime, endTime, investStatus, null);

        ModelAndView mv = new ModelAndView("/invest-list");
        mv.addObject("data", dataDto);
        mv.addObject("loginName", investorLoginName);
        mv.addObject("loanId", loanId);
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("investStatus", investStatus);
        mv.addObject("investStatusList", InvestStatus.values());
        return mv;
    }
}
