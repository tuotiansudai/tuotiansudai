package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.AnnounceDto;
import com.tuotiansudai.message.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/announce")
public class AnnounceController {

    @Autowired
    private AnnounceService announceService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseDto<BasePaginationDataDto> getAnnounceList(@RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        return announceService.getAnnouncementList(index, 10);
    }

    @RequestMapping(value = "/{announceId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getAnnounceDetail(@PathVariable long announceId, HttpServletResponse response) {
        AnnounceDto dto = announceService.getDtoById(announceId);

        if (dto == null) {
            response.setStatus(404);
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/about/notice-detail", "announce", dto);
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }
}
