package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ReplaceBankCardDto;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.service.BandCardManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/user-manage")
public class BandCardManagerController {
    @Autowired
    private BandCardManagerService bandCardManagerService;

    @RequestMapping(value = "/bind-card",method = RequestMethod.GET)
    public ModelAndView agentManage(@RequestParam(value = "mobile",required = false) String mobile,
                                    @RequestParam(value = "index",defaultValue = "1",required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/bind-card-list");
        int pageSize = 10;
        int count = bandCardManagerService.queryCountReplaceBankCardRecord(mobile);
        List<ReplaceBankCardDto> replaceBankCardDtoList = bandCardManagerService.queryReplaceBankCardRecord(mobile, (index - 1) * pageSize, pageSize);
        modelAndView.addObject("count",count);
        modelAndView.addObject("replaceBankCardDtoList",replaceBankCardDtoList);
        modelAndView.addObject("index",index);
        modelAndView.addObject("pageSize",pageSize);
        modelAndView.addObject("mobile",mobile);
        long totalPages = count / pageSize + (count % pageSize > 0 || count == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/update-remark", method = RequestMethod.GET)
    public BaseDto<BaseDataDto> updateRemark(long bankCardId, String remark) {
        bandCardManagerService.updateRemark(bankCardId,remark);
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }



}
