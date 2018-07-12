package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.service.*;
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

    @Autowired
    private BankBindCardService bankBindCardService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView userBill() {
        Role role = LoginUserInfo.getBankRole();
        String loginName = LoginUserInfo.getLoginName();
        String balance = role == null ? "0" : AmountConverter.convertCentToString(bankAccountService.findBankAccount(loginName, role).getBalance());
        String rechargeAmount = role == null ? "0" : AmountConverter.convertCentToString(bankRechargeService.sumSuccessRechargeAmount(loginName, role));
        String withdrawAmount = role == null ? "0" : AmountConverter.convertCentToString(bankWithdrawService.sumSuccessWithdrawByLoginName(loginName, role));

        return new ModelAndView("/user-bill")
                .addObject("balance", balance)
                .addObject("rechargeAmount", rechargeAmount)
                .addObject("withdrawAmount", withdrawAmount)
                .addObject("hasAccount", role != null && bankAccountService.findBankAccount(loginName, role) != null)
                .addObject("hasBankCard", role != null && bankBindCardService.findBankCard(loginName, role) != null);
    }

    @RequestMapping(value = "/user-bill-list-data", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getUserBillData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                          @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                          @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                          @RequestParam("status") List<BankUserBillBusinessType> bankUserBillBusinessTypes) {

        return userBillService.getUserBillData(LoginUserInfo.getLoginName(), index, 10, startTime, endTime, bankUserBillBusinessTypes, LoginUserInfo.getBankRole());
    }
}
