package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.service.UserBillService;
import org.joda.time.DateTime;
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
public class UserFundsController {

    @Autowired
    private UserBillService userBillService;

    @RequestMapping(value = "/userFunds", method = RequestMethod.GET)
    public ModelAndView userFunds(@RequestParam(value = "userBillBusinessType",required = false) UserBillBusinessType userBillBusinessType,
                                   @RequestParam(value = "userBillOperationType",required = false) UserBillOperationType userBillOperationType,
                                   @RequestParam(value = "loginName",required = false) String loginName,
                                   @RequestParam(value = "startTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,
                                   @RequestParam(value = "endTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime,
                                   @RequestParam(value = "currentPageNo",defaultValue = "1",required = false) int currentPageNo,
                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/user-funds");
        DateTime dateTime = new DateTime(endTime);
        List<UserBillModel> userBillModels = userBillService.findUserFunds(userBillBusinessType, userBillOperationType, loginName, startTime, endTime!=null?dateTime.plusDays(1).toDate():endTime, currentPageNo, pageSize);
        int userFundsCount = userBillService.findUserFundsCount(userBillBusinessType, userBillOperationType, loginName, startTime, endTime!=null?dateTime.plusDays(1).toDate():endTime);
        modelAndView.addObject("loginName",loginName);
        modelAndView.addObject("startTime",startTime);
        modelAndView.addObject("endTime",endTime);
        modelAndView.addObject("userBillBusinessType",userBillBusinessType);
        modelAndView.addObject("userBillOperationType",userBillOperationType);
        modelAndView.addObject("currentPageNo",currentPageNo);
        modelAndView.addObject("pageSize",pageSize);
        modelAndView.addObject("userBillModels",userBillModels);
        modelAndView.addObject("userFundsCount",userFundsCount);
        modelAndView.addObject("businessTypeList",UserBillBusinessType.values());
        modelAndView.addObject("operationTypeList",UserBillOperationType.values());
        long totalPages = userFundsCount / pageSize + (userFundsCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
        boolean hasNextPage = currentPageNo < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        return modelAndView;
    }


}
