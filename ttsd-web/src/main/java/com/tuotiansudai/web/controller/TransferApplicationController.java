package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping(value = "/transfer")
public class TransferApplicationController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserBindBankCardService userBindBankCardService;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private RiskEstimateService riskEstimateService;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @RequestMapping(value = "/{transferApplicationId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getTransferApplicationDetail(@PathVariable long transferApplicationId) {
        TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName(), 6);
        if (dto == null) {
            return new ModelAndView("/error/404");
        }
        LoanModel loanModel = loanService.findLoanById(dto.getLoanId());
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());

        LoanDto loanDto = new LoanDto();
        loanDto.setBasicRate(String.valueOf(loanModel.getBaseRate()));
        loanDto.setActivityRate(String.valueOf(loanModel.getActivityRate()));
        loanDto.setLoanAmount(AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        loanDto.setType(loanModel.getType());
        loanDto.setPeriods(loanModel.getPeriods());
        loanDto.setEstimate(loanDetailsModel != null && loanDetailsModel.getEstimate() != null ? loanDetailsModel.getEstimate().getType() : null);

        String loginName = LoginUserInfo.getLoginName();
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);

        ModelAndView modelAndView = new ModelAndView("/transfer-detail");
        modelAndView.addObject("transferApplication", dto);
        modelAndView.addObject("loanDto", loanDto);
        modelAndView.addObject("anxinAuthenticationRequired", anxinWrapperClient.isAuthenticationRequired(loginName).getData().getStatus());
        modelAndView.addObject("anxinUser", anxinProp != null && anxinProp.isAnxinUser());
        modelAndView.addObject("transferApplicationReceiver", transferService.getTransferee(transferApplicationId, LoginUserInfo.getLoginName()));
        modelAndView.addObject("investRepay", transferService.getUserTransferInvestRepay(dto.getTransferInvestId()));
        UserBankCardModel userBankCardModel = userBindBankCardService.findBankCard(LoginUserInfo.getLoginName());
        modelAndView.addObject("hasBankCard", userBankCardModel != null);

        modelAndView.addObject("estimate", riskEstimateService.getEstimate(LoginUserInfo.getLoginName()) != null);
        modelAndView.addObject("investFeeRate", membershipPrivilegePurchaseService.obtainServiceFee(LoginUserInfo.getLoginName()));
        return modelAndView;
    }

    @RequestMapping(value = "/{transferApplicationId:^\\d+$}/purchase-check", method = RequestMethod.GET)
    @ResponseBody
    public BaseDataDto IsPurchase(@PathVariable long transferApplicationId) {
        BaseDataDto baseDataDto = new BaseDataDto();
        List<TransferApplicationModel> transferApplicationModels = transferService.getTransferApplicaationByTransferInvestId(transferApplicationId);
        if (transferApplicationModels.size() > 1) {
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("MULTITERM");
        }
        if (transferApplicationModels.size() == 1) {
            if (Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.CANCEL).contains(transferApplicationModels.get(0).getStatus())) {
                baseDataDto.setStatus(false);
                baseDataDto.setMessage(transferApplicationModels.get(0).getStatus().name());
            }
        }
        return baseDataDto;
    }

    @RequestMapping(path = "/purchase", method = RequestMethod.POST)
    public ModelAndView purchase(@Valid @ModelAttribute InvestDto investDto, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("/error/404", "responsive", true);

        try {
            investDto.setLoginName(LoginUserInfo.getLoginName());
            BankAsyncMessage baseDto = transferService.transferPurchase(investDto);
            return new ModelAndView("/pay", "pay", baseDto);

        } catch (InvestException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getType().getDescription());
        }

        redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
        if (Source.M.equals(investDto.getSource())) {
            modelAndView.setViewName(MessageFormat.format("redirect:/m/transfer/{0}#transferDetail", investDto.getTransferApplicationId()));
        } else {
            modelAndView.setViewName(MessageFormat.format("redirect:/transfer/{0}", investDto.getTransferApplicationId()));
        }
        return modelAndView;
    }
}
