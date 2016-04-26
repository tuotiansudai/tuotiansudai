package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.service.impl.ReferrerRewardServiceImpl;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
@Transactional
public class MockReferrerRewardServiceTest {
    @Mock
    private IdGenerator idGenerator;
    @InjectMocks
    private ReferrerRewardServiceImpl referrerRewardService;

    @Mock
    private ReferrerRelationMapper referrerRelationMapper;

    @Mock
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AgentLevelRateMapper agentLevelRateMapper;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldRewardReferrerExistedInAgentIsSuccess(){
        when(idGenerator.generate()).thenReturn(90000001L);
        UserModel loginNameModel = createFakeUser("loginName");
        UserModel referrerModel = createFakeUser("referrerLoginName");
        UserModel investorModel = createFakeUser("investorLoginName");
        UserModel loanerModel = createFakeUser("loanerLoginName");
        LoanModel loanModel = fakeLoanModel(loanerModel);
        InvestModel investModel = getFakeInvestModel(loanModel, investorModel);
        List<InvestModel> successInvestList = Lists.newArrayList();
        successInvestList.add(investModel);
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();

        referrerRelationModel.setLevel(2);
        referrerRelationModel.setReferrerLoginName(referrerModel.getLoginName());
        referrerRelationModel.setLoginName(loginNameModel.getLoginName());
        List<ReferrerRelationModel> referrerRelationModels = Lists.newArrayList();
        referrerRelationModels.add(referrerRelationModel);
        when(referrerRelationMapper.findByLoginName(anyString())).thenReturn(referrerRelationModels);
        when(investReferrerRewardMapper.findByInvestIdAndReferrer(anyLong(), anyString())).thenReturn(null);
        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setLoginName(referrerModel.getLoginName());
        userRoleModel.setCreatedTime(new Date());
        userRoleModel.setRole(Role.STAFF);
        List<UserRoleModel> userRoleModels = Lists.newArrayList();
        userRoleModels.add(userRoleModel);
        when(userRoleMapper.findByLoginName(anyString())).thenReturn(userRoleModels);

        when(accountMapper.findByLoginName(anyString())).thenReturn(null);

        AgentLevelRateModel agentLevelRateModel = new AgentLevelRateModel();
        agentLevelRateModel.setLevel(2);
        agentLevelRateModel.setRate(0.033);
        agentLevelRateModel.setLoginName(loginNameModel.getLoginName());
        agentLevelRateModel.setId(90000002L);

        when(agentLevelRateMapper.findAgentLevelRateByLoginNameAndLevel(anyString(),anyInt())).thenReturn(agentLevelRateModel);

        referrerRewardService.rewardReferrer(loanModel, successInvestList);

        ArgumentCaptor<InvestReferrerRewardModel> investReferrerRewardModelArgumentCaptor = ArgumentCaptor.forClass(InvestReferrerRewardModel.class);
        verify(investReferrerRewardMapper,times(1)).create(investReferrerRewardModelArgumentCaptor.capture());
        assertEquals(8, investReferrerRewardModelArgumentCaptor.getValue().getAmount());

    }

    @Test
    public void shouldRewardReferrerNotExistedInAgentIsSuccess(){
        List<Double> referrerStaffRoleReward = Lists.newArrayList();
        referrerStaffRoleReward.add(0.1);
        referrerStaffRoleReward.add(0.2);
        referrerStaffRoleReward.add(0.3);
        referrerStaffRoleReward.add(0.4);
        ReflectionTestUtils.setField(referrerRewardService, "referrerStaffRoleReward",referrerStaffRoleReward);
        when(idGenerator.generate()).thenReturn(90000001L);
        UserModel loginNameModel = createFakeUser("loginName");
        UserModel referrerModel = createFakeUser("referrerLoginName");
        UserModel investorModel = createFakeUser("investorLoginName");
        UserModel loanerModel = createFakeUser("loanerLoginName");
        LoanModel loanModel = fakeLoanModel(loanerModel);
        InvestModel investModel = getFakeInvestModel(loanModel, investorModel);
        List<InvestModel> successInvestList = Lists.newArrayList();
        successInvestList.add(investModel);
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();

        referrerRelationModel.setLevel(2);
        referrerRelationModel.setReferrerLoginName(referrerModel.getLoginName());
        referrerRelationModel.setLoginName(loginNameModel.getLoginName());
        List<ReferrerRelationModel> referrerRelationModels = Lists.newArrayList();
        referrerRelationModels.add(referrerRelationModel);
        when(referrerRelationMapper.findByLoginName(anyString())).thenReturn(referrerRelationModels);
        when(investReferrerRewardMapper.findByInvestIdAndReferrer(anyLong(), anyString())).thenReturn(null);
        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setLoginName(referrerModel.getLoginName());
        userRoleModel.setCreatedTime(new Date());
        userRoleModel.setRole(Role.STAFF);
        List<UserRoleModel> userRoleModels = Lists.newArrayList();
        userRoleModels.add(userRoleModel);
        when(userRoleMapper.findByLoginName(anyString())).thenReturn(userRoleModels);

        when(accountMapper.findByLoginName(anyString())).thenReturn(null);

        when(agentLevelRateMapper.findAgentLevelRateByLoginNameAndLevel(anyString(),anyInt())).thenReturn(null);

        referrerRewardService.rewardReferrer(loanModel, successInvestList);

        ArgumentCaptor<InvestReferrerRewardModel> investReferrerRewardModelArgumentCaptor = ArgumentCaptor.forClass(InvestReferrerRewardModel.class);
        verify(investReferrerRewardMapper,times(1)).create(investReferrerRewardModelArgumentCaptor.capture());
        assertEquals(49, investReferrerRewardModelArgumentCaptor.getValue().getAmount());

    }

    private LoanModel fakeLoanModel(UserModel userModel){
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(userModel.getLoginName());
        loanModel.setBaseRate(16.00);
        long id = idGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(3);
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestFeeRate(15);
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(100000L);
        loanModel.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000L);
        loanModel.setMinInvestAmount(1);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setLoanerLoginName(userModel.getLoginName());
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        return loanModel;
    }

    private InvestModel getFakeInvestModel(LoanModel loanModel,UserModel userModel) {
        InvestModel model = new InvestModel();
        model.setAmount(1000L);
        Date currentDate = new Date((new Date().getTime() / 1000) * 1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(userModel.getLoginName());
        model.setLoanId(loanModel.getId());
        model.setSource(Source.WEB);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }

    private UserModel createFakeUser(String loginName) {
        UserModel model = new UserModel();
        model.setLoginName(loginName);
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("12900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return model;
    }




}