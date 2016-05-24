package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldCreateLoan() {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoan = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.PREHEAT,ActivityType.NEWBIE);
        loanMapper.create(fakeLoan);

        assertNotNull(loanMapper.findById(fakeLoan.getId()));
    }

    @Test
    public void shouldUpdateLoan() {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoan = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.PREHEAT,ActivityType.NEWBIE);
        loanMapper.create(fakeLoan);

        fakeLoan.setStatus(LoanStatus.COMPLETE);
        loanMapper.update(fakeLoan);

        assertThat(loanMapper.findById(fakeLoan.getId()).getStatus(), is(LoanStatus.COMPLETE));
    }

    @Test
    public void shouldFindRepayingPaginationByLoanerLoginName() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        LoanModel fakeRepayingLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.REPAYING,ActivityType.NEWBIE);
        LoanModel fakeRepayingLoan2 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.REPAYING,ActivityType.NEWBIE);
        LoanModel fakeRepayingLoan3 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.REPAYING,ActivityType.NEWBIE);
        LoanModel fakeCompletedLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.COMPLETE,ActivityType.NEWBIE);

        loanMapper.create(fakeRepayingLoan1);
        loanMapper.create(fakeRepayingLoan2);
        loanMapper.create(fakeRepayingLoan3);
        loanMapper.create(fakeCompletedLoan1);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), new DateTime().withTimeAtStartOfDay().withDate(2000, 1, 1).toDate(), 0, 1, 1, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), null, 1, 1, 0, 0);

        LoanRepayModel repayingLoan2Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 1, RepayStatus.REPAYING, new DateTime().withDate(2000, 4, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan2Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 5, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan2Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 6, 1).withTimeAtStartOfDay().toDate(), null, 1, 1, 0, 0);

        LoanRepayModel repayingLoan3Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan3, 1, RepayStatus.REPAYING, new DateTime().withDate(2000, 7, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 1, 0);
        LoanRepayModel repayingLoan3Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan3, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 8, 1).withTimeAtStartOfDay().toDate(), null, 0, 1, 1, 0);
        LoanRepayModel repayingLoan3Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan3, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 9, 1).withTimeAtStartOfDay().toDate(), null, 1, 1, 1, 0);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2, repayingLoan1Repay3,
                repayingLoan2Repay1, repayingLoan2Repay2, repayingLoan2Repay3,
                repayingLoan3Repay1, repayingLoan3Repay2, repayingLoan3Repay3);

        loanRepayMapper.create(loanRepayModels);

        List<LoanModel> loanModels = loanMapper.findRepayingPaginationByAgentLoginName(fakeUserModel.getLoginName(), 0, 2,
                new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(),
                new DateTime().withDate(2000, 12, 31).withTimeAtStartOfDay().toDate());

        long count = loanMapper.findCountRepayingByAgentLoginName(fakeUserModel.getLoginName(),
                new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(),
                new DateTime().withDate(2000, 12, 31).withTimeAtStartOfDay().toDate());

        assertThat(count, is(3L));

        assertThat(loanModels.size(), is(2));
        assertThat(loanModels.get(0).getId(), is(fakeRepayingLoan1.getId()));
        assertThat(loanModels.get(0).getNextRepayDate().getTime(), is(repayingLoan1Repay2.getRepayDate().getTime()));
        assertThat(loanModels.get(0).getUnpaidAmount(), is(3L));

        assertThat(loanModels.get(1).getId(), is(fakeRepayingLoan2.getId()));
        assertThat(loanModels.get(1).getNextRepayDate().getTime(), is(repayingLoan2Repay1.getRepayDate().getTime()));
        assertThat(loanModels.get(1).getUnpaidAmount(), is(4L));
    }

    @Test
    public void shouldFindCompletedPaginationByLoanerLoginName() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        LoanModel fakeCompletedLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.COMPLETE,ActivityType.NEWBIE);
        LoanModel fakeCompletedLoan2 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.COMPLETE,ActivityType.NEWBIE);
        LoanModel fakeCompletedLoan3 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.COMPLETE,ActivityType.NEWBIE);
        LoanModel fakeCanceledLoan = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.CANCEL,ActivityType.NEWBIE);

        loanMapper.create(fakeCompletedLoan1);
        loanMapper.create(fakeCompletedLoan2);
        loanMapper.create(fakeCompletedLoan3);
        loanMapper.create(fakeCanceledLoan);

        LoanRepayModel completedLoan1Repay1 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 2).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan1Repay2 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 2, 2).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan1Repay3 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 3, 2).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), 1, 2, 1, 0);

        LoanRepayModel completedLoan2Repay1 = this.getFakeLoanRepayModel(fakeCompletedLoan2, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 4, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 4, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan2Repay2 = this.getFakeLoanRepayModel(fakeCompletedLoan2, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 5, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 5, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan2Repay3 = this.getFakeLoanRepayModel(fakeCompletedLoan2, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 6, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 6, 1).withTimeAtStartOfDay().toDate(), 1, 2, 1, 0);

        LoanRepayModel completedLoan3Repay1 = this.getFakeLoanRepayModel(fakeCompletedLoan3, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 7, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 7, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan3Repay2 = this.getFakeLoanRepayModel(fakeCompletedLoan3, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 8, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 8, 1).withTimeAtStartOfDay().toDate(), 0, 2, 1, 0);
        LoanRepayModel completedLoan3Repay3 = this.getFakeLoanRepayModel(fakeCompletedLoan3, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 9, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 9, 1).withTimeAtStartOfDay().toDate(), 1, 2, 1, 0);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(completedLoan1Repay1, completedLoan1Repay2, completedLoan1Repay3,
                completedLoan2Repay1, completedLoan2Repay2, completedLoan2Repay3,
                completedLoan3Repay1, completedLoan3Repay2, completedLoan3Repay3);

        loanRepayMapper.create(loanRepayModels);

        List<LoanModel> loanModels = loanMapper.findCompletedPaginationByAgentLoginName(fakeUserModel.getLoginName(), 0, 2,
                new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(),
                new DateTime().withDate(2000, 12, 31).withTimeAtStartOfDay().toDate());

        long count = loanMapper.findCountCompletedByAgentLoginName(fakeUserModel.getLoginName(),
                new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(),
                new DateTime().withDate(2000, 12, 31).withTimeAtStartOfDay().toDate());

        assertThat(count, is(3L));

        assertThat(loanModels.size(), is(2));

        assertThat(loanModels.get(0).getId(), is(fakeCompletedLoan3.getId()));
        assertThat(loanModels.get(0).getCompletedDate().getTime(), is(completedLoan3Repay3.getRepayDate().getTime()));
        assertThat(loanModels.get(0).getExpectedRepayAmount(), is(7L));
        assertThat(loanModels.get(0).getActualRepayAmount(), is(4L));


        assertThat(loanModels.get(1).getId(), is(fakeCompletedLoan2.getId()));
        assertThat(loanModels.get(1).getCompletedDate().getTime(), is(completedLoan2Repay3.getRepayDate().getTime()));
        assertThat(loanModels.get(1).getExpectedRepayAmount(), is(7L));
        assertThat(loanModels.get(1).getActualRepayAmount(), is(4L));
    }

    @Test
    public void shouldFindCanceledPaginationByLoanerLoginName() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        LoanModel fakeCanceledLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.CANCEL,ActivityType.NEWBIE);
        LoanModel fakeCanceledLoan2 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.CANCEL,ActivityType.NEWBIE);
        LoanModel fakeCanceledLoan3 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.CANCEL,ActivityType.NEWBIE);
        LoanModel fakeCompletedLoan = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.COMPLETE,ActivityType.NEWBIE);

        loanMapper.create(fakeCanceledLoan1);
        fakeCanceledLoan1.setRecheckTime(new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate());
        loanMapper.update(fakeCanceledLoan1);

        loanMapper.create(fakeCanceledLoan2);
        fakeCanceledLoan2.setRecheckTime(new DateTime().withDate(2000, 1, 2).withTimeAtStartOfDay().toDate());
        loanMapper.update(fakeCanceledLoan2);

        loanMapper.create(fakeCanceledLoan3);
        fakeCanceledLoan3.setRecheckTime(new DateTime().withDate(2000, 1, 3).withTimeAtStartOfDay().toDate());
        loanMapper.update(fakeCanceledLoan3);

        loanMapper.create(fakeCompletedLoan);

        LoanRepayModel completedLoanRepay1 = this.getFakeLoanRepayModel(fakeCompletedLoan, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(), 0, 1, 1, 0);
        LoanRepayModel completedLoanRepay2 = this.getFakeLoanRepayModel(fakeCompletedLoan, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 2, 1).withTimeAtStartOfDay().toDate(), 0, 1, 1, 0);
        LoanRepayModel completedLoanRepay3 = this.getFakeLoanRepayModel(fakeCompletedLoan, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), new DateTime().withDate(2000, 3, 1).withTimeAtStartOfDay().toDate(), 1, 1, 1, 0);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(completedLoanRepay1, completedLoanRepay2, completedLoanRepay3);

        loanRepayMapper.create(loanRepayModels);

        List<LoanModel> loanModels = loanMapper.findCanceledPaginationByAgentLoginName(fakeUserModel.getLoginName(), 0, 2,
                new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(),
                new DateTime().withDate(2000, 12, 31).withTimeAtStartOfDay().toDate());

        long count = loanMapper.findCountCanceledByAgentLoginName(fakeUserModel.getLoginName(),
                new DateTime().withDate(2000, 1, 1).withTimeAtStartOfDay().toDate(),
                new DateTime().withDate(2000, 12, 31).withTimeAtStartOfDay().toDate());

        assertThat(count, is(3L));

        assertThat(loanModels.size(), is(2));

        assertThat(loanModels.get(0).getId(), is(fakeCanceledLoan3.getId()));
        assertThat(loanModels.get(0).getCanceledDate().getTime(), is(fakeCanceledLoan3.getRecheckTime().getTime()));

        assertThat(loanModels.get(1).getId(), is(fakeCanceledLoan2.getId()));
        assertThat(loanModels.get(1).getCanceledDate().getTime(), is(fakeCanceledLoan2.getRecheckTime().getTime()));
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus,ActivityType activityType) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
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

    private UserModel getFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("loginName");
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setPassword("password");
        fakeUserModel.setSalt("salt");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        return fakeUserModel;
    }

    @Test
    public void findLoanListTest() {
        List<LoanModel> loanModels = loanMapper.findLoanList(LoanStatus.RAISING, 1L, "", new Date(), new Date(), 0, 10);
        int loanListCount = loanMapper.findLoanListCount(LoanStatus.RAISING, 1L, "", new Date(), new Date());
        assertThat(loanModels.size(), is(loanListCount));
    }

    @Test
    public void updateRaisingCompleteTimeTest() {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        LoanModel fakeCanceledLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.CANCEL,ActivityType.NEWBIE);
        loanMapper.create(fakeCanceledLoan1);
        loanMapper.updateRaisingCompleteTime(fakeCanceledLoan1.getId(), new Date());

        LoanModel loan = loanMapper.findById(fakeCanceledLoan1.getId());
        assertThat(loan.getRaisingCompleteTime(), isA(Date.class));
    }

    @Test
    public void shouldFindHomeLoanByIsContainNewBieIsOk(){
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        LoanModel fakeCanceledLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.CANCEL,ActivityType.NEWBIE);
        LoanModel fakeCanceledLoan2 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.CANCEL,ActivityType.NORMAL);
        loanMapper.create(fakeCanceledLoan1);
        loanMapper.create(fakeCanceledLoan2);

        assertNotNull(loanMapper.findHomeLoanByIsContainNewBie("false",LoanStatus.RAISING));
        assertNotNull(loanMapper.findHomeLoanByIsContainNewBie("true",LoanStatus.RAISING));
    }
}
