package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleUserBillService;
import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.enums.BankUserBillOperationType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.AccountType;
import com.tuotiansudai.repository.model.BankUserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.util.PaginationUtil;
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
    private ConsoleUserBillService consoleUserBillService;

    @RequestMapping(value = "/user-funds", method = RequestMethod.GET)
    public ModelAndView userFunds(@RequestParam(value = "accountType", defaultValue ="INVESTOR", required = false)AccountType accountType,
                                  @RequestParam(value = "businessType", required = false) BankUserBillBusinessType businessType,
                                  @RequestParam(value = "operationType", required = false) BankUserBillOperationType operationType,
                                  @RequestParam(value = "businessTypeUMP", required = false) UserBillBusinessType businessTypeUMP,
                                  @RequestParam(value = "operationTypeUMP", required = false) UserBillOperationType operationTypeUMP,
                                  @RequestParam(value = "mobile", required = false) String mobile,
                                  @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                  @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                  @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/user-funds");
        List<? extends Object> userBillModels=null;
        long userFundsCount=0;
        if(accountType !=null && accountType == AccountType.UMP){
            userBillModels=consoleUserBillService.findUserFunds(businessTypeUMP, operationTypeUMP, mobile, startTime, endTime, index, pageSize);
            userFundsCount=consoleUserBillService.findUserFundsCount(businessTypeUMP, operationTypeUMP, mobile, startTime, endTime);
        }else{
            userBillModels=consoleUserBillService.findUserFunds(accountType,businessType, operationType, mobile, startTime, endTime, index, pageSize);
            userFundsCount=consoleUserBillService.findUserFundsCount(accountType,businessType, operationType, mobile, startTime, endTime);
        }
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("businessType", businessType);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userBillModels", userBillModels);
        modelAndView.addObject("userFundsCount", userFundsCount);
        modelAndView.addObject("businessTypes", BankUserBillBusinessType.values());
        modelAndView.addObject("operationTypes", BankUserBillOperationType.values());
        long totalPages = PaginationUtil.calculateMaxPage(userFundsCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("accountTypeList", AccountType.values());
        modelAndView.addObject("accountType",accountType);
        modelAndView.addObject("businessTypeUMP", businessTypeUMP);
        modelAndView.addObject("businessTypeUMPList", UserBillBusinessType.values());
        modelAndView.addObject("operationTypeUMP",operationTypeUMP);
        modelAndView.addObject("operationTypeUMPList", UserBillOperationType.values());
        return modelAndView;
    }

}
