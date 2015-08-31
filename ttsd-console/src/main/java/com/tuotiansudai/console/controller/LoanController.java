package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleLoanService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    private ConsoleLoanService consoleLoanService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan(HttpServletRequest request){
        //TODO 合同需要从数据库中获取
        List contracts = new ArrayList();
        Map<String,String> contract = new HashMap<>();
        contract.put("id", "789098123");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/create-loan");
        modelAndView.addObject("activityTypes", loanService.getActivityType());
        modelAndView.addObject("loanTypes", loanService.getLoanType());
        modelAndView.addObject("contracts",contracts);
        return modelAndView;
    }
    @RequestMapping(value = "/loaner/{loaner}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@PathVariable String loaner) {
        return loanService.getLoginNames(loaner);
    }

    @RequestMapping(value = "/titles", method = RequestMethod.GET)
    @ResponseBody
    public List<LoanTitleModel> findAllTitles(){
        return loanService.findAllTitles();
    }

    @RequestMapping(value = "/title",method = RequestMethod.POST)
    @ResponseBody
    public LoanTitleModel addTitle(@RequestBody LoanTitleDto loanTitleDto){
        return loanService.createTitle(loanTitleDto);
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> createLoan(@RequestBody LoanDto loanDto){
        return loanService.createLoan(loanDto);
    }

    @RequestMapping(value = "/{loanId:^[0-9]{15}$}",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView loanInfo(@PathVariable long loanId){
        //TODO 合同需要从数据库中获取
        List contracts = new ArrayList();
        Map<String,String> contract = new HashMap<>();
        contract.put("id", "789098123");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/create-loan");
        modelAndView.addObject("activityTypes", loanService.getActivityType());
        modelAndView.addObject("loanTypes", loanService.getLoanType());
        modelAndView.addObject("contracts",contracts);
        modelAndView.addObject("loanInfo",loanService.findLoanById(loanId));
        return modelAndView;
    }

    @RequestMapping(value = "/edit-loan",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> editLoan(@RequestBody LoanDto loanDto){
        return consoleLoanService.editLoan(loanDto);
    }

    @RequestMapping(value = "/first-trial-passed",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> firstTrialPassed(@RequestBody LoanDto loanDto){
        return consoleLoanService.firstTrialPassed(loanDto);
    }

    @RequestMapping(value = "/first-trial-refused",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> firstTrialRefused(@RequestBody LoanDto loanDto){
        return consoleLoanService.firstTrialRefused(loanDto);
    }
}
