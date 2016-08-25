package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.dto.LuxuryPrizeDto;
import com.tuotiansudai.console.activity.dto.UserLuxuryPrizeDto;
import com.tuotiansudai.console.activity.service.LuxuryPrizeService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/luxury")
public class LuxuryPrizeController {
    @Autowired
    private LuxuryPrizeService luxuryPrizeService;

    @RequestMapping(value = "/user-luxury-prize-list")
    public ModelAndView userLuxuryPrizeList(@RequestParam(value = "mobile", required = false) String mobile,
                                        @RequestParam(value = "startTime", required = false) Date startTime,
                                        @RequestParam(value = "endTime", required = false) Date endTime,
                                        @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView mv = new ModelAndView("/test1");
        BasePaginationDataDto<UserLuxuryPrizeDto> baseDto = luxuryPrizeService.obtainUserLuxuryPrizeList(mobile, startTime, endTime, index, pageSize);
        mv.addObject("userLuxuryPrize", baseDto);
        mv.addObject("index", index);
        mv.addObject("pageSize", pageSize);
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("mobile", mobile);
        return mv;
    }

    @RequestMapping(value = "/luxury-prize-list")
    public ModelAndView luxuryPrizeList(){
        ModelAndView mv = new ModelAndView("/test2");
        List<LuxuryPrizeDto> baseDto = luxuryPrizeService.obtainLuxuryPrizeList();
        mv.addObject("luxuryPrize",baseDto);
        return mv;
    }

    @RequestMapping(value = "/{luxuryPrizeId}/edit",method = RequestMethod.GET)
    public ModelAndView editLuxuryPrize(@PathVariable long luxuryPrizeId){
        ModelAndView mv = new ModelAndView("/luxury-prize-edit");
        mv.addObject("dto",luxuryPrizeService.obtainLuxuryPrizeDto(luxuryPrizeId));
        return mv;
    }

    @RequestMapping(value="/edit",method = RequestMethod.POST)
    public ModelAndView editLuxuryPrize(@ModelAttribute LuxuryPrizeDto luxuryPrizeDto){
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView mv = new ModelAndView();
        luxuryPrizeService.editLuxuryPrize(luxuryPrizeDto,loginName);
        return mv;
    }




}
