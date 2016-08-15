package com.tuotiansudai.pointsystem.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointExchangeService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.pointsystem.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/pointsystem")
public class PointSystemController {

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private PointExchangeService pointExchangeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pointSystemHome() {
        ModelAndView modelAndView = new ModelAndView("pointsystem-index");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

 @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView pointSystemDetail() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-detail");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

    @RequestMapping(value = "/order",method = RequestMethod.GET)
    public ModelAndView pointSystemOrder() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-order");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

    @RequestMapping(value = "/task",method = RequestMethod.GET)
    public ModelAndView pointSystemTask() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-task");
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("newbiePointTasks", pointTaskService.getNewbiePointTasks(loginName));
        modelAndView.addObject("advancedPointTasks", pointTaskService.getAdvancedPointTasks(loginName));
        modelAndView.addObject("completedAdvancedPointTasks", pointTaskService.getCompletedAdvancedPointTasks(loginName));
        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

    @RequestMapping(value = "/record",method = RequestMethod.GET)
    public ModelAndView pointSystemRecord(String loginName) {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-record");
        modelAndView.addObject("recordList", pointExchangeService.findProductOrderListByLoginName(loginName));
        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

    @RequestMapping(value = "/bill", method = RequestMethod.GET)
    public ModelAndView pointSystemBill(){
        ModelAndView modelAndView = new ModelAndView("/pointsystem-bill");
        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPagination(LoginUserInfo.getLoginName(), 1, 10, null, null, null);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        modelAndView.addObject("dto",dto);
        modelAndView.addObject("responsive",true);
        return modelAndView;

    }

    @RequestMapping(value = "/bill-list", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> pointBillListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                            @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                            @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                            @RequestParam(name = "businessType", required = false) List<PointBusinessType> businessType) {
        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPagination(LoginUserInfo.getLoginName(), index, pageSize, startTime, endTime, businessType);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

}
