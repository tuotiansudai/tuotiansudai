package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.loanout.impl.ReferrerRewardServiceImpl;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})@Transactional
public class MockReferrerRewardServiceTest {

    @InjectMocks
    private ReferrerRewardServiceImpl referrerRewardService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private ReferrerRelationMapper referrerRelationMapper;

    @Mock
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private InvestMapper investMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotRewardReferrerWhenReferrerIsZCStaff() throws Exception {
        LoanModel loanModel = new LoanModel();
        loanModel.setType(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);

        List<InvestModel> successInvestList = Lists.newArrayList();
        successInvestList.add(fakeInvestModel());
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(successInvestList);

        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setLevel(1);
        referrerRelationModel.setReferrerLoginName("referrer");
        referrerRelationModel.setLoginName("investor");
        List<ReferrerRelationModel> referrerRelationModels = Lists.newArrayList(referrerRelationModel);
        when(referrerRelationMapper.findByLoginName("investor")).thenReturn(referrerRelationModels);
        when(investReferrerRewardMapper.findByInvestIdAndReferrer(anyLong(), anyString())).thenReturn(null);
        when(userRoleMapper.findByLoginName("referrer")).thenReturn(Lists.newArrayList(new UserRoleModel("referrer", Role.ZC_STAFF)));
        ReflectionTestUtils.setField(referrerRewardService, "referrerStaffRoleReward", Lists.newArrayList(0.01, 0.01, 0.01, 0.01));
        ReflectionTestUtils.setField(referrerRewardService, "referrerUserRoleReward", Lists.newArrayList(0.01, 0.01));

        referrerRewardService.rewardReferrer(1);

        verify(paySyncClient, never()).send(eq(ProjectTransferMapper.class), any(TransferRequestModel.class), eq(ProjectTransferResponseModel.class));
        verify(investReferrerRewardMapper, times(1)).create(any(InvestReferrerRewardModel.class));
    }

    @Test
    public void shouldNotRewardReferrerWhenReferrerIsStaffRecommend() throws Exception {
        LoanModel loanModel = new LoanModel();
        loanModel.setType(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);

        List<InvestModel> successInvestList = Lists.newArrayList();
        successInvestList.add(fakeInvestModel());
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(successInvestList);

        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setLevel(1);
        referrerRelationModel.setReferrerLoginName("referrer");
        referrerRelationModel.setLoginName("investor");
        List<ReferrerRelationModel> referrerRelationModels = Lists.newArrayList(referrerRelationModel);
        when(referrerRelationMapper.findByLoginName("investor")).thenReturn(referrerRelationModels);
        when(investReferrerRewardMapper.findByInvestIdAndReferrer(anyLong(), anyString())).thenReturn(null);
        when(userRoleMapper.findByLoginName("referrer")).thenReturn(Lists.newArrayList(new UserRoleModel("referrer", Role.ZC_STAFF_RECOMMEND)));

        referrerRewardService.rewardReferrer(1);

        verify(paySyncClient, never()).send(eq(ProjectTransferMapper.class), any(TransferRequestModel.class), eq(ProjectTransferResponseModel.class));
        verify(investReferrerRewardMapper, times(1)).create(any(InvestReferrerRewardModel.class));
    }

    private InvestModel fakeInvestModel(){
        InvestModel investModel = new InvestModel(IdGenerator.generate(), 10000, null, 1000L, "investor", new Date(), Source.WEB, null, 0.1);
        investModel.setTradingTime(new Date());
        return investModel;
    }
}