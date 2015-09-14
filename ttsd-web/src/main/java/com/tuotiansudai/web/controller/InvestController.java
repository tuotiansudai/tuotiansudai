package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/invest")
public class InvestController {

    @Autowired
    private InvestService investService;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setInvestSource(InvestSource.WEB);
        BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
