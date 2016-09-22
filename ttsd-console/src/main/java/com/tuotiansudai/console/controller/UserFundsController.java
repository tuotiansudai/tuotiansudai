package com.tuotiansudai.console.controller;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.repository.model.UserBillPaginationView;
import com.tuotiansudai.service.UserBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage")
public class UserFundsController {

    @Autowired
    private UserBillService userBillService;

    @RequestMapping(value = "/user-funds", method = RequestMethod.GET)
    public ModelAndView userFunds(@RequestParam(value = "userBillBusinessType", required = false) UserBillBusinessType userBillBusinessType,
                                  @RequestParam(value = "userBillOperationType", required = false) UserBillOperationType userBillOperationType,
                                  @RequestParam(value = "mobile", required = false) String mobile,
                                  @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                  @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                  @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/user-funds");
        List<UserBillPaginationView> userBillModels = userBillService.findUserFunds(userBillBusinessType, userBillOperationType, mobile, startTime, endTime, index, pageSize);
        int userFundsCount = userBillService.findUserFundsCount(userBillBusinessType, userBillOperationType, mobile, startTime, endTime);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("userBillBusinessType", userBillBusinessType);
        modelAndView.addObject("userBillOperationType", userBillOperationType);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userBillModels", userBillModels);
        modelAndView.addObject("userFundsCount", userFundsCount);
        modelAndView.addObject("businessTypeList", UserBillBusinessType.values());
        modelAndView.addObject("operationTypeList", UserBillOperationType.values());
        long totalPages = userFundsCount / pageSize + (userFundsCount % pageSize > 0 || userFundsCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        return modelAndView;
    }

}
