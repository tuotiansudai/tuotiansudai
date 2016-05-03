package com.tuotiansudai.web.controller;


import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-security.xml"})
@WebAppConfiguration
@Transactional
public class AccountControllerTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private InvestRepayService investRepayService;
    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanMapper loanMapper;

    @Test
    public void shouldAccountDetailCountIsOk(){
        UserModel userModel = createUserModelTest("test4");
        AccountModel accountModel = createAccountModel(userModel.getLoginName(),1000L);
        createUserBillModel(userModel.getLoginName());
        LoanModel loanModel = this.getFakeLoan(userModel.getLoginName(), userModel.getLoginName(), LoanStatus.REPAYING);
        InvestModel investModel = createInvest(userModel.getLoginName(),loanModel.getId());
        InvestRepayModel investRepayModelRepay = createInvestRepay(investModel.getId(),RepayStatus.REPAYING);
        InvestRepayModel investRepayModelComplete = createInvestRepay(investModel.getId(),RepayStatus.COMPLETE);
        long banlance = accountService.getBalance(userModel.getLoginName());
        long collectedReward = userBillService.findSumRewardByLoginName(userModel.getLoginName());
        long sumRepaid = investRepayService.findSumRepaidInterestByLoginName(userModel.getLoginName());
        long collectingPrincipal = investRepayService.findSumRepayingCorpusByLoginName(userModel.getLoginName());
        long sumRepaying = investRepayService.findSumRepayingInterestByLoginName(userModel.getLoginName());
        long freeze = accountService.getFreeze(userModel.getLoginName());

        assertEquals(banlance,accountModel.getBalance());
        assertEquals(collectedReward + sumRepaid,2000L);
        assertEquals(collectingPrincipal,150000L);
        assertEquals(sumRepaying,1000L);
        assertEquals(freeze,1000L);

        accountModel.setBalance(accountModel.getBalance() + 10000L);
        accountMapper.update(accountModel);
        investRepayModelComplete.setActualFee(investRepayModelComplete.getActualFee() + 500L);
        investRepayMapper.update(investRepayModelComplete);
        investRepayModelRepay.setCorpus(collectingPrincipal + 100000L);
        investRepayModelRepay.setExpectedFee(investRepayModelComplete.getExpectedFee() + 500L);
        investRepayMapper.update(investRepayModelRepay);
        long banlanceNew = accountService.getBalance(userModel.getLoginName());
        long collectedRewardNew = userBillService.findSumRewardByLoginName(userModel.getLoginName());
        long sumRepaidNew = investRepayService.findSumRepaidInterestByLoginName(userModel.getLoginName());
        long sumRepayingNew = investRepayService.findSumRepayingInterestByLoginName(userModel.getLoginName());
        long collectingPrincipalNew = investRepayService.findSumRepayingCorpusByLoginName(userModel.getLoginName());


        assertEquals(banlanceNew,banlance + 10000L);
        assertEquals(collectedRewardNew + sumRepaidNew,collectedReward + sumRepaid - 500L);
        assertEquals(collectingPrincipalNew,collectingPrincipal + 100000L);
        assertEquals(sumRepayingNew,sumRepaying - 500L);
        assertEquals(sumRepaidNew,sumRepaid - 500L);
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }


    public InvestModel createInvest(String loginName,long loanId){
        InvestModel investModel = new InvestModel();
        investModel.setId(idGenerator.generate());
        investModel.setAmount(1000);
        investModel.setCreatedTime(new Date());
        investModel.setLoginName(loginName);
        investModel.setSource(Source.IOS);
        investModel.setLoanId(loanId);
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);
        return investModel;
    }


    public InvestRepayModel createInvestRepay(long investId,RepayStatus tepayStatus ){
        List<InvestRepayModel> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            InvestRepayModel investRepayModel = new InvestRepayModel();
            investRepayModel.setId(idGenerator.generate());
            investRepayModel.setInvestId(investId);
            investRepayModel.setPeriod(i + 1);
            investRepayModel.setRepayDate(new Date());
            investRepayModel.setStatus(tepayStatus);
            investRepayModel.setActualRepayDate(investRepayModel.getRepayDate());
            investRepayModel.setCorpus(150000L);
            investRepayModel.setExpectedFee(1000L);
            investRepayModel.setActualInterest(1000L);
            investRepayModel.setDefaultInterest(1000L);
            investRepayModel.setActualFee(1000L);
            investRepayModel.setExpectedInterest(1000L);
            list.add(investRepayModel);
        }
        investRepayMapper.create(list);
        return list.get(0);

    }

    public AccountModel createAccountModel(String loginName,long balance){
        AccountModel accountModel = new AccountModel(loginName, "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setBalance(balance);
        accountModel.setFreeze(1000L);
        accountMapper.create(accountModel);
        return accountModel;
    }

    private UserBillModel createUserBillModel(String loginName){
        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setId(idGenerator.generate());
        userBillModel.setAmount(1000L);
        userBillModel.setBalance(1100L);
        userBillModel.setFreeze(1200l);
        userBillModel.setBusinessType(UserBillBusinessType.ACTIVITY_REWARD);
        userBillModel.setOperationType(UserBillOperationType.TI_BALANCE);
        userBillModel.setLoginName(loginName);
        userBillModel.setOrderId(idGenerator.generate());
        userBillModel.setOperatorLoginName(loginName);
        userBillMapper.create(userBillModel);
        return userBillModel;
    }

    private UserModel createUserModelTest(String name) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(name);
        userModel.setPassword("123abc");
        userModel.setEmail("12345@abc.com");
        userModel.setMobile("13900000000");
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModel);
        return userModel;
    }

}
