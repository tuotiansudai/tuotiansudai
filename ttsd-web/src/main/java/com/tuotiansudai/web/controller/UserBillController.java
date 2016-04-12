package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.log4j.Logger;
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
@RequestMapping(value = "/user-bill")
public class UserBillController {

    static Logger logger = Logger.getLogger(UserBillController.class);

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawService withdrawService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView userBill() {
        AccountModel accountModel = accountService.findByLoginName(LoginUserInfo.getLoginName());
        String balance = AmountConverter.convertCentToString(accountModel != null ? accountModel.getBalance() : 0);
        String rechargeAmount = AmountConverter.convertCentToString(rechargeService.sumSuccessRechargeAmount(LoginUserInfo.getLoginName()));
        String withdrawAmount = AmountConverter.convertCentToString(withdrawService.sumSuccessWithdrawAmount(LoginUserInfo.getLoginName()));
        ModelAndView modelAndView = new ModelAndView("/user-bill");
        modelAndView.addObject("balance", balance);
        modelAndView.addObject("rechargeAmount", rechargeAmount);
        modelAndView.addObject("withdrawAmount", withdrawAmount);
        return modelAndView;
    }

    @RequestMapping(value = "/user-bill-list-data", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getUserBillData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                            @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                            @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                            @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                            @RequestParam("status") List<UserBillBusinessType> userBillBusinessTypes) {

        return userBillService.getUserBillData(LoginUserInfo.getLoginName(), index, pageSize, startTime, endTime, userBillBusinessTypes);
    }

}
