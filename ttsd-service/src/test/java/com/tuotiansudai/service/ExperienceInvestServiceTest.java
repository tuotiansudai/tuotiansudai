package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        UserModel investor = this.getFakeUser("newbieInvestor");
        LoanModel fakeExperienceLoan = this.getFakeExperienceLoan();

        MockitoAnnotations.initMocks(this);
        experienceInvestService.invest(this.getFakeInvestDto(investor, fakeExperienceLoan));

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

    private InvestDto getFakeInvestDto(UserModel investor, LoanModel experienceLoanModel) {
        InvestDto dto = new InvestDto();
        dto.setAmount("10000");
        dto.setLoginName(investor.getLoginName());
        dto.setSource(Source.WEB);
        dto.setLoanId(String.valueOf(experienceLoanModel.getId()));
        return dto;
    }

    private LoanModel getFakeExperienceLoan() {
        UserModel loaner = this.getFakeUser("experienceLoaner");
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

    protected UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setMobile(loginName);
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        fakeUser.setExperienceBalance(20000);
        userMapper.create(fakeUser);
        return fakeUser;
    }
}
