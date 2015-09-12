package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.TTSDException;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

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
        if (!loanService.loanIsExist(loanId)){
            return new ModelAndView("/");
        }
        //TODO 合同需要从数据库中获取
        List contracts = new ArrayList();
        Map<String,String> contract = new HashMap<>();
        contract.put("id", "789098123");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/edit-loan");
        modelAndView.addObject("activityTypes", loanService.getActivityType());
        modelAndView.addObject("loanTypes", loanService.getLoanType());
        modelAndView.addObject("contracts",contracts);
        modelAndView.addObject("loanInfo",loanService.findLoanById(loanId));
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public BaseDto<PayDataDto> updateLoan(@RequestBody LoanDto loanDto){
        return loanService.updateLoan(loanDto);
    }

    @RequestMapping(value = "/loanout", method = RequestMethod.GET)
    public ModelAndView loanOut() {
        long loanId = 0L;
        BaseDto<LoanDto> dto = loanService.getLoanDetail(loanId);
        return new ModelAndView("/loanout", "baseDto", dto);
    }

    @RequestMapping(value = "/loanout", method = RequestMethod.POST)
    public ModelAndView doLoanOut() {
        long loanId = 20150929444285L;
        try {
            loanService.loanOut(loanId,1,new Date());
            return new ModelAndView("/loanout");
        } catch (TTSDException e) {
            e.printStackTrace();
        }
        return loanOut();
    }
}
