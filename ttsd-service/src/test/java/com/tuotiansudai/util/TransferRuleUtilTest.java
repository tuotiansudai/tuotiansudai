package com.tuotiansudai.util;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class TransferRuleUtilTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Test
    public void shouldGetTransferFeeInvestInterestLevelOne() {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, new DateTime().minusDays(29).toDate());
        InvestModel investModel = createInvest("testuser", loanId, 100000, new DateTime().minusDays(20).toDate());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        long fee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        assertThat(fee, is(new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(transferRuleModel.getLevelOneFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue()));
    }

    @Test
    public void shouldGetTransferFeeInvestInterestLevelTwo() {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, new DateTime().minusDays(90).toDate());
        InvestModel investModel = createInvest("testuser", loanId, 100000, new DateTime().minusDays(70).toDate());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        long fee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        assertThat(fee, is(new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(transferRuleModel.getLevelTwoFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue()));
    }

    @Test
    public void shouldGetTransferFeeInvestInterestLevelThree() {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, new DateTime().minusDays(300).toDate());
        InvestModel investModel = createInvest("testuser", loanId, 100000, new DateTime().minusDays(290).toDate());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        long fee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        assertThat(fee, is(new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(transferRuleModel.getLevelThreeFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue()));
    }

    @Test
    public void shouldGetTransferFeeLoanInterestLevelOne() {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY, new DateTime().minusDays(29).toDate());
        InvestModel investModel = createInvest("testuser", loanId, 100000, new DateTime().minusDays(25).toDate());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        long fee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        assertThat(fee, is(new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(transferRuleModel.getLevelOneFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue()));
    }

    @Test
    public void shouldGetTransferFeeLoanInterestLevelTwo() {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY, new DateTime().minusDays(90).toDate());
        InvestModel investModel = createInvest("testuser", loanId, 100000, new DateTime().minusDays(22).toDate());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        long fee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        assertThat(fee, is(new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(transferRuleModel.getLevelTwoFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue()));
    }

    @Test
    public void shouldGetTransferFeeLoanInterestLevelThree() {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY, new DateTime().minusDays(300).toDate());
        InvestModel investModel = createInvest("testuser", loanId, 100000, new DateTime().minusDays(30).toDate());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        long fee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        assertThat(fee, is(new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(transferRuleModel.getLevelThreeFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue()));
    }

    private InvestModel createInvest(String userId, long loanId, long amount, Date createTime) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, amount, userId, createTime, Source.WEB, null);
        model.setCreatedTime(createTime);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private LoanModel createLoanByUserId(String userId, long loanId, LoanType loanType, Date recheckTime) {
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
        loanDto.setLoanAmount("1000000");
        loanDto.setType(loanType);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setProductType(ProductType._180);
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setRecheckTime(recheckTime);
        loanMapper.create(loanModel);
        return loanModel;
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

}
