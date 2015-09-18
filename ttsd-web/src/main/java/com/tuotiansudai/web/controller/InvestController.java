package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setInvestSource(InvestSource.WEB);
        BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }

    @RequestMapping(value = "/investor/invests", method = RequestMethod.GET)
    public ModelAndView userInvest(){
        ModelAndView mv = new ModelAndView("/investrecord");
        return mv;
    }

    @RequestMapping(value = "/investor/query_invests", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<InvestDetailDto> queryUserInvest(
            Long loanId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            LoanStatus loanStatus, InvestStatus investStatus,
            Integer pageIndex, Integer pageSize
    ) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<InvestDetailDto> paginationList = null;
        if (StringUtils.isBlank(loginName)) {
            paginationList = new BasePaginationDataDto<>(pageIndex,pageSize,0,new ArrayList<InvestDetailDto>());
        } else {
            InvestDetailQueryDto queryDto = new InvestDetailQueryDto();
            queryDto.setLoanId(loanId);
            queryDto.setLoginName(loginName);
            queryDto.setBeginTime(beginTime);
            if(endTime!=null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endTime);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                endTime = calendar.getTime();
            }
            queryDto.setEndTime(endTime);
            queryDto.setLoanStatus(loanStatus);
            queryDto.setInvestStatus(investStatus);
            if (pageIndex == null || pageIndex <= 0) {
                queryDto.setPageIndex(1);
            } else {
                queryDto.setPageIndex(pageIndex);
            }
            if (pageSize == null || pageSize <= 0) {
                queryDto.setPageSize(10);
            } else {
                queryDto.setPageSize(pageSize);
            }
            paginationList = investService.queryInvests(queryDto,true);
            List<InvestDetailDto> dtoList = paginationList.getRecords();
            List<InvestDetailDto> jsonDtoList = new ArrayList<>(dtoList.size());
            for (InvestDetailDto dto : dtoList) {
                jsonDtoList.add(new InvestJsonDetailDto(dto));
            }
            paginationList = new BasePaginationDataDto<>(
                    paginationList.getIndex(),
                    paginationList.getPageSize(),
                    paginationList.getCount(),
                    jsonDtoList);
            paginationList.setStatus(true);
        }
        return paginationList;
    }
}
