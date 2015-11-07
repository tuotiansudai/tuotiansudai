package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.AnnouncementManagementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.service.AnnouncementManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AnnouncementManagementController {

    @Autowired
    private AnnouncementManagementService announcementManagementService;

    @RequestMapping(value = "/announceManage", method = RequestMethod.GET)
    public ModelAndView announceManage(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "title",required = false) String title,
                                                @RequestParam(value = "currentPageNo",defaultValue = "1",required = false) int currentPageNo,
                                                @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/announcement-management");
        int announcementManagementCount = announcementManagementService.findAnnouncementManagementCount(id, title);
        modelAndView.addObject("announcementManagementCount", announcementManagementCount);
        modelAndView.addObject("announcementManagements", announcementManagementService.findAnnouncementManagement(id, title, (currentPageNo - 1) * pageSize, pageSize));
        modelAndView.addObject("id",id);
        modelAndView.addObject("title",title);
        modelAndView.addObject("currentPageNo",currentPageNo);
        modelAndView.addObject("pageSize",pageSize);
        long totalPages = announcementManagementCount / pageSize + (announcementManagementCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
        boolean hasNextPage = currentPageNo < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        return modelAndView;
    }

    @RequestMapping(value = "/announce", method = RequestMethod.GET)
    public ModelAndView announce() {
        return new ModelAndView("/announce-edit");
    }

    @RequestMapping(value = "/announceEdit/{id}", method = RequestMethod.GET)
    public ModelAndView userFundsRelease(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/announce-edit");
        modelAndView.addObject("announcementManagement",this.announcementManagementService.findById(id));
        return modelAndView;
    }

    @RequestMapping(value = "/announce/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> create(@RequestBody AnnouncementManagementDto announcementManagementDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.announcementManagementService.create(announcementManagementDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/announce/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> update(@RequestBody AnnouncementManagementDto announcementManagementDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.announcementManagementService.update(announcementManagementDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/announce/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> delete(@RequestBody AnnouncementManagementDto announcementManagementDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        this.announcementManagementService.delete(announcementManagementDto);
        dataDto.setStatus(true);
        return baseDto;
    }
}
