package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.TravelPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(path = "/activity-manage/travel")
public class TravelPrizeController {

    @Autowired
    private TravelPrizeService travelPrizeService;

    public ModelAndView getAwardItems(@RequestParam(value = "mobile", defaultValue = "", required = false) String mobile,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        travelPrizeService.getTravelAwardItems(mobile, startTime, endTime, index, pageSize);

        return null;
    }
}
