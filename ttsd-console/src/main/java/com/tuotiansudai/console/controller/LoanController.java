package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.LoanTitleDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.TTSDException;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.AmountUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan(HttpServletRequest request) {
        //TODO 合同需要从数据库中获取
        List contracts = new ArrayList();
        Map<String, String> contract = new HashMap<>();
        contract.put("id", "789098123");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/create-loan");
        modelAndView.addObject("activityTypes", loanService.getActivityType());
        modelAndView.addObject("loanTypes", loanService.getLoanType());
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
            return new ModelAndView("/");
        }
        //TODO 合同需要从数据库中获取
        List contracts = new ArrayList();
        Map<String, String> contract = new HashMap<>();
        contract.put("id", "789098123");
        contract.put("contractName", "四方合同");
        contracts.add(contract);
        ModelAndView modelAndView = new ModelAndView("/edit-loan");
        modelAndView.addObject("activityTypes", loanService.getActivityType());
        modelAndView.addObject("loanTypes", loanService.getLoanType());
        modelAndView.addObject("contracts", contracts);
        modelAndView.addObject("loanInfo", loanService.findLoanById(loanId));
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public BaseDto<PayDataDto> updateLoan(@RequestBody LoanDto loanDto) {
        return loanService.updateLoan(loanDto);
    }

    @RequestMapping(value = "/recheck/{loanId:^[0-9]{15}$}", method = RequestMethod.GET)
    public ModelAndView recheck(@PathVariable long loanId) {
        BaseDto<LoanDto> dto;// = loanService.getLoanDetail(loanId);
        dto = fakeLoanDto(loanId);
        return new ModelAndView("/recheck", "loan", dto.getData());
    }

    private BaseDto<LoanDto> fakeLoanDto(long loanId){
        BaseDto<LoanDto> dto = new BaseDto<>();
        LoanDto loanDto = new LoanDto();
        dto.setData(loanDto);
        loanDto.setId(loanId);
        loanDto.setProjectName("这是一个fake标的");
        loanDto.setLoanerLoginName("loannerLoginName");
        loanDto.setAgentLoginName("agentLoginName");
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setLoanAmount(RandomStringUtils.randomNumeric(7));
        loanDto.setAmountNeedRaised(new Random().nextDouble());
        loanDto.setBasicRate("13");
        loanDto.setActivityRate("1");
        loanDto.setPeriods(3);
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setMinInvestAmount("100");
        return dto;
    }

    @RequestMapping(value = "/recheck/{loanId:^[0-9]{15}$}", method = RequestMethod.POST)
    public ModelAndView doRecheck(@PathVariable long loanId,
                                  @RequestParam(value = "minInvestAmount", required = false) String minInvestAmount,
                                  @RequestParam(value = "fundraisingEndTime", required = false) String fundraisingEndTime) {
        ModelAndView mv;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateFundraisingEndTime = null;
        try {
            dateFundraisingEndTime = sdf.parse(fundraisingEndTime);
        } catch (ParseException e) {
            dateFundraisingEndTime = null;
            e.printStackTrace();
        }
        try {
            long minInvestAmountCent = AmountUtil.convertStringToCent(minInvestAmount);
            loanService.loanOut(loanId, minInvestAmountCent, dateFundraisingEndTime);
            mv = recheck(loanId);
        } catch (TTSDException e) {
            mv = recheck(loanId);
            WebUtils.addError(mv,e);
        }
        return mv;
    }
}
