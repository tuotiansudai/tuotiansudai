package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.TitleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoanController{

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "loan",method = RequestMethod.POST)
    @ResponseBody
    public BaseDto createLoan(@Valid @RequestBody LoanDto loanDto){
        return loanService.createLoan(loanDto);
    }

}
