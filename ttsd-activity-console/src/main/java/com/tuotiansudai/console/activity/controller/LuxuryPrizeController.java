package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.dto.LuxuryPrizeRequestDto;
import com.tuotiansudai.console.activity.service.LuxuryPrizeService;
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
@RequestMapping(value = "/activity-console/activity-manage/luxury")
public class LuxuryPrizeController {
    @Autowired
    private LuxuryPrizeService luxuryPrizeService;

    @RequestMapping(value = "/user-luxury-list")
    public ModelAndView userLuxuryPrizeList(@RequestParam(value = "mobile", required = false) String mobile,
                                            @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                            @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView mv = new ModelAndView("/user-luxury-list");
        BaseDto<BasePaginationDataDto> baseDto = luxuryPrizeService.obtainUserLuxuryPrizeList(mobile, startTime, endTime, index, pageSize);
        mv.addObject("data", baseDto);
        mv.addObject("index", index);
        mv.addObject("pageSize", pageSize);
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("mobile", mobile);
        return mv;
    }

    @RequestMapping(value = "/luxury-prize-list")
    public ModelAndView luxuryPrizeList(){
        ModelAndView mv = new ModelAndView("/luxury-prize-list");
        BaseDto<BasePaginationDataDto> dto = luxuryPrizeService.obtainLuxuryPrizeList();
        mv.addObject("data",dto);
        return mv;
    }

    @RequestMapping(value = "/{luxuryPrizeId}/edit",method = RequestMethod.GET)
    public ModelAndView editLuxuryPrize(@PathVariable long luxuryPrizeId){
        ModelAndView mv = new ModelAndView("/luxury-prize-edit");
        mv.addObject("dto",luxuryPrizeService.obtainLuxuryPrizeDto(luxuryPrizeId));
        return mv;
    }

    @RequestMapping(value="/edit",method = RequestMethod.POST)
    public ModelAndView editLuxuryPrize(@Valid @ModelAttribute LuxuryPrizeRequestDto luxuryPrizeRequestDto){
        String loginName = LoginUserInfo.getLoginName();
        luxuryPrizeService.editLuxuryPrize(luxuryPrizeRequestDto,loginName);
        return new ModelAndView("redirect:/activity-console/activity-manage/luxury/luxury-prize-list");
    }




}
