package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleLoanCreateService;
import com.tuotiansudai.console.service.ExtraLoanRateService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.tuotiansudai.repository.model.LoanType.*;

@Controller
@RequestMapping(value = "/project-manage/loan")
public class LoanController {

    static Logger logger = Logger.getLogger(LoanController.class);

    private static final String DEFAULT_CONTRACT_ID = "789098123"; // 四方合同


    @Autowired
    private LoanService loanService;

    @Autowired
    private ConsoleLoanCreateService consoleLoanCreateService;

    @Autowired
    private ExtraLoanRateService extraLoanRateService;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView createLoan() {
        ModelAndView modelAndView = new ModelAndView("/loan-create");
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360));
        modelAndView.addObject("loanTypes", Lists.newArrayList(INVEST_INTEREST_MONTHLY_REPAY, INVEST_INTEREST_LUMP_SUM_REPAY));
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.NORMAL, ActivityType.NEWBIE));
        modelAndView.addObject("extraSources", Lists.newArrayList(Source.WEB, Source.MOBILE));
        modelAndView.addObject("contractId", DEFAULT_CONTRACT_ID);
        return modelAndView;
    }

    @RequestMapping(value = "/titles", method = RequestMethod.GET)
    @ResponseBody
    public List<LoanTitleModel> findAllTitles() {
        return consoleLoanCreateService.findAllTitles();
    }

    @RequestMapping(value = "/title", method = RequestMethod.POST)
    @ResponseBody
    public LoanTitleModel addTitle(@RequestBody LoanTitleDto loanTitleDto) {
        return consoleLoanCreateService.createTitle(loanTitleDto);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> createLoan(@RequestBody LoanCreateRequestDto loanCreateRequestDto) {

        loanCreateRequestDto.getLoan().setCreatedBy(LoginUserInfo.getLoginName());
        return consoleLoanCreateService.createLoan(loanCreateRequestDto);
    }

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView loanDetails(@PathVariable long loanId) {
        if (loanService.findLoanById(loanId) == null) {
            return new ModelAndView("/index");
        }
        ModelAndView modelAndView = new ModelAndView("/loan-edit");
        modelAndView.addObject("productTypes", Lists.newArrayList(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360)));
        modelAndView.addObject("loanTypes", LoanType.values());
        modelAndView.addObject("activityTypes", Lists.newArrayList(ActivityType.NORMAL, ActivityType.NEWBIE));
        modelAndView.addObject("extraSources", Lists.newArrayList(Source.WEB, Source.MOBILE));
        modelAndView.addObject("loan", consoleLoanCreateService.getEditLoanDetails(loanId));
        modelAndView.addObject("extraLoanRates", extraLoanRateMapper.findByLoanId(loanId));
        return modelAndView;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> updateLoan(@RequestBody LoanCreateRequestDto loanCreateRequestDto) {
        loanCreateRequestDto.getLoan().setCreatedBy(LoginUserInfo.getLoginName());
        return consoleLoanCreateService.updateLoan(loanCreateRequestDto);
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> openLoan(HttpServletRequest request,
                                        @RequestBody LoanCreateRequestDto loanCreateRequestDto) {
        loanCreateRequestDto.getLoan().setVerifyLoginName(LoginUserInfo.getLoginName());
        return consoleLoanCreateService.openLoan(loanCreateRequestDto, RequestIPParser.parse(request));
    }

    @RequestMapping(value = "/delay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> delayLoan(@RequestBody LoanCreateRequestDto loanCreateRequestDto) {
        return consoleLoanCreateService.delayLoan(loanCreateRequestDto);
    }

    @RequestMapping(value = "/recheck", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> recheckLoan(@RequestBody LoanCreateRequestDto loanCreateRequestDto) {
        loanCreateRequestDto.getLoan().setRecheckLoginName(LoginUserInfo.getLoginName());
        return consoleLoanCreateService.loanOut(loanCreateRequestDto);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> cancelLoan(@RequestBody LoanCreateRequestDto loanCreateRequestDto) {
        return consoleLoanCreateService.cancelLoan(loanCreateRequestDto);
    }

    @RequestMapping(value = "/apply-audit", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> applyAuditLoan(@RequestBody LoanCreateRequestDto loanCreateRequestDto) {
        return consoleLoanCreateService.applyAuditLoan(loanCreateRequestDto.getLoan().getId());
    }

    @RequestMapping(value = "/extra-rate-rule", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<ExtraLoanRateRuleDto> extraRateRule(@RequestParam(value = "loanName") String loanName, @RequestParam(value = "productType") ProductType productType) {
        return extraLoanRateService.findExtraLoanRateRuleByNameAndProductType(loanName, productType);
    }
    @RequestMapping(value = "/check-loaner", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> checkLoaner(@RequestParam(value = "loanerLoginName") String loanerLoginName) {
        return consoleLoanCreateService.checkLoaner(loanerLoginName);
    }
}
