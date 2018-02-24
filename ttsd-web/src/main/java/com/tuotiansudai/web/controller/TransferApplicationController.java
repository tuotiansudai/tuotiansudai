package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AccountMapper accountMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private LoanService loanService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;


    @RequestMapping(value = "/{transferApplicationId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getTransferApplicationDetail(@PathVariable long transferApplicationId) {
        TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName(), 6);
        if (dto == null) {
            return new ModelAndView("/error/404");
        }
        LoanModel loanModel = loanService.findLoanById(dto.getLoanId());
        LoanDto loanDto = new LoanDto();
        loanDto.setBasicRate(String.valueOf(loanModel.getBaseRate()));
        loanDto.setActivityRate(String.valueOf(loanModel.getActivityRate()));
        loanDto.setLoanAmount(AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        loanDto.setType(loanModel.getType());
        loanDto.setPeriods(loanModel.getPeriods());

        String loginName = LoginUserInfo.getLoginName();
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);

        ModelAndView modelAndView = new ModelAndView("/transfer-detail");
        modelAndView.addObject("transferApplication", dto);
        modelAndView.addObject("loanDto", loanDto);
        modelAndView.addObject("anxinAuthenticationRequired", anxinWrapperClient.isAuthenticationRequired(loginName).getData().getStatus());
        modelAndView.addObject("anxinUser", anxinProp != null && anxinProp.isAnxinUser());
        modelAndView.addObject("transferApplicationReceiver", transferService.getTransferee(transferApplicationId, LoginUserInfo.getLoginName()));
        modelAndView.addObject("investRepay", transferService.getUserTransferInvestRepay(dto.getTransferInvestId()));
        BankCardModel passedBankCard = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        modelAndView.addObject("hasBankCard", passedBankCard != null);
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
        AccountModel accountModel = accountMapper.findByLoginName(LoginUserInfo.getLoginName());
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(investDto.getTransferApplicationId()));
        investDto.setAmount(String.valueOf(transferApplicationModel.getTransferAmount()));
        if (accountModel.isNoPasswordInvest()) {
            try {
                investDto.setLoginName(LoginUserInfo.getLoginName());
                BaseDto<PayDataDto> baseDto = transferService.noPasswordTransferPurchase(investDto);
                if (baseDto.getData().getStatus()) {
                    if (Source.M.equals(investDto.getSource())) {
                        return new ModelAndView(MessageFormat.format("redirect:/m/callback/invest_transfer_project_transfer_nopwd?order_id={0}", baseDto.getData().getExtraValues().get("order_id")));
                    } else {
                        return new ModelAndView(MessageFormat.format("redirect:/callback/invest_transfer_project_transfer_nopwd?order_id={0}", baseDto.getData().getExtraValues().get("order_id")));
                    }
                }
                redirectAttributes.addFlashAttribute("errorMessage", baseDto.getData().getMessage());
            } catch (InvestException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "投资失败，请联系客服！");
            }

            redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
            if (Source.M.equals(investDto.getSource())) {
                modelAndView.setViewName(MessageFormat.format("redirect:/m/transfer/{0}#transferDetail", investDto.getTransferApplicationId()));
            } else {
                modelAndView.setViewName(MessageFormat.format("redirect:/transfer/{0}", investDto.getTransferApplicationId()));
            }
        } else {
            try {
                investDto.setLoginName(LoginUserInfo.getLoginName());
                BaseDto<PayFormDataDto> baseDto = transferService.transferPurchase(investDto);
                if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                    return new ModelAndView("/pay", "pay", baseDto);
                }

                redirectAttributes.addFlashAttribute("errorMessage", baseDto.getData().getMessage());
            } catch (InvestException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "投资失败，请联系客服！");
            }

            redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
            if (Source.M.equals(investDto.getSource())) {
                modelAndView.setViewName(MessageFormat.format("redirect:/m/transfer/{0}#transferDetail", investDto.getTransferApplicationId()));
            } else {
                modelAndView.setViewName(MessageFormat.format("redirect:/transfer/{0}", investDto.getTransferApplicationId()));
            }

        }
        return modelAndView;
    }
}
