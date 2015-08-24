package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.TitleDto;
import com.tuotiansudai.repository.model.TitleModel;
import com.tuotiansudai.service.CreateLoanBidService;
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
    private CreateLoanBidService createLoanBidService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan(HttpServletRequest request){
        List contracts = new ArrayList();
        Map<String,String> contract = new HashMap<>();
        contract.put("id", "squareContract");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/create-loan");
        modelAndView.addObject("activityTypes",createLoanBidService.getActivityType());
        modelAndView.addObject("loanTypes", createLoanBidService.getLoanType());
        modelAndView.addObject("contracts",contracts);
        return modelAndView;
    }
    @RequestMapping(value = "/loaner", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@RequestParam(value = "loginName")String loginName) {
        return createLoanBidService.getLoginNames(loginName);
    }

    @RequestMapping(value = "/titles", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,List<TitleModel>> findAllTitles(){
        return createLoanBidService.findAllTitles();
    }

    @RequestMapping(value = "/title",method = RequestMethod.POST)
    @ResponseBody
    public TitleModel addTitle(@RequestBody TitleDto titleDto){
        return createLoanBidService.createTitle(titleDto);
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> createLoan(@RequestBody LoanDto loanDto){
        return createLoanBidService.createLoanBid(loanDto);
    }

}
