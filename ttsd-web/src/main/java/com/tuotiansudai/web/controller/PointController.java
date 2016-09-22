package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

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
}
