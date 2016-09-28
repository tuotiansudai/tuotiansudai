package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.LoanApplicationView;
import com.tuotiansudai.service.LoanApplicationService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/loan-application")
public class LoanApplicationController {

    @Autowired
    LoanApplicationService loanApplicationService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    ModelAndView getViewList(@RequestParam(value = "index", defaultValue = "1") int index,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/loan-application-list");
        BasePaginationDataDto<LoanApplicationView> basePaginationDataDto = loanApplicationService.getPagination(index, pageSize);
        modelAndView.addObject("dataDto", basePaginationDataDto);

        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    BaseDto<BaseDataDto> editView(@RequestBody LoanApplicationView loanApplicationView) {
        loanApplicationView.setUpdatedBy(LoginUserInfo.getLoginName());
        return loanApplicationService.update(loanApplicationView);
    }
}
