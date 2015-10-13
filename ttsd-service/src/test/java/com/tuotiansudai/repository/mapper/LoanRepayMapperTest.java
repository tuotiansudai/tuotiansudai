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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanRepayMapperTest {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateLoanRepayModel() throws Exception {
        UserModel userModel = this.getFakeUserModel();
        LoanModel fakeLoanModel = this.getFakeLoanModel(LoanStatus.REPAYING);
        userMapper.create(userModel);
        loanMapper.create(fakeLoanModel);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(idGenerator.generate());
        loanRepayModel.setDefaultInterest(0);
        loanRepayModel.setActualInterest(0);
        loanRepayModel.setPeriod(1);
        loanRepayModel.setStatus(RepayStatus.REPAYING);
        loanRepayModel.setLoanId(fakeLoanModel.getId());
        loanRepayModel.setRepayDate(new Date());
        loanRepayModel.setCorpus(0);
        loanRepayModel.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel);
        loanRepayMapper.create(loanRepayModels);

        List<LoanRepayModel> actualLoanRepayModels = loanRepayMapper.findByLoanId(fakeLoanModel.getId());

        assertTrue(CollectionUtils.isNotEmpty(actualLoanRepayModels));
    }

    @Test
    public void shouldUpdate() throws Exception {
        UserModel userModel = this.getFakeUserModel();
        LoanModel fakeLoanModel = this.getFakeLoanModel(LoanStatus.REPAYING);
        userMapper.create(userModel);
        loanMapper.create(fakeLoanModel);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(idGenerator.generate());
        loanRepayModel.setDefaultInterest(0);
        loanRepayModel.setActualInterest(0);
        loanRepayModel.setPeriod(1);
        loanRepayModel.setStatus(RepayStatus.REPAYING);
        loanRepayModel.setLoanId(fakeLoanModel.getId());
        loanRepayModel.setRepayDate(new Date());
        loanRepayModel.setCorpus(0);
        loanRepayModel.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel);
        loanRepayMapper.create(loanRepayModels);

        loanRepayModel.setCorpus(1000);
        loanRepayModel.setActualInterest(1);
        loanRepayModel.setStatus(RepayStatus.COMPLETE);
        loanRepayModel.setActualRepayDate(new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate());
        loanRepayMapper.update(loanRepayModel);


        LoanRepayModel actualLoanRepayModel = loanRepayMapper.findById(loanRepayModel.getId());

        assertThat(actualLoanRepayModel.getCorpus(), is(loanRepayModel.getCorpus()));
        assertThat(actualLoanRepayModel.getActualInterest(), is(loanRepayModel.getActualInterest()));
        assertThat(actualLoanRepayModel.getActualRepayDate(), is(loanRepayModel.getActualRepayDate()));
        assertThat(actualLoanRepayModel.getStatus(), is(loanRepayModel.getStatus()));
    }

    @Test
    public void shouldFindEnabledRepayingLoanRepayWhenFirstPeriodIsEnabled() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoanModel = this.getFakeLoanModel(LoanStatus.REPAYING);
        loanMapper.create(fakeLoanModel);
        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeLoanModel, 1, RepayStatus.REPAYING, new DateTime().toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeLoanModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(1).toDate(), null, 0, 0, 0, 0);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        LoanRepayModel enabledRepay = loanRepayMapper.findEnabledRepayByLoanId(fakeLoanModel.getId());

        assertThat(enabledRepay.getId(), is(fakeLoanRepayModel1.getId()));
    }

    @Test
    public void shouldNotFindEnabledRepayingLoanRepayWhenCurrentPeriodIsCompleted() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoanModel = this.getFakeLoanModel(LoanStatus.REPAYING);
        loanMapper.create(fakeLoanModel);
        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeLoanModel, 1, RepayStatus.COMPLETE, new DateTime().minusDays(1).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeLoanModel, 2, RepayStatus.COMPLETE, new DateTime().plusDays(1).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeLoanModel, 3, RepayStatus.REPAYING, new DateTime().plusDays(2).toDate(), null, 0, 0, 0, 0);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3));

        LoanRepayModel enabledRepay = loanRepayMapper.findEnabledRepayByLoanId(fakeLoanModel.getId());

        assertNull(enabledRepay);
    }

    @Test
    public void shouldFindEnabledOverdueLoanRepayWhenLoanHasMoreThanOneOverdueRepay() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoanModel = this.getFakeLoanModel(LoanStatus.OVERDUE);
        loanMapper.create(fakeLoanModel);
        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeLoanModel, 1, RepayStatus.COMPLETE, new DateTime().minusDays(3).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeLoanModel, 2, RepayStatus.OVERDUE, new DateTime().minusDays(2).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeLoanModel, 3, RepayStatus.OVERDUE, new DateTime().minusDays(1).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel4 = this.getFakeLoanRepayModel(fakeLoanModel, 4, RepayStatus.REPAYING, new DateTime().plusDays(1).toDate(), null, 0, 0, 0, 0);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3, fakeLoanRepayModel4));

        LoanRepayModel enabledRepay = loanRepayMapper.findEnabledRepayByLoanId(fakeLoanModel.getId());

        assertThat(enabledRepay.getId(), is(fakeLoanRepayModel2.getId()));
    }

    @Test
    public void shouldNotFindEnabledRepayingLoanRepayWhenLoanIsCompleted() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoanModel = this.getFakeLoanModel(LoanStatus.COMPLETE);
        loanMapper.create(fakeLoanModel);
        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeLoanModel, 1, RepayStatus.COMPLETE, new DateTime().minusDays(3).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeLoanModel, 2, RepayStatus.COMPLETE, new DateTime().minusDays(2).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel3 = this.getFakeLoanRepayModel(fakeLoanModel, 3, RepayStatus.COMPLETE, new DateTime().minusDays(1).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel4 = this.getFakeLoanRepayModel(fakeLoanModel, 4, RepayStatus.COMPLETE, new DateTime().toDate(), null, 0, 0, 0, 0);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2, fakeLoanRepayModel3, fakeLoanRepayModel4));

        LoanRepayModel enabledRepay = loanRepayMapper.findEnabledRepayByLoanId(fakeLoanModel.getId());

        assertNull(enabledRepay);
    }

    @Test
    public void shouldFindEnabledRepayingLoanRepayWhenFirstPeriodIsCompleted() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoanModel = this.getFakeLoanModel(LoanStatus.REPAYING);
        loanMapper.create(fakeLoanModel);
        LoanRepayModel fakeLoanRepayModel1 = this.getFakeLoanRepayModel(fakeLoanModel, 1, RepayStatus.COMPLETE, new DateTime().minusDays(1).toDate(), null, 0, 0, 0, 0);
        LoanRepayModel fakeLoanRepayModel2 = this.getFakeLoanRepayModel(fakeLoanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(1).toDate(), null, 0, 0, 0, 0);
        loanRepayMapper.create(Lists.newArrayList(fakeLoanRepayModel1, fakeLoanRepayModel2));

        LoanRepayModel enabledRepay = loanRepayMapper.findEnabledRepayByLoanId(fakeLoanModel.getId());

        assertThat(enabledRepay.getId(), is(fakeLoanRepayModel2.getId()));
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

    private LoanModel getFakeLoanModel(LoanStatus loanStatus) {
        UserModel fakeUserModel = getFakeUserModel();
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("name");
        fakeLoanModel.setAgentLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setLoanerLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setType(LoanType.LOAN_TYPE_1);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setStatus(loanStatus);
        return fakeLoanModel;
    }

    private LoanRepayModel getFakeLoanRepayModel(LoanModel fakeLoanModel,
                                                 int period,
                                                 RepayStatus repayStatus,
                                                 Date repayDate,
                                                 Date actualRepayDate,
                                                 long corpus,
                                                 long expectedInterest,
                                                 long actualInterest,
                                                 long defaultInterest
    ) {
        LoanRepayModel fakeLoanRepayModel = new LoanRepayModel();
        fakeLoanRepayModel.setId(idGenerator.generate());
        fakeLoanRepayModel.setPeriod(period);
        fakeLoanRepayModel.setStatus(repayStatus);
        fakeLoanRepayModel.setLoanId(fakeLoanModel.getId());
        fakeLoanRepayModel.setRepayDate(repayDate);
        fakeLoanRepayModel.setActualRepayDate(actualRepayDate);
        fakeLoanRepayModel.setCorpus(corpus);
        fakeLoanRepayModel.setExpectedInterest(expectedInterest);
        fakeLoanRepayModel.setActualInterest(actualInterest);
        fakeLoanRepayModel.setDefaultInterest(defaultInterest);
        return fakeLoanRepayModel;
    }
}
