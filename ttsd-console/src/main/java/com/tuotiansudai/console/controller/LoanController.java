package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.LoanTitleDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/project-manage/loan")
public class LoanController {

    private static final String DEFAULT_CONTRACT_ID = "789098123"; // 四方合同

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan() {
        ModelAndView modelAndView = new ModelAndView("/loan-create");
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.values()));
        modelAndView.addObject("productTypes",Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("loanTypes", Lists.newArrayList(LoanType.values()));
        modelAndView.addObject("contractId", DEFAULT_CONTRACT_ID);
        return modelAndView;
    }

    @RequestMapping(value = "/titles", method = RequestMethod.GET)
    @ResponseBody
    public List<LoanTitleModel> findAllTitles() {
        return loanService.findAllTitles();
    }

    @RequestMapping(value = "/title", method = RequestMethod.POST)
    @ResponseBody
    public LoanTitleModel addTitle(@RequestBody LoanTitleDto loanTitleDto) {
        return loanService.createTitle(loanTitleDto);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> createLoan(@RequestBody LoanDto loanDto) {
        loanDto.setCreatedLoginName(LoginUserInfo.getLoginName());
        return loanService.createLoan(loanDto);
    }

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView loanInfo(@PathVariable long loanId) {
        if (!loanService.loanIsExist(loanId)) {
            return new ModelAndView("/index");
        }
        ModelAndView modelAndView = new ModelAndView("/loan-edit");
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.values()));
        modelAndView.addObject("productTypes",Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("loanTypes", Lists.newArrayList(LoanType.values()));
        modelAndView.addObject("contractId", DEFAULT_CONTRACT_ID);
        modelAndView.addObject("loanInfo", loanService.findLoanById(loanId));
        modelAndView.addObject("loanTitleRelationModels", loanTitleRelationMapper.findByLoanId(loanId));
        return modelAndView;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> updateLoan(@RequestBody LoanDto loanDto) {
        return loanService.updateLoan(loanDto);
    }

    @RequestMapping(value = "/ok", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> openLoan(@RequestBody LoanDto loanDto) {
        loanDto.setVerifyLoginName(LoginUserInfo.getLoginName());
        return loanService.openLoan(loanDto);
    }

    @RequestMapping(value = "/delay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> delayLoan(@RequestBody LoanDto loanDto) {
        return loanService.delayLoan(loanDto);
    }

    @RequestMapping(value = "/recheck", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> recheckLoan(@RequestBody LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = null;
        try {
            loanDto.setRecheckLoginName(LoginUserInfo.getLoginName());
            baseDto = loanService.loanOut(loanDto);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return baseDto;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> cancelLoan(@RequestBody LoanDto loanDto) {
        return loanService.cancelLoan(loanDto);
    }

}
