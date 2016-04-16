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
@RequestMapping(value = "/link-exchange-manage", method = RequestMethod.GET)
public class LinkExchangeController {

    @Autowired
    private LinkExchangeService linkExchangeService;

    @RequestMapping(value = "/link-exchange", method = RequestMethod.GET)
    public ModelAndView linkExchangeManage(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "title",required = false) String title,
                                                @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                                @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/link-exchange-list");
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

    @RequestMapping(value = "/link-exchange/add", method = RequestMethod.GET)
    public ModelAndView linkExchange() {
        return new ModelAndView("/link-exchange-edit");
    }

    @RequestMapping(value = "/link-exchange/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editLinkExchange(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/link-exchange-edit");
        modelAndView.addObject("linkExchange", this.linkExchangeService.getLinkExchangeById(id));
        return modelAndView;
    }

    @RequestMapping(value = "/link-exchange/create", method = RequestMethod.POST)
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

    @RequestMapping(value = "/link-exchange/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> update(@RequestBody LinkExchangeDto linkExchangeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.linkExchangeService.update(linkExchangeDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/link-exchange/delete", method = RequestMethod.POST)
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
