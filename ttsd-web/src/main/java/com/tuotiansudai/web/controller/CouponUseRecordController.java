package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping(value = "/coupon")
public class CouponUseRecordController {

    @Autowired
    UserCouponService userCouponService;

    @ResponseBody
    @RequestMapping(value = "/use-record", method = RequestMethod.GET)
    public BaseDto<BasePaginationDataDto> getMoneyCouponUseRecordList(@RequestParam("couponTypeList") List<CouponType> couponTypeList,
                                                                      @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        String loginName = LoginUserInfo.getLoginName();
        return userCouponService.findUseRecords(couponTypeList, loginName, index, pageSize);
    }
}
