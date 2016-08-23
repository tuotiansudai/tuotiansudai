//package com.tuotiansudai.console.activity.controller;
//
//import com.tuotiansudai.activity.dto.BasePaginationDataDto;
//import com.tuotiansudai.activity.dto.UserLuxuryPrizeView;
//import com.tuotiansudai.activity.service.SeptemberActivityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.Date;
//
//@Controller
//@RequestMapping(value = "/activity-manage")
//public class SeptemberActivityController {
//    @Autowired
//    private SeptemberActivityService septemberActivityService;
//
//    @RequestMapping(value = "/luxury-prize-list")
//    public ModelAndView luxuryPrizeList(@RequestParam(value = "mobile", required = false) String mobile,
//                                        @RequestParam(value = "startTime", required = false) Date startTime,
//                                        @RequestParam(value = "endTime", required = false) Date endTime,
//                                        @RequestParam(value = "index", required = false, defaultValue = "1") int index,
//                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
//        ModelAndView mv = new ModelAndView("/test");
//        BasePaginationDataDto<UserLuxuryPrizeView> baseDto = septemberActivityService.obtainUserLuxuryPrizeList(mobile, startTime, endTime, index, pageSize);
//        mv.addObject("luxuryPrize", baseDto);
//        mv.addObject("index", index);
//        mv.addObject("pageSize", pageSize);
//        mv.addObject("startTime", startTime);
//        mv.addObject("endTime", endTime);
//        mv.addObject("mobile", mobile);
//        return mv;
//    }
//}
