package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleLinkExchangeService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LinkExchangeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.WebSiteType;
import com.tuotiansudai.service.LinkExchangeService;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/link-exchange-manage", method = RequestMethod.GET)
public class LinkExchangeController {

    @Autowired
    private ConsoleLinkExchangeService consoleLinkExchangeService;

    @Autowired
    private LinkExchangeService linkExchangeService;

    @RequestMapping(value = "/link-exchange", method = RequestMethod.GET)
    public ModelAndView linkExchangeManage(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "title", required = false) String title,
                                           @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/link-exchange-list");
        int linkExchangeCount = consoleLinkExchangeService.findCountByTitle(title);
        modelAndView.addObject("linkExchangeCount", linkExchangeCount);
        modelAndView.addObject("linkExchangeList", linkExchangeService.getLinkExchangeList(title, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("title", title);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(linkExchangeCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        return modelAndView;
    }

    @RequestMapping(value = "/link-exchange/add", method = RequestMethod.GET)
    public ModelAndView linkExchange() {
        ModelAndView modelAndView = new ModelAndView("/link-exchange-edit");
        modelAndView.addObject("webSiteTypeList", WebSiteType.values());
        return modelAndView;
    }

    @RequestMapping(value = "/link-exchange/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editLinkExchange(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("/link-exchange-edit");
        modelAndView.addObject("linkExchange", this.consoleLinkExchangeService.getLinkExchangeById(id));
        modelAndView.addObject("webSiteTypeList", WebSiteType.values());
        return modelAndView;
    }

    @RequestMapping(value = "/link-exchange/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> create(@RequestBody LinkExchangeDto linkExchangeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        linkExchangeDto.setId(System.currentTimeMillis());
        this.consoleLinkExchangeService.create(linkExchangeDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/link-exchange/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> update(@RequestBody LinkExchangeDto linkExchangeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.consoleLinkExchangeService.update(linkExchangeDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/link-exchange/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> delete(@RequestBody LinkExchangeDto linkExchangeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.consoleLinkExchangeService.delete(linkExchangeDto);
        dataDto.setStatus(true);
        return baseDto;
    }
}
