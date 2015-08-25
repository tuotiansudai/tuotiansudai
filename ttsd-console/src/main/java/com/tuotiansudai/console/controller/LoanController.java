package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.TitleModel;
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

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan(HttpServletRequest request){
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
    @RequestMapping(value = "/loaner", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@RequestParam(value = "loginName")String loginName) {
        return loanService.getLoginNames(loginName);
    }

    @RequestMapping(value = "/titles", method = RequestMethod.GET)
    @ResponseBody
    public List<TitleModel> findAllTitles(){
        return loanService.findAllTitles();
    }

    @RequestMapping(value = "/title",method = RequestMethod.POST)
    @ResponseBody
    public TitleModel addTitle(@RequestBody TitleDto titleDto){
        return loanService.createTitle(titleDto);
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> createLoan(@RequestBody LoanDto loanDto){
        return loanService.createLoanBid(loanDto);
    }

}
