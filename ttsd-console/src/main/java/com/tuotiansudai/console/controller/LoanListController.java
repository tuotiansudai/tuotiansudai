package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.service.LoanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2015/8/25.
 */
@Controller
@RequestMapping("/loanList")
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/status/{status}/loanId/{loanId}/loanName/{loanName}/startTime/{startTime}/endTime/{endTime}/currentPageNo/{currentPageNo}", method = RequestMethod.GET)
    public ModelAndView loanList(@PathVariable String status, @PathVariable String loanId, @PathVariable String loanName, @PathVariable String startTime, @PathVariable String endTime, @PathVariable String currentPageNo) {
        int loanListCount = loanService.findLoanListCount(status,loanId,loanName,startTime,endTime);
        List<LoanListDto> loanListDtos = loanService.findLoanList(status,loanId,loanName,startTime,endTime,currentPageNo);
        ModelAndView modelAndView = new ModelAndView("/loanList");
        modelAndView.addObject("loanListCount",loanListCount);
        modelAndView.addObject("loanListDtos",loanListDtos);
        modelAndView.addObject("currentPageNo",currentPageNo);
        return modelAndView;
    }

}
