package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanRepayDto;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.LoanRepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/loan-repay")
public class LoanRepayController {
    @Autowired
    private LoanRepayService loanRepayService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView findLoanRepayPagination(int index,int pageSize,String loanId,
                                                String loginName,String repayStartDate,String repayEndDate,RepayStatus repayStatus){
        ModelAndView modelAndView = new ModelAndView("/loan-repay");
        BaseDto<BasePaginationDataDto> baseDto = loanRepayService.findLoanRepayPagination(index,pageSize,
                                                                                loanId,loginName,repayStartDate,repayEndDate,repayStatus);
        List<RepayStatus> repayStatusList = Lists.newArrayList(RepayStatus.values());
        modelAndView.addObject("baseDto",baseDto);
        modelAndView.addObject("repayStatusList",repayStatusList);
        modelAndView.addObject("index",index);
        modelAndView.addObject("pageSize",pageSize);
        modelAndView.addObject("loanId",loanId);
        modelAndView.addObject("loginName",loginName);
        modelAndView.addObject("repayStartDate",repayStartDate);
        modelAndView.addObject("repayEndDate",repayEndDate);
        modelAndView.addObject("repayStatus",repayStatus);

        return modelAndView;
    }


}
