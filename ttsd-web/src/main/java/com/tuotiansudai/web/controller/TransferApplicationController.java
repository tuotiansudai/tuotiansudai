package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/transfer")
public class TransferApplicationController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/{transferApplicationId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getTransferApplicationDetail(@PathVariable long transferApplicationId) {
        TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName());
        if (dto == null) {
            return new ModelAndView("/error/404");
        }
        ModelAndView modelAndView = new ModelAndView("/transfer-detail", "responsive", true);
        modelAndView.addObject("transferApplication", dto);
        modelAndView.addObject("transferApplicationReceiver", transferService.getTransferee(transferApplicationId, LoginUserInfo.getLoginName()));
        return modelAndView;
    }

    @RequestMapping(value = "/IsPurchase/{transferApplicationId:^\\d+$}/{transferStatus}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDataDto IsPurchase(@PathVariable long transferApplicationId, @PathVariable String transferStatus) {
        BaseDataDto baseDataDto = new BaseDataDto();
        if(!transferStatus.equals("SUCCESS")){
            TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName());
            if (dto.getTransferStatus() == TransferStatus.SUCCESS) {
                baseDataDto.setStatus(false);
                baseDataDto.setMessage("SUCCESS");
            }
            if (dto.getTransferStatus() == TransferStatus.CANCEL) {
                baseDataDto.setStatus(false);
                baseDataDto.setMessage("CANCEL");
            }
        }
        return baseDataDto;
    }

    @RequestMapping(path = "/purchase/{transferApplicationId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView purchase(@PathVariable long transferApplicationId, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("/error/404", "responsive", true);
        String errorMessage = "投资失败，请联系客服！";
        TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName());
        AccountModel accountModel = accountMapper.findByLoginName(LoginUserInfo.getLoginName());
        InvestDto investDto = new InvestDto();
        investDto.setLoanId(String.valueOf(dto.getLoanId()));
        investDto.setTransferInvestId(String.valueOf(dto.getTransferInvestId()));
        investDto.setAmount(dto.getBalance());
        investDto.setSource(Source.WEB);

        if(accountModel.isNoPasswordInvest()){
            try {
                investDto.setLoginName(LoginUserInfo.getLoginName());
                BaseDto<PayDataDto> baseDto = transferService.noPasswordTransferPurchase(investDto);
                if (baseDto.getData().getStatus()) {
                    httpServletRequest.getSession().setAttribute("noPasswordInvestSuccess", true);
                    return new ModelAndView("/transfer/purchase-success");
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
        else{
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

    @RequestMapping(path = "/purchase-success", method = RequestMethod.GET)
    public ModelAndView purchaseSuccess(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView("/error/404", "responsive", true);

        InvestModel latestSuccessInvest = investService.findLatestSuccessInvest(LoginUserInfo.getLoginName());
        if (latestSuccessInvest == null) {
            return modelAndView;
        }

        if (httpServletRequest.getSession().getAttribute("noPasswordInvestSuccess") != null) {
            httpServletRequest.getSession().removeAttribute("noPasswordInvestSuccess");
            modelAndView.setViewName("/purchase-success");
            modelAndView.addObject("amount", AmountConverter.convertCentToString(latestSuccessInvest.getAmount()));
            return modelAndView;
        }

        if (latestSuccessInvest.getStatus() == InvestStatus.SUCCESS) {
            modelAndView.setViewName("/purchase-success");
            modelAndView.addObject("amount", AmountConverter.convertCentToString(latestSuccessInvest.getAmount()));
        }
        return modelAndView;
    }


}
