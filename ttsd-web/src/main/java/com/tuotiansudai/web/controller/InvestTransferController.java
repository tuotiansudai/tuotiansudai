package com.tuotiansudai.web.controller;

import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.service.InvestTransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/transfer")
public class InvestTransferController {

    static Logger logger = Logger.getLogger(InvestTransferController.class);

    @Autowired
    private InvestTransferService investTransferService;

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public ModelAndView investTransferApply(@RequestBody TransferApplicationDto transferApplicationDto) {
        try {
            investTransferService.investTransferApply(transferApplicationDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return new ModelAndView("");
    }

    @RequestMapping(value = "/{transferApplyId}/cancel", method = RequestMethod.POST)
    @ResponseBody
    public boolean investTransferApplyCancel(@RequestBody long id) {
         return investTransferService.investTransferApplyCancel(id);
    }

}
