package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


@Controller
@RequestMapping(value = "/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable long loanId) {
        BaseDto dto = loanService.getLoanDetail(loanId);
        ModelAndView view = new ModelAndView("/loan");
        view.addObject("baseDto", dto);
        return view;
    }

    @RequestMapping(value = "/{loanId}/index/{index:^\\d+$}/pagesize/{pagesize:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto getInvestList(@PathVariable long loanId, @PathVariable int index, @PathVariable int pagesize) {
        BaseDto dto = loanService.getInvests(loanId, index, pagesize);
        return dto;

    }

}
