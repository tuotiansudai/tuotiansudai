package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.FundManagementDto;
import com.tuotiansudai.dto.UserBillDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
@Controller
@RequestMapping(value = "/fund")
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

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView fund(){
        return new ModelAndView("/fund");
    }

    @RequestMapping(value = "/management", method = RequestMethod.GET)
    @ResponseBody
    public FundManagementDto fundManagement(@RequestParam("startTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,@RequestParam("endTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime,
                                       @RequestParam("index") Integer currentPage,@RequestParam("status") List<UserBillBusinessType> userBillBusinessTypes, @RequestParam("pageSize") Integer pageSize) {

        List<UserBillDto> userBillDtos = userBillService.findUserBills(userBillBusinessTypes,currentPage,startTime,endTime,pageSize!=null?pageSize:10);
        AccountModel accountModel = accountService.findByLoginName(LoginUserInfo.getLoginName());
        int countNum = userBillService.findUserBillsCount(userBillBusinessTypes,startTime,endTime);
        String sumRecharge = AmountUtil.convertCentToString(rechargeService.findSumRechargeByLoginName(LoginUserInfo.getLoginName()));
        String sumWithdraw = AmountUtil.convertCentToString(withdrawService.findSumWithdrawByLoginName(LoginUserInfo.getLoginName()));
        FundManagementDto fundManagementDto = new FundManagementDto(currentPage,pageSize!=null?pageSize:10,countNum,userBillDtos);
        fundManagementDto.setStatus(true);
        fundManagementDto.setBalance(AmountUtil.convertCentToString(accountModel != null ? accountModel.getBalance() : 0L));
        fundManagementDto.setSumRecharge(sumRecharge);
        fundManagementDto.setSumWithdraw(sumWithdraw);
        return fundManagementDto;
    }

}
