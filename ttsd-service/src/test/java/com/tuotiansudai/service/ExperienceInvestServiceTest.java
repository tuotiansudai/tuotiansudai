package com.tuotiansudai.service;

import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExperienceInvestServiceTest {
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ExperienceInvestService experienceInvestService;

    @Test
    public void shouldInvestExperienceLoan() throws Exception {
        UserModel investor = this.getFakeUser("newbieInvestor", 20000);
        LoanModel fakeExperienceLoan = this.getFakeExperienceLoan(20000);

        MockitoAnnotations.initMocks(this);
        experienceInvestService.invest(this.getFakeInvestDto(investor, fakeExperienceLoan, "10000"));

        List<InvestModel> successInvestModels = investMapper.findByLoanIdAndLoginName(fakeExperienceLoan.getId(), investor.getLoginName());
        assertThat(successInvestModels.size(), is(1));
        assertThat(successInvestModels.get(0).getTransferStatus(), is(TransferStatus.NONTRANSFERABLE));
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(successInvestModels.get(0).getId());
        assertThat(investRepayModels.size(), is(1));
        assertThat(investRepayModels.get(0).getExpectedInterest(), is(10L));
        assertThat(investRepayModels.get(0).getExpectedFee(), is(0L));
        assertThat(investRepayModels.get(0).getRepayDate().getTime(), is(new DateTime().withTimeAtStartOfDay().plusDays(3).minusSeconds(1).getMillis()));
        UserModel userModel = userMapper.findByLoginName(investor.getLoginName());
        assertThat(userModel.getExperienceBalance(), is(10000L));
    }

    @Test
    public void shouldInvestExperienceLoanWhenExperienceBalanceNoSufficient() throws Exception {
        UserModel investor = this.getFakeUser("newExperienceInvestor", 1000);
        LoanModel fakeExperienceLoan = this.getFakeExperienceLoan(1000);
        MockitoAnnotations.initMocks(this);
        experienceInvestService.invest(this.getFakeInvestDto(investor, fakeExperienceLoan, "2000"));

        List<InvestModel> successInvestModels = investMapper.findByLoanIdAndLoginName(fakeExperienceLoan.getId(), investor.getLoginName());
        assertThat(successInvestModels.size(), is(0));
        UserModel userModel = userMapper.findByLoginName(investor.getLoginName());
        assertThat(userModel.getExperienceBalance(), is(1000L));    }

    private InvestDto getFakeInvestDto(UserModel investor, LoanModel experienceLoanModel, String investAmount) {
        InvestDto dto = new InvestDto();
        dto.setAmount(investAmount);
        dto.setLoginName(investor.getLoginName());
        dto.setSource(Source.WEB);
        dto.setLoanId(String.valueOf(experienceLoanModel.getId()));
        return dto;
    }

    private LoanModel getFakeExperienceLoan(long experienceBalance) {
        UserModel loaner = this.getFakeUser("experienceLoaner", experienceBalance);
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanAmount(10000L);
        fakeLoanModel.setLoanerLoginName(loaner.getLoginName());
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("id");
        fakeLoanModel.setAgentLoginName(loaner.getLoginName());
        fakeLoanModel.setType(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NEWBIE);
        fakeLoanModel.setProductType(ProductType.EXPERIENCE);
        fakeLoanModel.setBaseRate(0.13);
        fakeLoanModel.setActivityRate(0);
        fakeLoanModel.setDuration(3);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(new Date());
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);

        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    protected UserModel getFakeUser(String loginName, long experienceBalance) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setMobile(loginName);
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        fakeUser.setExperienceBalance(experienceBalance);
        userMapper.create(fakeUser);
        return fakeUser;
    }
}
