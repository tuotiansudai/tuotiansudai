package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.dto.NotWorkDto;
import com.tuotiansudai.console.activity.service.NotWorkService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage")
public class NotWorkController {
    @Autowired
    NotWorkService notWorkService;

    @RequestMapping(value = "/not-work-list", method = RequestMethod.GET)
    ModelAndView getNotWorkList(@RequestParam(value = "index") int index) {
        ModelAndView modelAndView = new ModelAndView("not-work-list");
        final int pageSize = 10;
        BasePaginationDataDto<NotWorkDto> basePaginationDataDto = notWorkService.findNotWorkPagination(index, pageSize);

        modelAndView.addObject("data", basePaginationDataDto);

        return modelAndView;
    }
}
