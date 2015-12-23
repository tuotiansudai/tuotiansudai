package com.tuotiansudai.console.controller;

import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/finance-manage")
public class AccountBalanceController {

    static Logger logger = Logger.getLogger(AccountBalanceController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/account-balance")
    public ModelAndView accountBalance(@RequestParam(value = "currentPageNo", defaultValue = "1", required = false) int currentPageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/account-balance");
        modelAndView.addObject("currentPageNo", currentPageNo);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userAccountList",userService.findUsersAccountBalance(currentPageNo, pageSize));
        int count = userService.findUsersAccountBalanceCount();
        modelAndView.addObject("sumBalance",userService.findUsersAccountBalanceSum());
        long totalPages = count / pageSize + (count % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
        boolean hasNextPage = currentPageNo < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        return modelAndView;
    }

}
