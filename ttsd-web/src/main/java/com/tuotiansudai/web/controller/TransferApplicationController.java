package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.TransferApplicationDetailDto;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/transfer")
public class TransferApplicationController {

    @Autowired
    private TransferService transferService;

    @RequestMapping(value = "/{transferApplicationId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getTransferApplicationDetail(@PathVariable long transferApplicationId, Model model) {
        TransferApplicationDetailDto dto = transferService.getTransferApplicationDetailDto(transferApplicationId, LoginUserInfo.getLoginName());
        if (dto == null) {
            return new ModelAndView("/error/404");
        }
        ModelAndView modelAndView = new ModelAndView("/transfer-detail", "responsive", true);
        modelAndView.addObject("transferApplication", dto);
        modelAndView.addObject("transferApplicationReceiver", transferService.getTransferee(transferApplicationId, LoginUserInfo.getLoginName()));
        return modelAndView;
    }

}
