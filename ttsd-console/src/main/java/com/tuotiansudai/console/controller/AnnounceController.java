package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/announce-manage", method = RequestMethod.GET)
public class AnnounceController {

    @Autowired
    private AnnounceService announceService;

    @RequestMapping(value = "/announce", method = RequestMethod.GET)
    public ModelAndView announceManage(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "title",required = false) String title,
                                                @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                                @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/announce-list");
        int announceCount = announceService.findAnnounceCount(id, title);
        modelAndView.addObject("announceCount", announceCount);
        modelAndView.addObject("announceList", announceService.findAnnounce(id, title, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("id",id);
        modelAndView.addObject("title",title);
        modelAndView.addObject("index",index);
        modelAndView.addObject("pageSize",pageSize);
        long totalPages = announceCount / pageSize + (announceCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        return modelAndView;
    }

    @RequestMapping(value = "/announce/add", method = RequestMethod.GET)
    public ModelAndView announce() {
        return new ModelAndView("/announce-edit");
    }

    @RequestMapping(value = "/announce/edit/{id}", method = RequestMethod.GET)
    public ModelAndView userFundsRelease(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/announce-edit");
        modelAndView.addObject("announce", this.announceService.findById(id));
        return modelAndView;
    }

    @RequestMapping(value = "/announce/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> create(@RequestBody AnnounceDto announceDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.announceService.create(announceDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/announce/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> update(@RequestBody AnnounceDto announceDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.announceService.update(announceDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/announce/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> delete(@RequestBody AnnounceDto announceDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.announceService.delete(announceDto);
        dataDto.setStatus(true);
        return baseDto;
    }
}
