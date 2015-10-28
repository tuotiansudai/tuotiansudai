package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestRepayMapperTest {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateInvestRepayModel() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investModel.getId());
        investRepayModel.setPeriod(1);
        investRepayModel.setStatus(RepayStatus.REPAYING);
        investRepayModel.setRepayDate(new Date());
        investRepayModels.add(investRepayModel);
        investRepayMapper.create(investRepayModels);

        List<InvestRepayModel> actualInvestRepayModels = investRepayMapper.findByInvestId(investModel.getId());

        assertTrue(CollectionUtils.isNotEmpty(actualInvestRepayModels));
    }

    @Test
    public void shouldFindInvestRepayModelByInvestIdAndPeriod() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);

        InvestRepayModel investRepayModel1 = new InvestRepayModel();
        investRepayModel1.setId(idGenerator.generate());
        investRepayModel1.setInvestId(investModel.getId());
        investRepayModel1.setPeriod(1);
        investRepayModel1.setStatus(RepayStatus.REPAYING);
        investRepayModel1.setRepayDate(new Date());

        InvestRepayModel investRepayModel2 = new InvestRepayModel();
        investRepayModel2.setId(idGenerator.generate());
        investRepayModel2.setInvestId(investModel.getId());
        investRepayModel2.setPeriod(2);
        investRepayModel2.setStatus(RepayStatus.REPAYING);
        investRepayModel2.setRepayDate(new Date());

        List<InvestRepayModel> investRepayModels = Lists.newArrayList(investRepayModel1, investRepayModel2);
        investRepayMapper.create(investRepayModels);

        InvestRepayModel secondInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), 2);

        assertThat(secondInvestRepayModel.getId(), is(investRepayModel2.getId()));
    }

    @Test
    public void shouldUpdate() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        Date actualRepayDate = new Date();
        investMapper.create(investModel);
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investModel.getId());
        investRepayModel.setPeriod(1);
        investRepayModel.setStatus(RepayStatus.REPAYING);
        investRepayModel.setRepayDate(actualRepayDate);
        List<InvestRepayModel> investRepayModels = Lists.newArrayList(investRepayModel);
        investRepayMapper.create(investRepayModels);

        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayModel.setActualInterest(100L);
        investRepayModel.setActualRepayDate(actualRepayDate);
        investRepayMapper.update(investRepayModel);
        InvestRepayModel updateInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), investRepayModel.getPeriod());

        assertThat(updateInvestRepayModel.getStatus(), is(investRepayModel.getStatus()));
        assertThat(updateInvestRepayModel.getActualInterest(), is(investRepayModel.getActualInterest()));
        assertThat(new DateTime(updateInvestRepayModel.getActualRepayDate()).withTimeAtStartOfDay().getMillis(),
                is(new DateTime(investRepayModel.getActualRepayDate().getTime()).withTimeAtStartOfDay().getMillis()));
    }

    private UserModel getFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("loginName");
        fakeUserModel.setPassword("password");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUserModel;
    }

    private LoanModel getFakeLoanModel() {
        UserModel fakeUserModel = getFakeUserModel();
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("name");
        fakeLoanModel.setAgentLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setLoanerLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        return fakeLoanModel;
    }
    
    private InvestModel getFakeInvestModel() {
        UserModel fakeUserModel = this.getFakeUserModel();
        LoanModel fakeLoanModel = this.getFakeLoanModel();
        userMapper.create(fakeUserModel);
        loanMapper.create(fakeLoanModel);
        InvestModel fakeInvestModel = new InvestModel();
        fakeInvestModel.setId(idGenerator.generate());
        fakeInvestModel.setLoginName(fakeUserModel.getLoginName());
        fakeInvestModel.setLoanId(fakeLoanModel.getId());
        fakeInvestModel.setSource(Source.WEB);
        fakeInvestModel.setStatus(InvestStatus.SUCCESS);
        return fakeInvestModel;
    }
    

}
