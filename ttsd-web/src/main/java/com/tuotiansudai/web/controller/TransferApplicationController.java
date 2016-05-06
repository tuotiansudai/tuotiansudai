package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/transfer")
public class TransferApplicationController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountMapper accountMapper;

    @RequestMapping(value = "/{transferApplicationId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getTransferApplicationDetail(@PathVariable long transferApplicationId) {
        TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName(), 6);
        if (dto == null) {
            return new ModelAndView("/error/404");
        }
        ModelAndView modelAndView = new ModelAndView("/transfer-detail", "responsive", true);
        modelAndView.addObject("transferApplication", dto);
        modelAndView.addObject("transferApplicationReceiver", transferService.getTransferee(transferApplicationId, LoginUserInfo.getLoginName()));
        return modelAndView;
    }

    @RequestMapping(value = "/{transferApplicationId:^\\d+$}/purchase-check", method = RequestMethod.GET)
    @ResponseBody
    public BaseDataDto IsPurchase(@PathVariable long transferApplicationId) {
        BaseDataDto baseDataDto = new BaseDataDto();
        TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName(),6);
        if (Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.CANCEL).contains(dto.getTransferStatus())) {
            baseDataDto.setStatus(false);
            baseDataDto.setMessage(dto.getTransferStatus().name());
        }
        return baseDataDto;
    }

    @RequestMapping(path = "/purchase", method = RequestMethod.POST)
    public ModelAndView purchase(@Valid @ModelAttribute InvestDto investDto, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("/error/404", "responsive", true);
        String errorMessage = "投资失败，请联系客服！";
        AccountModel accountModel = accountMapper.findByLoginName(LoginUserInfo.getLoginName());

        if (accountModel.isNoPasswordInvest()) {
            try {
                investDto.setLoginName(LoginUserInfo.getLoginName());
                BaseDto<PayDataDto> baseDto = transferService.noPasswordTransferPurchase(investDto);
                if (baseDto.getData().getStatus()) {
                    httpServletRequest.getSession().setAttribute("noPasswordInvestSuccess", true);
                    return new ModelAndView("redirect:/invest-success");
                }
                if (baseDto.getData() != null) {
                    errorMessage = baseDto.getData().getMessage();
                }
            } catch (InvestException e) {
                errorMessage = e.getMessage();
            }

            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
            modelAndView.setViewName(MessageFormat.format("redirect:/loan/{0}", investDto.getLoanId()));
        } else {
            try {
                investDto.setLoginName(LoginUserInfo.getLoginName());
                BaseDto<PayFormDataDto> baseDto = transferService.transferPurchase(investDto);
                if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                    return new ModelAndView("/pay", "pay", baseDto);
                }
                if (baseDto.getData() != null) {
                    errorMessage = baseDto.getData().getMessage();
                }
            } catch (InvestException e) {
                errorMessage = e.getMessage();
            }

            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
            modelAndView.setViewName(MessageFormat.format("redirect:/loan/{0}", investDto.getLoanId()));

        }
        return modelAndView;
    }

}
