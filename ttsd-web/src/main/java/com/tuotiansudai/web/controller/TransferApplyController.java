package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.service.InvestTransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/transfer")
public class TransferApplyController {

    private static Logger logger = Logger.getLogger(TransferApplyController.class);

    @Autowired
    private InvestTransferService investTransferService;

    @RequestMapping(value = "/invest/{investId:^\\d+$}/is-transferable", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> isInvestTransferable(@PathVariable long investId) {
        return investTransferService.isInvestTransferable(investId);
    }

    @RequestMapping(value = "/invest/{investId:^\\d+$}/apply", method = RequestMethod.GET)
    public ModelAndView transferApply(@PathVariable long investId) {
        return new ModelAndView("/create-transfer-detail", "formData", investTransferService.getApplicationForm(investId));
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
