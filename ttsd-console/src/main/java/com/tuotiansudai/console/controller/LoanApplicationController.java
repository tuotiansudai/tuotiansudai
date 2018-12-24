package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleLoanApplicationService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.LoanApplicationStatus;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/loan-application")
public class LoanApplicationController {

    private static final String DEFAULT_CONTRACT_ID = "789098123"; // 四方合同

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
        modelAndView.addObject("keyWord", status);
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
    public BaseDto<BaseDataDto> consumeSave(@PathVariable long applyId, @RequestBody LoanApplicationUpdateDto loanApplicationUpdateDto) {
        return consoleLoanApplicationService.consumeSave(applyId, loanApplicationUpdateDto, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/create-loan", method = RequestMethod.GET)
    public ModelAndView createLoan(@PathVariable long applyId) {
        ModelAndView modelAndView = new ModelAndView("/loan-create");
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360));
        List<LoanType> loanTypes = Lists.newArrayList(LoanType.values());
        Collections.reverse(loanTypes);
        modelAndView.addObject("loanTypes", loanTypes);
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.NORMAL, ActivityType.NEWBIE));
        modelAndView.addObject("extraSources", Lists.newArrayList(Source.WEB, Source.MOBILE));
        modelAndView.addObject("contractId", DEFAULT_CONTRACT_ID);
        modelAndView.addObject("pledgeType", PledgeType.NONE);
        modelAndView.addObject("loanerDto", consoleLoanApplicationService.findLoanerDetail(applyId));
        modelAndView.addObject("loanApplicationId", String.valueOf(applyId));
        return modelAndView;
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/reject", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> consumeReject(@PathVariable long applyId) {
        return consoleLoanApplicationService.consumeReject(applyId);
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/submit-audit", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> consumeSubmitAudit(@PathVariable long applyId) {
        return consoleLoanApplicationService.applyAuditLoanApplication(applyId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/consume/{applyId:^\\d+$}/approve", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> consumeApprove(@PathVariable long applyId) {
        return consoleLoanApplicationService.consumeApprove(applyId);
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
