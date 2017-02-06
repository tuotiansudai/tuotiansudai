package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.dto.TravelPrizeRequestDto;
import com.tuotiansudai.console.activity.service.ActivityConsoleTravelPrizeService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.CalculateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping(path = "/activity-console/activity-manage/travel")
public class TravelPrizeController {

    @Autowired
    private ActivityConsoleTravelPrizeService activityConsoleTravelPrizeService;

    @RequestMapping(path = "/travel-prize-list")
    public ModelAndView getTravelPrizeList() {
        BaseDto<BasePaginationDataDto> dto = activityConsoleTravelPrizeService.getTravelPrizeItems();
        return new ModelAndView("/travel-prize-list", "data", dto);
    }

    @RequestMapping(path = "/user-travel-list")
    public ModelAndView getAwardItems(@RequestParam(value = "mobile", defaultValue = "", required = false) String mobile,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        BaseDto<BasePaginationDataDto> dto = activityConsoleTravelPrizeService.getTravelAwardItems(mobile,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                index, pageSize);
        ModelAndView modelAndView = new ModelAndView("/user-travel-list", "data", dto);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public ModelAndView update(@Valid @ModelAttribute TravelPrizeRequestDto dto) {
        activityConsoleTravelPrizeService.update(LoginUserInfo.getLoginName(), dto);
        return new ModelAndView("redirect:/activity-console/activity-manage/travel/travel-prize-list");
    }

    @RequestMapping(value = "/{travelPrizeId:^\\d+$}/edit", method = RequestMethod.GET)
    public ModelAndView editTravelPrize(@PathVariable long travelPrizeId) {
        ModelAndView mv = new ModelAndView("/travel-prize-edit");
        mv.addObject("dto", activityConsoleTravelPrizeService.getTravelPrize(travelPrizeId));
        return mv;
    }


}
