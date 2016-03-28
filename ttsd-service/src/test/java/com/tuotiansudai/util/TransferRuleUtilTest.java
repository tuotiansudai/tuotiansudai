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

    private InvestModel createInvest(String userId, long loanId) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1000000, userId, Source.WEB, null);
        model.setCreatedTime(new DateTime().minusDays(29).toDate());
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private LoanModel createLoanByUserId(String userId, long loanId) {
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
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setRecheckTime(new DateTime().minusDays(20).toDate());
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

    @Test
    public void shouldGetTransferFee() {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        long fee = TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel);
        assertThat(fee, is(new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(transferRuleModel.getLevelTwoFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue()));
    }

}
