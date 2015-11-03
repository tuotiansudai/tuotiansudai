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
import com.tuotiansudai.utils.AmountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            baseDto =  loanService.loanOut(loanDto);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return baseDto;
    }

    @RequestMapping(value = "/recheck/{loanId:^[0-9]{15}$}", method = RequestMethod.GET)
    public ModelAndView recheck(@PathVariable long loanId) {
        BaseDto<LoanDto> dto = loanService.getLoanDetail(loanId);
        return new ModelAndView("/recheck", "loan", dto.getData());
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
//            loanService.loanOut(loanId, minInvestAmountCent, dateFundraisingEndTime);
            mv = recheck(loanId);
        } catch (Exception e) {
            mv = recheck(loanId);
            WebUtils.addError(mv,e);
        }
        return mv;
    }
}
