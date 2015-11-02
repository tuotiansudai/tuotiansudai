package com.tuotiansudai.console.controller;

import com.tuotiansudai.service.AnnouncementManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AnnouncementManagementController {

    @Autowired
    private AnnouncementManagementService announcementManagementService;

    @RequestMapping(value = "/announcementManagement", method = RequestMethod.GET)
    public ModelAndView announcementManagement(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "title",required = false) String title,
                                                @RequestParam(value = "currentPageNo",defaultValue = "1",required = false) int currentPageNo,
                                                @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/announcement-management");
        modelAndView.addObject("announcementManagementCount", announcementManagementService.findAnnouncementManagementCount(id,title));
        modelAndView.addObject("announcementManagements", announcementManagementService.findAnnouncementManagement(id, title, (currentPageNo - 1) * pageSize, pageSize));
        modelAndView.addObject("id",id);
        modelAndView.addObject("title",title);
        modelAndView.addObject("currentPageNo",currentPageNo);
        modelAndView.addObject("pageSize",pageSize);
        return modelAndView;
    }
    

}
