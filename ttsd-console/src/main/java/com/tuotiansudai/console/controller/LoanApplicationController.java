package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleLoanApplicationService;
import com.tuotiansudai.console.service.ConsoleLoanCreateService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.LoanApplicationStatus;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.LoanRiskManagementTitleModel;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/loan-application")
public class LoanApplicationController {

    @Autowired
    private ConsoleLoanApplicationService consoleLoanApplicationService;

    @Autowired
    private ConsoleLoanCreateService consoleLoanCreateService;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

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

    @RequestMapping(value = "/consume-list", method = RequestMethod.GET)
    public ModelAndView consumeList(@RequestParam(value = "keyWord", required = false) String keyWord,
                                    @RequestParam(value = "status", required = false) LoanApplicationStatus status,
                                    @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                    @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                    @RequestParam(value = "index", defaultValue = "1") int index) {
        ModelAndView modelAndView = new ModelAndView("/loan-application-consume-list");
        modelAndView.addObject("statusList", LoanApplicationStatus.values());
        modelAndView.addObject("dataDto", consoleLoanApplicationService.loanApplicationConsumeList(keyWord, status, startTime, endTime, index, 10));
        modelAndView.addObject("keyWord", keyWord);
        modelAndView.addObject("selectedStatus", status);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView consumeDetail(@PathVariable long applyId) {
        ModelAndView modelAndView = new ModelAndView("/loan-application-consume-detail");
        modelAndView.addObject("data", consoleLoanApplicationService.consumeDetail(applyId));
        return modelAndView;
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/save", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> consumeSave(@PathVariable long applyId, @RequestBody LoanApplicationUpdateDto loanApplicationUpdateDto) {
        return consoleLoanApplicationService.consumeSave(applyId, loanApplicationUpdateDto, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/create-loan", method = RequestMethod.GET)
    public ModelAndView createLoan(@PathVariable long applyId) {
        ModelAndView modelAndView = new ModelAndView("/loan-create");
        modelAndView.addAllObjects(consoleLoanApplicationService.loanParams());
        modelAndView.addObject("loanerDto", consoleLoanApplicationService.findLoanerDetail(applyId));
        modelAndView.addObject("loanApplicationId", String.valueOf(applyId));
        return modelAndView;
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/loan/{loanId:^\\d+$}/edit", method = RequestMethod.GET)
    public ModelAndView loanEdit(@PathVariable long applyId, @PathVariable long loanId) {
        ModelAndView modelAndView = new ModelAndView("/loan-edit");
        modelAndView.addAllObjects(consoleLoanApplicationService.loanParams());
        modelAndView.addObject("loan", consoleLoanCreateService.getEditLoanDetails(loanId));
        modelAndView.addObject("loanApplicationId", String.valueOf(applyId));
        modelAndView.addObject("extraLoanRates", extraLoanRateMapper.findByLoanId(loanId));
        return modelAndView;
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> consumeReject(@PathVariable long applyId) {
        return consoleLoanApplicationService.consumeReject(applyId);
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/submit-audit", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> consumeSubmitAudit(@PathVariable long applyId) {
        return consoleLoanApplicationService.applyAuditLoanApplication(applyId);
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/approve", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> consumeApprove(@PathVariable long applyId, HttpServletRequest request) {
        return consoleLoanApplicationService.consumeApprove(applyId, RequestIPParser.parse(request));
    }

    @RequestMapping(value = "/{applyId:^\\d+$}/titles", method = RequestMethod.GET)
    @ResponseBody
    public List<LoanRiskManagementDetailDto> findAllTitles(@PathVariable long applyId) {
        return consoleLoanApplicationService.findAllTitleDetail(applyId);
    }

    @RequestMapping(value = "/title", method = RequestMethod.POST)
    @ResponseBody
    public LoanRiskManagementTitleModel addTitle(@RequestParam(value = "title") String title) {
        return consoleLoanApplicationService.createTitle(title);
    }

}
