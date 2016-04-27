package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Date;

@Controller
@RequestMapping(value = "/transfer")
public class InvestTransferController {

    static Logger logger = Logger.getLogger(InvestTransferController.class);

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private InvestService investService;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Autowired
    private LoanMapper loanMapper;

    @RequestMapping(value = "/application/{transferInvestId}/isAllowTransfer", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto isAllowTransfer(@PathVariable long transferInvestId) {
        return investTransferService.isAllowTransfer(transferInvestId);
    }

    @RequestMapping(value = "/application/{transferInvestId}/apply", method = RequestMethod.POST)
    public ModelAndView investApply(@PathVariable long transferInvestId) {
        ModelAndView modelAndView = new ModelAndView("/create-transfer-detail");
        InvestModel investModel = investService.findById(transferInvestId);
        Date deadline = investTransferService.getDeadlineFromNow();
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        long transferFee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        long transferAmountLimit = new BigDecimal(1).subtract(new BigDecimal(transferRuleModel.getDiscount())).multiply(new BigDecimal(investModel.getAmount())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        modelAndView.addObject("investAmount", investModel.getAmount());
        modelAndView.addObject("transferAmountLimit", transferAmountLimit);
        modelAndView.addObject("transferFee", transferFee);
        modelAndView.addObject("deadline", deadline);
        modelAndView.addObject("transferInvestId", transferInvestId);
        return modelAndView;
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public boolean investTransferApply(@RequestBody TransferApplicationDto transferApplicationDto) {
        return investTransferService.investTransferApply(transferApplicationDto);
    }

    @RequestMapping(value = "/application/{transferApplyId}/cancel", method = RequestMethod.POST)
    @ResponseBody
    public boolean investTransferApplyCancel(@PathVariable long transferApplyId) {
         return investTransferService.cancelTransferApplication(transferApplyId);
    }

}
