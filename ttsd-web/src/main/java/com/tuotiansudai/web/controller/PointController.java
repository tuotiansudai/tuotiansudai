package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.*;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/point")
public class PointController {

    private final static Logger logger = Logger.getLogger(PointController.class);

    @Autowired
    private SignInService signInService;

    @Autowired
    private PointService pointService;

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private PointExchangeService pointExchangeService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private PointBillService pointBillService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView myPoint() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/point");
        modelAndView.addObject("myPoint", pointService.getAvailablePoint(loginName));
        modelAndView.addObject("latestThreePointBills", pointBillService.getPointBillPagination(loginName, 1, 3, new Date(0), new Date(), Lists.<PointBusinessType>newArrayList()));
        modelAndView.addObject("signedIn", signInService.signInIsSuccess(loginName));
        modelAndView.addObject("newbiePointTasks", pointTaskService.getNewbiePointTasks(loginName));
        modelAndView.addObject("advancedPointTasks", pointTaskService.getAdvancedPointTasks(loginName));
        modelAndView.addObject("completedAdvancedPointTasks", pointTaskService.getCompletedAdvancedPointTasks(loginName));
        modelAndView.addObject("exchangeCoupons", pointExchangeService.findExchangeableCouponList());
        return modelAndView;
    }

    @RequestMapping(path = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> signIn() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = signInService.signIn(loginName);
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }

    @RequestMapping(value = "/{couponId}/exchange", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto exchangeCoupon(@PathVariable long couponId) {
        BaseDataDto baseDataDto = new BaseDataDto();
        long exchangePoint = couponService.findCouponExchangeByCouponId(couponId).getExchangePoint();
        boolean sufficient = pointExchangeService.exchangeableCoupon(couponId, LoginUserInfo.getLoginName());
        CouponModel couponModel = couponService.findCouponById(couponId);
        boolean couponExchangeable = couponModel.getIssuedCount() < couponModel.getTotalCount();
        if (sufficient && pointExchangeService.exchangeCoupon(couponId, LoginUserInfo.getLoginName(), exchangePoint)) {
            baseDataDto.setStatus(true);
        } else {
            baseDataDto.setStatus(false);
            if (!sufficient) {
                if (!couponExchangeable) {
                    baseDataDto.setMessage("coupon exchangeable insufficient");
                } else {
                    baseDataDto.setMessage("point insufficient");
                }
            }
        }
        return baseDataDto;
    }

    @RequestMapping(value = "/point-list", method = RequestMethod.GET)
    public ModelAndView investList() {
        return new ModelAndView("/point-bill-list");
    }

    @RequestMapping(value = "/point-bill-list-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> pointBillListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                            @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                            @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                            @RequestParam(name = "businessType", required = false) List<PointBusinessType> businessType) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPagination(loginName, index, pageSize, startTime, endTime, businessType);
        dataDto.setStatus(true);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }
}
