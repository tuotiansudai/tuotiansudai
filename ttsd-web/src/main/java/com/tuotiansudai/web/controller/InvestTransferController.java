package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.MessageFormat;
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

    private static String MESSAGE_TEMPLATE = "您持有债权{0}天，需支付本金{1}%的手续费。";

    @RequestMapping(value = "/application/{transferInvestId}/isAllowTransfer", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto isAllowTransfer(@PathVariable long transferInvestId) {
        return investTransferService.isAllowTransfer(transferInvestId);
    }

    @RequestMapping(value = "/application/{investId}/apply", method = RequestMethod.GET)
    public ModelAndView investApply(@PathVariable long investId) {
        ModelAndView modelAndView = new ModelAndView("/create-transfer-detail");
        InvestModel investModel = investService.findById(investId);
        Date deadline = investTransferService.getDeadlineFromNow();
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        long transferFee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        long transferAmountLimit = new BigDecimal(1).subtract(new BigDecimal(transferRuleModel.getDiscount())).multiply(new BigDecimal(investModel.getAmount())).setScale(0, BigDecimal.ROUND_UP).longValue();
        modelAndView.addObject("investAmount", investModel.getAmount());
        modelAndView.addObject("transferAmountLimit", transferAmountLimit);
        modelAndView.addObject("transferFee", transferFee);
        modelAndView.addObject("deadline", deadline);
        modelAndView.addObject("transferInvestId", investId);
        DateTime beginDate;
        DateTime endDate = new DateTime();
        if (Lists.newArrayList(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, LoanType.INVEST_INTEREST_MONTHLY_REPAY).contains(loanModel.getType())){
            beginDate = new DateTime(investModel.getCreatedTime());
        } else {
            beginDate = new DateTime(loanModel.getRecheckTime());
        }
        int days = Days.daysBetween(beginDate, endDate).getDays();
        String messageDay;
        double feeRate;
        if (days <= transferRuleModel.getLevelOneUpper()) {
            feeRate = transferRuleModel.getLevelOneFee();
            messageDay = "不足" + transferRuleModel.getLevelTwoLower() ;
        } else if (days <= transferRuleModel.getLevelTwoUpper()) {
            feeRate = transferRuleModel.getLevelTwoFee();
            messageDay = "在" + transferRuleModel.getLevelTwoLower()  + "至" + transferRuleModel.getLevelTwoUpper() ;
        } else {
            feeRate = transferRuleModel.getLevelThreeFee();
            messageDay = "大于" + transferRuleModel.getLevelTwoUpper() ;
        }
        String message = MessageFormat.format(MESSAGE_TEMPLATE, messageDay, feeRate * 100 );
        modelAndView.addObject("message", message);
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
