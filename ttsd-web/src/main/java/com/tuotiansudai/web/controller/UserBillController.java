package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.BankRechargeService;
import com.tuotiansudai.service.BankWithdrawService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
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
    private BankAccountService bankAccountService;

    @Autowired
    private BankRechargeService bankRechargeService;

    @Autowired
    private BankWithdrawService bankWithdrawService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView userBill() {
        BankAccountModel bankAccount = bankAccountService.findBankAccount(LoginUserInfo.getLoginName());
        String balance = AmountConverter.convertCentToString(bankAccount != null ? bankAccount.getBalance() : 0);
        String rechargeAmount = AmountConverter.convertCentToString(bankRechargeService.sumSuccessRechargeAmount(LoginUserInfo.getLoginName()));
        String withdrawAmount = AmountConverter.convertCentToString(bankWithdrawService.sumSuccessWithdrawByLoginName(LoginUserInfo.getLoginName()));

        ModelAndView modelAndView = new ModelAndView("/user-bill");
        modelAndView.addObject("balance", balance);
        modelAndView.addObject("rechargeAmount", rechargeAmount);
        modelAndView.addObject("withdrawAmount", withdrawAmount);
        return modelAndView;
    }

    @RequestMapping(value = "/user-bill-list-data", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getUserBillData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                          @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                          @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                          @RequestParam("status") List<UserBillBusinessType> userBillBusinessTypes) {

        return userBillService.getUserBillData(LoginUserInfo.getLoginName(), index, 10, startTime, endTime, userBillBusinessTypes);
    }

}
