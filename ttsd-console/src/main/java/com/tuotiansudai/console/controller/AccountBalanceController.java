package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.UserServiceConsole;
import com.tuotiansudai.dto.UserItemDataDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage")
public class AccountBalanceController {

    static Logger logger = Logger.getLogger(AccountBalanceController.class);

    @Autowired
    private UserServiceConsole userServiceConsole;


    @RequestMapping(value = "/account-balance")
    public ModelAndView accountBalance(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                       @RequestParam(value = "mobile", required = false) String mobile,
                                       @RequestParam(value = "balanceMin", required = false) String balanceMin,
                                       @RequestParam(value = "balanceMax", required = false) String balanceMax) {
        ModelAndView modelAndView = new ModelAndView("/account-balance");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        List<UserItemDataDto> userItemDataDtoList = userServiceConsole.findUsersAccountBalance(mobile, balanceMin, balanceMax, index, pageSize);
        modelAndView.addObject("userAccountList", userItemDataDtoList);
        long count = userServiceConsole.findUsersAccountBalanceCount(mobile, balanceMin, balanceMax);
        modelAndView.addObject("sumBalance", userServiceConsole.findUsersAccountBalanceSum(mobile, balanceMin, balanceMax));
        long totalPages = count / pageSize + (count % pageSize > 0 || count == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("balanceMin", balanceMin);
        modelAndView.addObject("balanceMax", balanceMax);
        return modelAndView;
    }
}
