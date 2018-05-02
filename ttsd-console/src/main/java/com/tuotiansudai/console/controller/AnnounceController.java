package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleAnnounceService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.message.dto.AnnounceCreateDto;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/announce-manage")
public class AnnounceController {

    @Autowired
    private ConsoleAnnounceService consoleAnnounceService;

    @RequestMapping(value = "/announce", method = RequestMethod.GET)
    public ModelAndView anammnounceManage(@RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/announce-list");
        int announceCount = consoleAnnounceService.findAnnounceCount(title);

        modelAndView.addObject("announceCount", announceCount);
        modelAndView.addObject("announceList", consoleAnnounceService.findAnnounce(title, index, pageSize));
        modelAndView.addObject("title", title);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("hasPreviousPage", index > 1);
        modelAndView.addObject("hasNextPage", index < PaginationUtil.calculateMaxPage(announceCount, pageSize));
        return modelAndView;
    }

    @RequestMapping(value = "/announce/create", method = RequestMethod.GET)
    public ModelAndView announce() {
        return new ModelAndView("/announce-edit");
    }

    @RequestMapping(value = "/announce/edit/{id}", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/announce-edit");
        modelAndView.addObject("announce", consoleAnnounceService.findById(id));
        return modelAndView;
    }

    @RequestMapping(value = "/announce/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> create(@RequestBody AnnounceCreateDto announceDto) {
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>(dataDto);
        consoleAnnounceService.create(announceDto, LoginUserInfo.getLoginName());
        return baseDto;
    }

    @RequestMapping(value = "/announce/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> update(@RequestBody AnnounceCreateDto announceCreateDto) {
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>(dataDto);
        consoleAnnounceService.update(announceCreateDto);
        return baseDto;
    }

    @RequestMapping(value = "/announce/delete/{announceId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> delete(@PathVariable long announceId) {
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>(dataDto);
        consoleAnnounceService.delete(announceId);
        return baseDto;
    }
}
