package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.service.ExtraLoanRateService;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/project-manage/loan")
public class LoanController {

    static Logger logger = Logger.getLogger(LoanController.class);

    private static final String DEFAULT_CONTRACT_ID = "789098123"; // 四方合同

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private ExtraLoanRateService extraLoanRateService;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan() {
        ModelAndView modelAndView = new ModelAndView("/loan-create");
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.values()));
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
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

    @RequestMapping(value = "/create/house", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> createLoan(@RequestBody CreateHouseLoanDto createLoanDto) {
        createLoanDto.getLoanDto().setCreatedLoginName(LoginUserInfo.getLoginName());
        createLoanDto.getLoanDto().setPledgeType(PledgeType.HOUSE);
        return loanService.createLoan(createLoanDto.getLoanDto(), createLoanDto.getLoanDetailsDto(), createLoanDto.getLoanerDetailsDto(),
                createLoanDto.getPledgeDetailsDto());
    }

    @RequestMapping(value = "/create/vehicle", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> createLoan(@RequestBody CreateVehicleLoanDto createLoanDto) {
        createLoanDto.getLoanDto().setCreatedLoginName(LoginUserInfo.getLoginName());
        createLoanDto.getLoanDto().setPledgeType(PledgeType.VEHICLE);
        return loanService.createLoan(createLoanDto.getLoanDto(), createLoanDto.getLoanDetailsDto(), createLoanDto.getLoanerDetailsDto(),
                createLoanDto.getPledgeDetailsDto());
    }

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView loanInfo(@PathVariable long loanId) {
        if (!loanService.loanIsExist(loanId)) {
            return new ModelAndView("/index");
        }
        ModelAndView modelAndView = new ModelAndView("/loan-edit");
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.values()));
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("loanTypes", Lists.newArrayList(LoanType.values()));
        modelAndView.addObject("contractId", DEFAULT_CONTRACT_ID);
        modelAndView.addObject("loanInfo", loanService.findLoanById(loanId));
        modelAndView.addObject("extraLoanRates", extraLoanRateMapper.findByLoanId(loanId));
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
    public BaseDto<PayDataDto> openLoan(@RequestBody LoanDto loanDto, HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        loanDto.setVerifyLoginName(LoginUserInfo.getLoginName());
        return loanService.openLoan(loanDto, ip);
    }

    @RequestMapping(value = "/delay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> delayLoan(@RequestBody LoanDto loanDto) {
        return loanService.delayLoan(loanDto);
    }

    @RequestMapping(value = "/recheck", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> recheckLoan(@RequestBody LoanDto loanDto) {
        loanDto.setRecheckLoginName(LoginUserInfo.getLoginName());
        return loanService.loanOut(loanDto);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> cancelLoan(@RequestBody LoanDto loanDto) {
        return loanService.cancelLoan(loanDto);
    }

    @RequestMapping(value = "/apply-audit", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> applyAuditLoan(@RequestBody LoanDto loanDto) {
        return loanService.applyAuditLoan(loanDto);
    }

    @RequestMapping(value = "/extra-rate-rule", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<ExtraLoanRateRuleDto> extraRateRule(@RequestParam(value = "loanName") String loanName, @RequestParam(value = "productType") ProductType productType){
        return extraLoanRateService.findExtraLoanRateRuleByNameAndProductType(loanName, productType);
    }

}
