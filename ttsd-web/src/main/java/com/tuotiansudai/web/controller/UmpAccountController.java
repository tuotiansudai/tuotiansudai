package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserFundView;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
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
@RequestMapping(value = "/ump/account")
public class UmpAccountController {

    private final UserService userService;

    private final UserFundMapper userFundMapper;

    private final UserBillService userBillService;

    private final RechargeMapper rechargeMapper;

    private final WithdrawMapper withdrawMapper;

    private final UserRoleMapper userRoleMapper;

    private final BankCardMapper bankCardMapper;

    @Autowired
    public UmpAccountController(UserService userService, UserFundMapper userFundMapper, UserBillService userBillService, RechargeMapper rechargeMapper, WithdrawMapper withdrawMapper, UserRoleMapper userRoleMapper, BankCardMapper bankCardMapper){
        this.userService = userService;
        this.userFundMapper = userFundMapper;
        this.userBillService = userBillService;
        this.rechargeMapper = rechargeMapper;
        this.withdrawMapper = withdrawMapper;
        this.userRoleMapper = userRoleMapper;
        this.bankCardMapper = bankCardMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView account(){
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/ump-account");
        UserFundView userFundView = userFundMapper.findUmpByLoginName(loginName);

        modelAndView.addObject("isLoaner", userRoleMapper.findByLoginNameAndRole(loginName, Role.UMP_LOANER) != null);
        modelAndView.addObject("userName", userService.findByMobile(loginName).getUserName());
        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(loginName);
        modelAndView.addObject("bankCard", bankCardModel == null ? null : bankCardModel.getCardNumber());
        modelAndView.addObject("balance", userFundView.getBalance()); //余额
        modelAndView.addObject("expectedTotalCorpus", userFundView.getExpectedTotalCorpus()); //待收投资本金
        modelAndView.addObject("expectedTotalInterest", userFundView.getExpectedTotalInterest()); //待收预期收益

        modelAndView.addObject("referRewardAmount", userFundView.getReferRewardAmount()); //已收推荐奖励
        modelAndView.addObject("actualTotalInterest", userFundView.getActualTotalInterest()); //已收投资收益
        //已收优惠券奖励 = 已收红包奖励+已收加息券奖励
        modelAndView.addObject("actualCouponInterest", userFundView.getActualCouponInterest() + userFundView.getRedEnvelopeAmount());

        modelAndView.addObject("actualTotalExtraInterest", userFundView.getActualTotalExtraInterest()); //已收投资奖励=阶梯加息+现金补贴
        modelAndView.addObject("expectedTotalExtraInterest", userFundView.getExpectedTotalExtraInterest()); //待收收投资奖励

        modelAndView.addObject("expectedExperienceInterest", userFundView.getExpectedExperienceInterest()); //待收体验金收益
        modelAndView.addObject("expectedCouponInterest", userFundView.getExpectedCouponInterest()); //待收优惠券收益
        modelAndView.addObject("actualExperienceInterest", userFundView.getActualExperienceInterest()); //已收体验金收益

        //累计收益(分)=已收投资收益+已收投资奖励(阶梯加息+现金补贴)+已收优惠券奖励(已收红包奖励+已收加息券奖励)+已收推荐奖励+已收体验金收益
        modelAndView.addObject("totalIncome", userFundView.getActualTotalInterest()
                + userFundView.getActualTotalExtraInterest()
                + userFundView.getActualCouponInterest()
                + userFundView.getRedEnvelopeAmount()
                + userFundView.getReferRewardAmount()
                + userFundView.getActualExperienceInterest());

        String rechargeAmount = AmountConverter.convertCentToString(rechargeMapper.findSumSuccessRechargeByLoginName(loginName));
        String withdrawAmount = AmountConverter.convertCentToString(withdrawMapper.findSumSuccessWithdrawByLoginName(loginName));

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

        return userBillService.getUmpUserBillData(LoginUserInfo.getLoginName(), index, 10, startTime, endTime, userBillBusinessTypes);
    }
}
