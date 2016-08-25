package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.dto.TravelPrizeRequestDto;
import com.tuotiansudai.console.activity.service.TravelPrizeService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
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
    private TravelPrizeService travelPrizeService;

    @RequestMapping(path = "/travel-prize-list")
    public ModelAndView getTravelPrizeList() {
        BaseDto<BasePaginationDataDto> dto = travelPrizeService.getTravelPrizeItems();
        return new ModelAndView("/travel-prize-list", "data", dto);
    }

    @RequestMapping(path = "/user-travel-list")
    public ModelAndView getAwardItems(@RequestParam(value = "mobile", defaultValue = "", required = false) String mobile,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        BaseDto<BasePaginationDataDto> dto = travelPrizeService.getTravelAwardItems(mobile, startTime, endTime, index, pageSize);

        ModelAndView modelAndView = new ModelAndView("/user-travel-list", "data", dto);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public ModelAndView update(@Valid @ModelAttribute TravelPrizeRequestDto dto) {
        travelPrizeService.update(LoginUserInfo.getLoginName(), dto);
        return new ModelAndView("redirect:/activity-console/activity-manage/travel/travel-prize-list");
    }

    @RequestMapping(value = "/{travelPrizeId:^\\d+$}/edit",method = RequestMethod.GET)
    public ModelAndView editTravelPrize(@PathVariable long travelPrizeId){
        ModelAndView mv = new ModelAndView("/travel-prize-edit");
        mv.addObject("dto", travelPrizeService.getTravelPrize(travelPrizeId));
        return mv;
    }


}
