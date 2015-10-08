package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.LoanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
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

    @RequestMapping(value = "/console",method = RequestMethod.GET)
    public ModelAndView ConsoleLoanList(@RequestParam("status") LoanStatus status, @RequestParam("loanId") long loanId,
                                        @RequestParam("startTime") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date startTime,
                                        @RequestParam("endTime") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date endTime,
                                        @RequestParam("currentPageNo") int currentPageNo, @RequestParam("loanName") String loanName, @RequestParam("pageSize") int pageSize) {
        int loanListCount = loanService.findLoanListCount(status,loanId,loanName,startTime,endTime);
        List<LoanListDto> loanListDtos = loanService.findLoanList(status,loanId,loanName,startTime,endTime,currentPageNo,pageSize);
        ModelAndView modelAndView = new ModelAndView("/loan-list");
        modelAndView.addObject("loanListCount",loanListCount);
        modelAndView.addObject("loanListDtos",loanListDtos);
        modelAndView.addObject("currentPageNo",currentPageNo);
        modelAndView.addObject("pageSize",pageSize);
        long totalPages = loanListCount / pageSize + (loanListCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
        boolean hasNextPage = currentPageNo < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        modelAndView.addObject("status",status);
        if (loanId > 0) {
            modelAndView.addObject("loanId",loanId);
        }
        modelAndView.addObject("loanName",loanName);
        modelAndView.addObject("startTime",startTime);
        modelAndView.addObject("endTime",endTime);
        return modelAndView;
    }

}
