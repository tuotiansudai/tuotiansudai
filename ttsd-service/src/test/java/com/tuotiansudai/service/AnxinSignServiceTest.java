package com.tuotiansudai.service;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.response.tx3.Tx3202ResVO;
import com.google.common.collect.Lists;
import com.tuotiansudai.anxin.service.impl.AnxinSignServiceImpl;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AnxinSignServiceTest {

    @InjectMocks
    private AnxinSignServiceImpl anxinSignService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private LoanerDetailsMapper loanerDetailsMapper;

    @Mock
    private AnxinSignConnectService anxinSignConnectService;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Mock
    private TransferRuleMapper transferRuleMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGenerateContractIsOK() throws PKIException {
        UserModel agent = getFakeUserModel("agentLoginName");
        UserModel investor = getFakeUserModel("investorLoginName");
        LoanModel loanModel = getFakeLoan(agent.getLoginName(), agent.getLoginName());
        List investList = Lists.newArrayList(getFakeInvestModel(loanModel.getId(), investor.getLoginName()));
        AnxinSignPropertyModel agentAnxin = getAnxinSignPropertyModel(agent.getLoginName());

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investList);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(anxinSignPropertyMapper.findByLoginName(anyString())).thenReturn(agentAnxin);
        when(userMapper.findByLoginName(anyString())).thenReturn(agent);
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(getInvestRepayModel());
        when(loanerDetailsMapper.getByLoanId(anyLong())).thenReturn(getLoanerDetailsModel());
        when(anxinSignConnectService.generateContractBatch3202(anyLong(), anyString(), any(AnxinContractType.class), anyList())).thenReturn(new Tx3202ResVO());

        BaseDto baseDto = anxinSignService.createLoanContracts(loanModel.getId());
        assertTrue(baseDto.isSuccess());
    }

    @Test
    public void shouldGenerateContractNoAnxinSignIsfault() throws PKIException {
        UserModel agent = getFakeUserModel("agentLoginName");
        UserModel investor = getFakeUserModel("investorLoginName");
        LoanModel loanModel = getFakeLoan(agent.getLoginName(), agent.getLoginName());
        List investList = Lists.newArrayList(getFakeInvestModel(loanModel.getId(), investor.getLoginName()));

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investList);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(anxinSignPropertyMapper.findByLoginName(anyString())).thenReturn(null);
        when(userMapper.findByLoginName(anyString())).thenReturn(agent);
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(getInvestRepayModel());
        when(loanerDetailsMapper.getByLoanId(anyLong())).thenReturn(getLoanerDetailsModel());
        when(anxinSignConnectService.generateContractBatch3202(anyLong(), anyString(), any(AnxinContractType.class), anyList())).thenReturn(new Tx3202ResVO());

        BaseDto baseDto = anxinSignService.createLoanContracts(loanModel.getId());
        assertTrue(!baseDto.isSuccess());
    }

    @Test
    public void shouldGenerateContractErrorIsFault() throws PKIException {
        UserModel agent = getFakeUserModel("agentLoginName");
        UserModel investor = getFakeUserModel("investorLoginName");
        LoanModel loanModel = getFakeLoan(agent.getLoginName(), agent.getLoginName());
        List investList = Lists.newArrayList(getFakeInvestModel(loanModel.getId(), investor.getLoginName()));
        AnxinSignPropertyModel agentAnxin = getAnxinSignPropertyModel(agent.getLoginName());

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investList);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(anxinSignPropertyMapper.findByLoginName(anyString())).thenReturn(agentAnxin);
        when(userMapper.findByLoginName(anyString())).thenReturn(agent);
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(getInvestRepayModel());
        when(loanerDetailsMapper.getByLoanId(anyLong())).thenReturn(getLoanerDetailsModel());
        when(anxinSignConnectService.generateContractBatch3202(anyLong(), anyString(), any(AnxinContractType.class), anyList())).thenReturn(null);

        BaseDto baseDto = anxinSignService.createLoanContracts(loanModel.getId());
        assertTrue(!baseDto.isSuccess());
    }

    @Test
    public void shouldGenerateTransferContractIsOk(){
        UserModel agent = getFakeUserModel("agentTransferLoginName");
        UserModel investor = getFakeUserModel("investorLoginName");
        TransferApplicationModel transferApplicationModel = getTransferApplication(agent.getLoginName());
        AnxinSignPropertyModel agentAnxin = getAnxinSignPropertyModel(agent.getLoginName());
        LoanModel loanModel = getFakeLoan(agent.getLoginName(), agent.getLoginName());
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), investor.getLoginName());
        InvestRepayModel investRepayModel = getInvestRepayModel();

        when(transferApplicationMapper.findById(anyLong())).thenReturn(transferApplicationModel);
        when(userMapper.findByLoginName(anyString())).thenReturn(agent);
        when(anxinSignPropertyMapper.findByLoginName(anyString())).thenReturn(agentAnxin);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(loanerDetailsMapper.getByLoanId(anyLong())).thenReturn(getLoanerDetailsModel());
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(investRepayModel);
//        when(transferRuleMapper.find()).thenReturn();

    }

    private TransferRuleModel getTransferRuleModel(){
        TransferRuleModel transferRuleModel = new TransferRuleModel();
//        transferRuleModel.setLevelThreeFee();
        return transferRuleModel;
    }

    private TransferApplicationModel getTransferApplication(String loginName){
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(loginName);
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.TRANSFERRING);
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        return transferApplicationModel;
    }


    private LoanerDetailsModel getLoanerDetailsModel(){
        return new LoanerDetailsModel(9999L, "testLoaner", "loaner", Gender.MALE, 12,
                "123814134", Marriage.DIVORCE, "region", "income", "employment");

    }

    private InvestRepayModel getInvestRepayModel(){
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(1);
        investRepayModel.setPeriod(1);
        investRepayModel.setStatus(RepayStatus.REPAYING);
        investRepayModel.setRepayDate(new Date());
        return investRepayModel;
    }

    private AnxinSignPropertyModel getAnxinSignPropertyModel(String loginName) {
        AnxinSignPropertyModel anxinSignPropertyModel = new AnxinSignPropertyModel();
        anxinSignPropertyModel.setCreatedTime(DateTime.now().toDate());
        anxinSignPropertyModel.setAnxinUserId(String.valueOf(1));
        anxinSignPropertyModel.setAuthIp("123455");
        anxinSignPropertyModel.setAuthTime(DateTime.now().toDate());
        anxinSignPropertyModel.setLoginName(loginName);
        anxinSignPropertyModel.setProjectCode("123");
        anxinSignPropertyModel.setSkipAuth(false);
        return anxinSignPropertyModel;
    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        InvestModel model = new InvestModel(1l, loanId, null, 1000000L, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        fakeLoanModel.setCreatedTime(new Date());
        return fakeLoanModel;
    }

    private UserModel getFakeUserModel(String loginName) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(loginName);
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setPassword("password");
        fakeUserModel.setSalt("salt");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        return fakeUserModel;
    }

}
