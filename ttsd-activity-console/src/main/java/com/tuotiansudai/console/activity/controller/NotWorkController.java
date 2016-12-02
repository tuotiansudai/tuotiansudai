package com.tuotiansudai.console.activity.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.NotWorkDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.console.activity.service.ActivityConsoleNotWorkService;
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
    ActivityConsoleNotWorkService activityConsoleNotWorkService;

    @RequestMapping(value = "/not-work-list", method = RequestMethod.GET)
    public ModelAndView getNotWorkList(@RequestParam(value = "index", defaultValue = "1") int index,
                                       @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                       @RequestParam(value = "activityCategory", defaultValue = "NO_WORK_ACTIVITY", required = false) ActivityCategory activityCategory) {
        ModelAndView modelAndView = new ModelAndView("/not-work-list");
        final int pageSize = 10;
        BasePaginationDataDto<NotWorkDto> basePaginationDataDto = activityConsoleNotWorkService.findNotWorkPagination(mobile, activityCategory, index, pageSize);

        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("activityCategory", activityCategory);
        modelAndView.addObject("activityCategoryList", Lists.newArrayList(ActivityCategory.NO_WORK_ACTIVITY, ActivityCategory.ANNUAL_ACTIVITY));

        return modelAndView;
    }
}
