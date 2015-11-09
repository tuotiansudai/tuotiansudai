package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.LoanTitleDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan() {
        //TODO 合同需要从数据库中获取
        List contracts = new ArrayList();
        Map<String, String> contract = new HashMap<>();
        contract.put("id", "789098123");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/create-loan");
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.values()));
        modelAndView.addObject("loanTypes", Lists.newArrayList(LoanType.values()));
        modelAndView.addObject("contracts", contracts);
        return modelAndView;
    }

    @RequestMapping(value = "/loaner/{loaner}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@PathVariable String loaner) {
        return loanService.getLoginNames(loaner);
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

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> createLoan(@RequestBody LoanDto loanDto) {
        return loanService.createLoan(loanDto);
    }

    @RequestMapping(value = "/{loanId:^[0-9]{15}$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView loanInfo(@PathVariable long loanId) {
        if (!loanService.loanIsExist(loanId)) {
            return new ModelAndView("/index");
        }
        //TODO 合同需要从数据库中获取
        List contracts = new ArrayList();
        Map<String, String> contract = new HashMap<>();
        contract.put("id", "789098123");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/edit-loan");
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.values()));
        modelAndView.addObject("loanTypes", Lists.newArrayList(LoanType.values()));
        modelAndView.addObject("contracts", contracts);
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
        return loanService.openLoan(loanDto);
    }

    @RequestMapping(value = "/recheck", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> recheckLoan(@RequestBody LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = null;
        try {
            baseDto = loanService.loanOut(loanDto);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return baseDto;
    }
}
