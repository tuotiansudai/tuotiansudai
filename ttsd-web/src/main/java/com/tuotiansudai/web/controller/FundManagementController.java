package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.UserBillDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
@Controller
@RequestMapping(value = "/fundManagement")
public class FundManagementController {

    static Logger logger = Logger.getLogger(FundManagementController.class);

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawService withdrawService;

    @RequestMapping(value = "/userBillBusinessType/{userBillBusinessType}/currentPage/{currentPage}/startTime{startTime}/endTime/{endTime}", method = RequestMethod.GET)
    public ModelAndView fundManagement(@PathVariable String userBillBusinessType,@PathVariable int currentPage,@PathVariable String startTime,@PathVariable String endTime) {
        ModelAndView modelAndView = new ModelAndView("/fundManagement");
        List<UserBillDto> userBillDtos = userBillService.findUserBills(userBillBusinessType,currentPage,startTime,endTime);
        modelAndView.addObject("userBillDtos",userBillDtos);
        AccountModel accountModel = accountService.findByLoginName(LoginUserInfo.getLoginName());
        modelAndView.addObject("accountModel",accountModel);
        int countNum = userBillService.findUserBillsCount(userBillBusinessType,startTime,endTime);
        modelAndView.addObject("countNum",countNum);
        return modelAndView;
    }

}
