package com.tuotiansudai.web.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ContractServiceTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private ContractService contractService;
    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private TransferRuleMapper transferRuleMapper;

    @Test
    public void shouldGenerateTransferContractIsOk() throws ParseException {
        UserModel userModel = getUserModel();
        userMapper.create(userModel);
        LoanModel loanModel = getLoanModel();
        loanMapper.create(loanModel);
        LoanerDetailsModel loanerDetailsModel = getLoanerDetailsModel(loanModel);
        loanerDetailsMapper.create(loanerDetailsModel);
        InvestModel investModel = getInvest(loanModel.getId());
        investMapper.create(investModel);
        TransferApplicationModel transferApplicationModel = getTransferApplicationModel(loanModel.getId(), investModel.getId());
        transferApplicationMapper.create(transferApplicationModel);
        AccountModel accountModel = getAccountModel();
        accountMapper.create(accountModel);
        InvestRepayModel startInvestRepayModel = new InvestRepayModel(1L, investModel.getId(), 1, 233L, 2000L, 2L,
                DateTime.parse("2011-1-1").toDate(), RepayStatus.REPAYING);
        InvestRepayModel endInvestRepayModel = new InvestRepayModel(2L, investModel.getId(), 3, 233L, 2000L, 2L,
                DateTime.parse("2011-3-1").toDate(), RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(startInvestRepayModel, endInvestRepayModel));

        String pdfStr = contractService.generateTransferContract(transferApplicationModel.getId());
        assertNotNull(pdfStr);
    }

    private LoanModel getLoanModel() throws ParseException {
        LoanModel lm = new LoanModel();
        lm.setId(idGenerator.generate());
        lm.setName("12标的");
        lm.setLoanerUserName("testUserModel");
        lm.setAgentLoginName("testUserModel");
        lm.setLoanerLoginName("testUserModel");
        lm.setLoanerLoginName("testUserModel");
        lm.setLoanerIdentityNumber("22012219881003356X");
        lm.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        lm.setPeriods(3);
        lm.setDescriptionText("123");
        lm.setDescriptionHtml("<p>123</p>");
        lm.setLoanAmount(1300);
        lm.setMinInvestAmount(100);
        lm.setMaxInvestAmount(1300);
        lm.setInvestIncreasingAmount(1);
        lm.setActivityType(ActivityType.NORMAL);
        lm.setProductType(ProductType._180);
        lm.setBaseRate(0.12);
        lm.setActivityRate(0);
        lm.setContractId(789098123);
        lm.setFundraisingStartTime(new Date());
        lm.setFundraisingEndTime(new Date());
        lm.setVerifyTime(new Date());
        lm.setVerifyLoginName("testUserModel");
        lm.setStatus(LoanStatus.RECHECK);
        lm.setShowOnHome(false);
        lm.setCreatedTime(new Date());
        lm.setCreatedLoginName("testUserModel");
        lm.setPledgeType(PledgeType.HOUSE);
        lm.setUpdateTime(new Date());
        return lm;
    }

    private LoanerDetailsModel getLoanerDetailsModel(LoanModel loanModel) {
        return new LoanerDetailsModel(loanModel.getId(), loanModel.getLoanerLoginName(),
                loanModel.getLoanerUserName(), Gender.FEMALE, 12, loanModel.getLoanerIdentityNumber(), Marriage.MARRIED,
                "", "", "");
    }

    private InvestModel getInvest(long loanId) throws ParseException {
        InvestModel investModel = new InvestModel(idGenerator.generate(), loanId, null, 2577, "testUserModel", new Date(), Source.ANDROID, null, 0.1);
        investModel.setCreatedTime(new Date());
        return investModel;
    }

    private UserModel getUserModel() throws ParseException {
        UserModel um = new UserModel();
        um.setId(idGenerator.generate());
        um.setLoginName("testUserModel");
        um.setPassword("1234567");
        um.setMobile("1823123123");
        um.setRegisterTime(new Date());
        um.setLastModifiedTime(new Date());
        um.setLastModifiedUser("testUserModel");
        um.setStatus(UserStatus.ACTIVE);
        um.setSalt("12313");
        um.setChannel("123");
        um.setProvince("北京");
        um.setCity("北京");
        um.setSource(Source.WEB);
        return um;
    }

    public TransferApplicationModel getTransferApplicationModel(long loanId,long investId) throws ParseException {
        TransferApplicationModel al = new TransferApplicationModel();
        al.setId(idGenerator.generate());
        al.setName("测试");
        al.setLoanId(loanId);
        al.setTransferInvestId(investId);
        al.setInvestId(investId);
        al.setPeriod(2);
        al.setLeftPeriod(1);
        al.setLoginName("testUserModel");
        al.setInvestAmount(100);
        al.setTransferAmount(100);
        al.setTransferFee(1);
        al.setStatus(TransferStatus.SUCCESS);
        al.setDeadline(new Date());
        al.setTransferTime(new Date());
        al.setApplicationTime(new Date());
        return al;
    }

    public AccountModel getAccountModel(){
        AccountModel accountModel = new AccountModel("testUserModel", "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        return accountModel;
    }

    public TransferRuleModel getTransferRuleModel(){
        TransferRuleModel tr = new TransferRuleModel();
        tr.setId(idGenerator.generate());
        tr.setLevelOneFee(0.01);
        tr.setLevelOneLower(1);
        tr.setLevelOneUpper(29);
        tr.setLevelTwoFee(0.005);
        tr.setLevelTwoLower(30);
        tr.setLevelTwoUpper(90);
        tr.setLevelThreeFee(0);
        tr.setLevelThreeLower(91);
        tr.setLevelThreeUpper(365);
        tr.setDiscount(0.005);
        tr.setDaysLimit(5);
        return tr;
    }
}
