package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleLoanApplicationService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/loan-application")
public class LoanApplicationController {

    @Autowired
    private ConsoleLoanApplicationService consoleLoanApplicationService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView getViewList(@RequestParam(value = "index", defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/loan-application-list");
        BasePaginationDataDto<LoanApplicationModel> basePaginationDataDto = consoleLoanApplicationService.getPagination(index, pageSize);
        modelAndView.addObject("dataDto", basePaginationDataDto);

        return modelAndView;
    }

    @RequestMapping(value = "/{applyId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable long applyId) {
        ModelAndView modelAndView = new ModelAndView("/loan-application-detail");
        modelAndView.addObject("data", consoleLoanApplicationService.detail(applyId));
        return modelAndView;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> commentLoanApplication(@RequestBody LoanApplicationModel loanApplicationModel) {
        loanApplicationModel.setUpdatedBy(LoginUserInfo.getLoginName());
        return consoleLoanApplicationService.comment(loanApplicationModel);
    }

    @RequestMapping(value = "/consume-detail", method = RequestMethod.GET)
    public ModelAndView consumeList() {
        ModelAndView modelAndView = new ModelAndView("/loan-application-consume-detail");
        return modelAndView;
    }
}
