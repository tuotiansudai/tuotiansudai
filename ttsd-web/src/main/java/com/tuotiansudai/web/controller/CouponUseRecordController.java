package com.tuotiansudai.web.controller;


import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/coupon")
public class CouponUseRecordController {

    @Autowired
    UserCouponService userCouponService;

    @ResponseBody
    @RequestMapping(value = "/use-record", method = RequestMethod.GET)
    public BaseDto<BasePaginationDataDto> getAnnounceList(@RequestParam(value = "loginName", required = true) String loginName,
                                                          @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return userCouponService.findUseRecords(loginName, index, pageSize);
    }

}
