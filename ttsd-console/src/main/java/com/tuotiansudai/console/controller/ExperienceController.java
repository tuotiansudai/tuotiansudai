package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.dto.ExperienceBalancePaginationItemDto;
import com.tuotiansudai.console.service.ConsoleExperienceService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.RepayStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@Controller
@RequestMapping(value = "/experience-manage")
public class ExperienceController {
    @Autowired
    private ConsoleExperienceService consoleExperienceService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ModelAndView balance(@RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "balanceMin", required = false) String balanceMin,
                                @RequestParam(value = "balanceMax", required = false) String balanceMax,
                                @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/experience-balance");
        int pageSize = 10;
        BasePaginationDataDto<ExperienceBalancePaginationItemDto> basePaginationDataDto = consoleExperienceService.balance(mobile,balanceMin,balanceMax,index,pageSize);
        long sumExperienceBalance = consoleExperienceService.sumExperienceBalance(mobile,balanceMin,balanceMax);
        modelAndView.addObject("baseDto",basePaginationDataDto);
        modelAndView.addObject("sumExperienceBalance",sumExperienceBalance);
        modelAndView.addObject("mobile",mobile);
        modelAndView.addObject("balanceMin",balanceMin);
        modelAndView.addObject("balanceMin",balanceMax);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("hasNextPage", basePaginationDataDto.isHasNextPage());
        modelAndView.addObject("hasPreviousPage", basePaginationDataDto.isHasPreviousPage());
        return modelAndView;
    }

    @RequestMapping(value = "/repay-detail", method = RequestMethod.GET)
    public ModelAndView repayDetail(@RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "repayDateMin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date repayDateMin,
                                @RequestParam(value = "repayDateMax", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date repayDateMax,
                                @RequestParam(value = "repayStatus", required = false) RepayStatus repayStatus,
                                @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/experience-repay-detail");

        int pageSize = 10;
    }


}
