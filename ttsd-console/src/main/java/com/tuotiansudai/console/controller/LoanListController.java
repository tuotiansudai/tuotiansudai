package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.LoanListDto;
import org.apache.log4j.Logger;
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

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView loanList(@Valid @ModelAttribute LoanListDto loanListDto) {

        return new ModelAndView("/loanList");
    }


}
