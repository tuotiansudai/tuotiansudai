package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.service.ConsoleUserService;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping(value = "/finance-manage")
public class AccountBalanceController {

    static Logger logger = Logger.getLogger(AccountBalanceController.class);

    @Autowired
    private ConsoleUserService consoleUserService;


    @RequestMapping(value = "/account-balance")
    public ModelAndView accountBalance(@RequestParam(value = "role", defaultValue ="BANK_INVESTOR", required = false)Role role,
                                       @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "mobile", required = false) String mobile,
                                       @RequestParam(value = "balanceMin", required = false) String balanceMin,
                                       @RequestParam(value = "balanceMax", required = false) String balanceMax) {
        int pageSize = 10;
        if (StringUtils.isEmpty(balanceMax)) {
            balanceMax = String.valueOf(Long.MAX_VALUE);
        }
        ModelAndView modelAndView = new ModelAndView("/account-balance");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        List<UserItemDataDto> userItemDataDtoList = consoleUserService.findUsersAccountBalance(role,mobile, balanceMin, balanceMax, index, pageSize);
        modelAndView.addObject("userAccountList", userItemDataDtoList);
        long count = consoleUserService.findUsersAccountBalanceCount(role,mobile, balanceMin, balanceMax);
        modelAndView.addObject("sumBalance", consoleUserService.findUsersAccountBalanceSum(role,mobile, balanceMin, balanceMax));
        long totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("balanceMin", balanceMin);
        modelAndView.addObject("balanceMax", balanceMax);
        modelAndView.addObject("role",role);
        return modelAndView;
    }
}
