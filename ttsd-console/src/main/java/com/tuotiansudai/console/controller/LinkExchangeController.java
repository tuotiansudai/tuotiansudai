package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LinkExchangeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.service.LinkExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/linkexchange-manage", method = RequestMethod.GET)
public class LinkExchangeController {

    @Autowired
    private LinkExchangeService linkExchangeService;

    @RequestMapping(value = "/linkexchange", method = RequestMethod.GET)
    public ModelAndView linkExchangeManage(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "title",required = false) String title,
                                                @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                                @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/linkexchange-list");
        int linkExchangeCount = linkExchangeService.findCountByTitle(title);
        modelAndView.addObject("linkExchangeCount", linkExchangeCount);
        modelAndView.addObject("linkExchangeList", linkExchangeService.getLinkExchangeList(title, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("title",title);
        modelAndView.addObject("index",index);
        modelAndView.addObject("pageSize",pageSize);
        long totalPages = linkExchangeCount / pageSize + (linkExchangeCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        return modelAndView;
    }

    @RequestMapping(value = "/linkexchange/add", method = RequestMethod.GET)
    public ModelAndView linkExchange() {
        return new ModelAndView("/linkexchange-edit");
    }

    @RequestMapping(value = "/linkexchange/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editLinkExchange(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/linkexchange-edit");
        modelAndView.addObject("linkexchange", this.linkExchangeService.getLinkExchangeById(id));
        return modelAndView;
    }

    @RequestMapping(value = "/linkexchange/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> create(@RequestBody LinkExchangeDto linkExchangeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        linkExchangeDto.setId(System.currentTimeMillis());
        this.linkExchangeService.create(linkExchangeDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/linkexchange/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> update(@RequestBody LinkExchangeDto linkExchangeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.linkExchangeService.update(linkExchangeDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/linkexchange/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> delete(@RequestBody LinkExchangeDto linkExchangeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.linkExchangeService.delete(linkExchangeDto);
        dataDto.setStatus(true);
        return baseDto;
    }
}
