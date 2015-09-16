package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BasePaginationDto;
import com.tuotiansudai.dto.InvestDetailDto;
import com.tuotiansudai.dto.InvestDetailQueryDto;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/invests", method = RequestMethod.GET)
    public ModelAndView findAllInvests(Long loanId, String loginName,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                       LoanStatus loanStatus, InvestStatus investStatus,
                                       Integer pageIndex, Integer pageSize) throws Exception {
        InvestDetailQueryDto queryDto = new InvestDetailQueryDto();
        queryDto.setLoanId(loanId);
        queryDto.setLoginName(loginName);
        queryDto.setBeginTime(beginTime);
        queryDto.setEndTime(endTime);
        queryDto.setLoanStatus(loanStatus);
        queryDto.setInvestStatus(investStatus);
        if (pageIndex == null || pageIndex <= 0) {
            queryDto.setPageIndex(1);
        }else{
            queryDto.setPageIndex(pageIndex);
        }
        if (pageSize == null || pageSize <= 0) {
            queryDto.setPageSize(10);
        }else{
            queryDto.setPageSize(pageSize);
        }

        BasePaginationDto<InvestDetailDto> paginationList = investService.queryInvests(queryDto, false);

        List<InvestDetailDto> invests = paginationList.getRecordDtoList();

        ModelAndView mv = new ModelAndView("/invest-list");
        mv.addObject("pagination", paginationList);
        mv.addObject("invests", invests);
        mv.addObject("query", queryDto);
        mv.addObject("investStatusList", InvestStatus.values());
        return mv;
    }
}
