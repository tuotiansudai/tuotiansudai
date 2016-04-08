package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.repository.model.PushSource;
import com.tuotiansudai.jpush.repository.model.PushType;
import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class JPushAlertServiceTest {
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private JPushAlertService jPushAlertService;

    @Autowired
    private JPushAlertMapper jPushAlertMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    private static final long loanId = 10000000001L;
    private static final long loanRepayId = 100000003L;

    private void createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }

    private void createInvest(String loginName, long loanId, long investId) {
        InvestModel model = new InvestModel();
        model.setAmount(1000000);
        model.setCreatedTime(new Date());
        model.setId(investId);
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.WEB);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
    }

    private void createLoanRepay(long loanRepayId, int period) {
        List<LoanRepayModel> loanRepayModelList = Lists.newArrayList();
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(loanRepayId);
        loanRepayModel.setLoanId(loanId);
        loanRepayModel.setStatus(RepayStatus.REPAYING);
        loanRepayModel.setCorpus(100);
        loanRepayModel.setExpectedInterest(100);
        loanRepayModel.setActualInterest(100);
        loanRepayModel.setDefaultInterest(100);
        loanRepayModel.setExpectedInterest(100);
        loanRepayModel.setPeriod(period);
        loanRepayModel.setRepayDate(new Date());
        loanRepayModel.setActualRepayDate(new Date());
        loanRepayModel.setCreatedTime(new Date());
        loanRepayModelList.add(loanRepayModel);
        loanRepayMapper.create(loanRepayModelList);
    }


    private void createInvestRepay(long investId, RepayStatus repayStatus, int period) {
        List<InvestRepayModel> investRepayModelList = Lists.newArrayList();
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setCorpus(100);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setActualInterest(100);
        investRepayModel.setDefaultInterest(100);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setExpectedFee(100);
        investRepayModel.setActualFee(100);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(new Date());
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setCreatedTime(new Date());
        investRepayModelList.add(investRepayModel);
        investRepayMapper.create(investRepayModelList);
    }

    public void createJPushAlert(){
        JPushAlertModel jPushAlertModel1 = new JPushAlertModel();
        jPushAlertModel1.setId(idGenerator.generate());
        jPushAlertModel1.setName("测试还款");
        jPushAlertModel1.setPushType(PushType.REPAY_ALERT);
        jPushAlertModel1.setPushSource(PushSource.ALL);
        jPushAlertModel1.setIsAutomatic(true);
        jPushAlertModel1.setContent("dfsdfdfdsf");
        jPushAlertModel1.setCreatedTime(new Date());
        jPushAlertMapper.create(jPushAlertModel1);
    }

    @Before
    public void init() throws Exception {
        createJPushAlert();
        createUserByUserId("testuser123");
        createLoanByUserId("testuser123", loanId);

        createLoanRepay(loanRepayId, 3);
        createLoanRepay(idGenerator.generate(), 2);
        createLoanRepay(idGenerator.generate(), 1);
        long investId = idGenerator.generate();

        createInvest("testuser123", loanId, investId);

        createInvestRepay(investId, RepayStatus.COMPLETE, 1);
        createInvestRepay(investId,RepayStatus.OVERDUE,2);
        createInvestRepay(investId,RepayStatus.REPAYING,3);
    }


    @Test
    public void shouldAutoJPushRepayAlertWhenNoDefaultInterest(){

        jPushAlertService.autoJPushRepayAlert(loanRepayId);

    }

}
