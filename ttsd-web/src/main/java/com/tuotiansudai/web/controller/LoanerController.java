package com.tuotiansudai.web.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.LoanService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/loaner")
public class LoanerController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loanList(@RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                 @RequestParam(name = "pageSize", defaultValue = "10",required = false) int pageSize,
                                 @RequestParam(name = "startTime", required = false) Date startTime,
                                 @RequestParam(name = "endTime", required = false) Date endTime,
                                 @RequestParam(name = "status", required = false) LoanStatus status) {
        return new ModelAndView("/loaner-loan-list");
    }

    @RequestMapping(path = "/loan-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> loanData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                   @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                   @RequestParam(name = "status", required = false) LoanStatus status,
                                                   @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                   @RequestParam(name = "endTime", required = false) Date endTime) {

        return loanService.getLoanerLoanData(index, pageSize, status, startTime, endTime);
    }

    @RequestMapping(path = "/fake-loan-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> fakeLoanData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                       @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                       @RequestParam(name = "status", required = false) LoanStatus status,
                                                       @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                       @RequestParam(name = "endTime", required = false) Date endTime) {
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();

        List<LoanerLoanPaginationItemDataDto> records = Lists.newArrayList();
        for (int count = 0; count < 100; count++) {
            LoanModel loanModel = new LoanModel();
            loanModel.setId(count);
            loanModel.setName("name" + count);
            loanModel.setLoanAmount(1000 + count);
            loanModel.setExpectedRepayAmount(100 + count);
            loanModel.setActualRepayAmount(100 + count);
            loanModel.setUnpaidAmount(100 + count);
            loanModel.setRecheckTime(new DateTime().plusDays(count).toDate());
            loanModel.setNextRepayDate(new DateTime().plusDays(count).toDate());
            loanModel.setCompletedDate(new DateTime().plusDays(count).toDate());
            LoanerLoanPaginationItemDataDto itemDataDto = new LoanerLoanPaginationItemDataDto(loanModel);
            records.add(itemDataDto);
        }
        BasePaginationDataDto<LoanerLoanPaginationItemDataDto> dataDto = new BasePaginationDataDto<>(index, pageSize, 100, records);
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(path = "/loan-repay/{loanId}", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<LoanRepayDataDto> loanRepayData(@PathVariable long loanId) {
        BaseDto<LoanRepayDataDto> baseDto = new BaseDto<>();
        LoanRepayDataDto dataDto = new LoanRepayDataDto();
        baseDto.setData(dataDto);

        dataDto.setStatus(true);

        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(1);
        loanRepayModel.setPeriod(1);
        loanRepayModel.setCorpus(10000);
        loanRepayModel.setExpectedInterest(100);
        loanRepayModel.setActualInterest(100);
        loanRepayModel.setDefaultInterest(100);
        loanRepayModel.setRepayDate(new Date());
        loanRepayModel.setActualRepayDate(new Date());
        loanRepayModel.setStatus(RepayStatus.REPAYING);

        LoanRepayModel loanRepayModel1 = new LoanRepayModel();
        loanRepayModel1.setId(1);
        loanRepayModel1.setPeriod(1);
        loanRepayModel1.setCorpus(10000);
        loanRepayModel1.setExpectedInterest(100);
        loanRepayModel1.setDefaultInterest(100);
        loanRepayModel1.setRepayDate(new Date());
        loanRepayModel1.setStatus(RepayStatus.REPAYING);
        dataDto.setRecords(Lists.newArrayList(new LoanRepayDataItemDto(loanRepayModel, true), new LoanRepayDataItemDto(loanRepayModel1, false)));

        return baseDto;
    }
}
