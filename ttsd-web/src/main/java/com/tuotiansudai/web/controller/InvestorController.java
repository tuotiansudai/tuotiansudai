package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestPaginationItemDataDto;
import com.tuotiansudai.dto.InvestRepayDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;

@Controller
@RequestMapping(path = "/investor")
public class InvestorController {

    @Autowired
    private InvestService investService;

    @Autowired
    private RepayService repayService;

    @RequestMapping(value = "/invest-list", method = RequestMethod.GET)
    public ModelAndView investList() {
        return new ModelAndView("/investor-invest-list");
    }

    @RequestMapping(value = "/invest-list-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> investListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                         @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                         @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                         @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                         @RequestParam(name = "status", required = false) LoanStatus status) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<InvestPaginationItemDataDto> dataDto = investService.getInvestPagination(loginName, index, pageSize, startTime, endTime, status);
        dataDto.setStatus(true);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    @RequestMapping(path = "/invest/{investId:^\\d+$}/repay-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<InvestRepayDataDto> investRepayData(@PathVariable long investId) {
        return repayService.findInvestorInvestRepay(LoginUserInfo.getLoginName(), investId);
    }
}
