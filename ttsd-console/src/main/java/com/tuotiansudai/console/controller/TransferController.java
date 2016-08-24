package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.dto.TransferRuleDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.service.TransferRuleService;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.Date;

@Controller
@RequestMapping(value = "/transfer-manage")
public class TransferController {

    static Logger logger = Logger.getLogger(TransferController.class);

    @Autowired
    private TransferRuleService transferRuleService;

    @Autowired
    private InvestTransferService investTransferService;

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
    @RequestMapping(value = "/transfer-list", method = RequestMethod.GET)
    public ModelAndView findTransferApplicationPaginationList(@RequestParam(name = "transferApplicationId", required = false) Long transferApplicationId,
                                                              @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                              @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                              @RequestParam(name = "status", required = false) TransferStatus status,
                                                              @RequestParam(name = "transferrerMobile", required = false) String transferrerMobile,
                                                              @RequestParam(name = "transfereeMobile", required = false) String transfereeMobile,
                                                              @RequestParam(name = "loanId", required = false) Long loanId,
                                                              @RequestParam(name = "source", required = false) Source source,
                                                              @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                              @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize)  {
        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = investTransferService.findTransferApplicationPaginationList(transferApplicationId, startTime, endTime, status, transferrerMobile, transfereeMobile, loanId, source, index, pageSize);
        ModelAndView mv = new ModelAndView("/transfer-list");
        mv.addObject("data",basePaginationDataDto);
        mv.addObject("transferApplicationId",transferApplicationId);
        mv.addObject("startTime",startTime);
        mv.addObject("endTime",endTime);

        mv.addObject("status",status);
        mv.addObject("sourceList", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.MOBILE, Source.IOS));
        mv.addObject("selectedSource", source);
        mv.addObject("transferrerMobile", transferrerMobile);
        mv.addObject("transfereeMobile", transfereeMobile);
        mv.addObject("loanId",loanId);
        mv.addObject("index",index);
        mv.addObject("pageSize",pageSize);
        mv.addObject("transferStatusList",TransferStatus.values());

        return mv;
    }
}
