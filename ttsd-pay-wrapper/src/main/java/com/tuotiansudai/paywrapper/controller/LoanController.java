package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.TitleModel;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoanController extends BaseController{
    @Autowired
    private LoanService loanService;

    @Autowired
    private com.tuotiansudai.paywrapper.service.LoanService payLoanService;

    @RequestMapping(value = "/post/createLoan",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto createLoan(@Valid @RequestBody LoanDto loanDto){
        return payLoanService.createLoan(loanDto);
    }

    @RequestMapping(value = "/get/loginNames",method = RequestMethod.GET)
    @ResponseBody
    public List<String> getLoginNames(@Valid @RequestParam String loginName){
        return payLoanService.getLoginNames(loginName);
    }

    @RequestMapping(value = "get/allLoanTypes",method = RequestMethod.GET)
    @ResponseBody
    public List<LoanType> getAllLoanTypes(){
        return loanService.getLoanType();
    }

    @RequestMapping(value = "/get/allActivityTypes",method = RequestMethod.GET)
    @ResponseBody
    public List<ActivityType> getAllActivityTypes(){
        return loanService.getActivityType();
    }

    @RequestMapping(value = "get/AllContracts",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,String>> getAllContracts(){
        List contracts = new ArrayList();
        Map<String,String> contract = new HashMap<>();
        contract.put("id","squareContract");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        return contracts;
    }

    @RequestMapping(value = "get/allTitles",method = RequestMethod.GET)
    @ResponseBody
    public List<TitleModel> findAllTitles(){
        return payLoanService.findAllTitles();
    }
}
