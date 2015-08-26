package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.service.LoanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Administrator on 2015/8/25.
 */
@Controller
@RequestMapping("/loanList")
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView loanList(@Valid @ModelAttribute LoanListDto loanListDto) {
        loanService.findLoanList(loanListDto);
        return new ModelAndView("/loanList");
    }


}
