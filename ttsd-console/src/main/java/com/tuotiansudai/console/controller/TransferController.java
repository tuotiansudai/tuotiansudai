package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.TransferRuleDto;
import com.tuotiansudai.transfer.service.TransferRuleService;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/transfer-manage")
public class TransferController {

    static Logger logger = Logger.getLogger(TransferController.class);

    @Autowired
    private TransferRuleService transferRuleService;

    @RequestMapping(value = "/transfer-rule", method = RequestMethod.GET)
    public ModelAndView getRule(Model model) {
        TransferRuleDto transferRuleDto = transferRuleService.getTransferRule();
        ModelAndView modelAndView = new ModelAndView("/transfer-rule", "transferRule", transferRuleDto);
        modelAndView.addObject("applicationCommit", model.containsAttribute("update") && transferRuleDto.isHasTask());
        modelAndView.addObject("verificationCommit", model.containsAttribute("update") && !transferRuleDto.isHasTask());
        return modelAndView;
    }

    @RequestMapping(value = "/transfer-rule", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView updateRule(@ModelAttribute TransferRuleDto transferRuleDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        transferRuleService.updateTransferRule(transferRuleDto, LoginUserInfo.getLoginName(), RequestIPParser.parse(request));
        redirectAttributes.addFlashAttribute("update", true);
        return new ModelAndView("redirect:/transfer-manage/transfer-rule");
    }

}
