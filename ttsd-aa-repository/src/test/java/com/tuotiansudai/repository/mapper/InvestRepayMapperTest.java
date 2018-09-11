package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class InvestRepayMapperTest {

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Test
    public void shouldCreateInvestRepayModel() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setInvestId(investModel.getId());
        investRepayModel.setPeriod(1);
        investRepayModel.setStatus(RepayStatus.REPAYING);
        investRepayModel.setRepayDate(new Date());
        investRepayModels.add(investRepayModel);
        investRepayMapper.create(investRepayModels);

        List<InvestRepayModel> actualInvestRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());

        assertTrue(CollectionUtils.isNotEmpty(actualInvestRepayModels));
    }

    @Test
    public void shouldFindInvestRepayModelByInvestIdAndPeriod() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);

        InvestRepayModel investRepayModel1 = new InvestRepayModel();
        investRepayModel1.setId(IdGenerator.generate());
        investRepayModel1.setInvestId(investModel.getId());
        investRepayModel1.setPeriod(1);
        investRepayModel1.setStatus(RepayStatus.REPAYING);
        investRepayModel1.setRepayDate(new Date());

        InvestRepayModel investRepayModel2 = new InvestRepayModel();
        investRepayModel2.setId(IdGenerator.generate());
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
    public void shouldFindCompletedInvestRepayByIdAndPeriod() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);

        InvestRepayModel investRepayModel1 = new InvestRepayModel();
        investRepayModel1.setId(IdGenerator.generate());
        investRepayModel1.setInvestId(investModel.getId());
        investRepayModel1.setPeriod(1);
        investRepayModel1.setStatus(RepayStatus.REPAYING);
        investRepayModel1.setRepayDate(new Date());

        InvestRepayModel investRepayModel2 = new InvestRepayModel();
        investRepayModel2.setId(IdGenerator.generate());
        investRepayModel2.setInvestId(investModel.getId());
        investRepayModel2.setPeriod(2);
        investRepayModel2.setStatus(RepayStatus.COMPLETE);
        investRepayModel2.setRepayDate(new Date());

        List<InvestRepayModel> investRepayModels = Lists.newArrayList(investRepayModel1, investRepayModel2);
        investRepayMapper.create(investRepayModels);

        InvestRepayModel secondInvestRepayModel = investRepayMapper.findCompletedInvestRepayByIdAndPeriod(investModel.getId(), 2);

        assertThat(secondInvestRepayModel.getId(), is(investRepayModel2.getId()));
    }

    @Test
    public void shouldUpdate() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        Date actualRepayDate = new Date();
        investMapper.create(investModel);
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(IdGenerator.generate());
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

    @Test
    public void shouldFindByLoginNameAndStatus() {
        InvestModel fakeInvestModel = getFakeInvestModel();
        investMapper.create(fakeInvestModel);

        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            InvestRepayModel investRepayModel = new InvestRepayModel();
            investRepayModel.setId(IdGenerator.generate());
            investRepayModel.setInvestId(fakeInvestModel.getId());
            investRepayModel.setPeriod(i + 1);
            investRepayModel.setRepayDate(new DateTime().withDate(2015, 1, i + 1).withTimeAtStartOfDay().toDate());
            if (i > 20) {
                investRepayModel.setStatus(RepayStatus.REPAYING);
            } else {
                investRepayModel.setStatus(RepayStatus.COMPLETE);
                investRepayModel.setActualRepayDate(investRepayModel.getRepayDate());
            }
            investRepayModels.add(investRepayModel);
        }
        investRepayMapper.create(investRepayModels);

        List<InvestRepayModel> repayModelsPaid = investRepayMapper.findByLoginNameAndStatus(fakeInvestModel.getLoginName(), "paid", 0, Integer.MAX_VALUE);
        List<InvestRepayModel> repayModelsUnPaid = investRepayMapper.findByLoginNameAndStatus(fakeInvestModel.getLoginName(), "unpaid", 0, Integer.MAX_VALUE);
        long repayModelCountPaid = investRepayMapper.findCountByLoginNameAndStatus(fakeInvestModel.getLoginName(), "paid");
        long repayModelCountUnPaid = investRepayMapper.findCountByLoginNameAndStatus(fakeInvestModel.getLoginName(), "unpaid");

        assertEquals(21, repayModelsPaid.get(0).getPeriod());
        assertEquals(22, repayModelsUnPaid.get(0).getPeriod());

        assertEquals(21, repayModelCountPaid);
        assertEquals(9, repayModelCountUnPaid);
    }

    @Test
    public void shouldFindLeftPeriodByTransferInvestIdAndPeriodIsSuccess() {
        InvestModel fakeInvestModel = getFakeInvestModel();
        investMapper.create(fakeInvestModel);
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            InvestRepayModel investRepayModel = new InvestRepayModel();
            investRepayModel.setId(IdGenerator.generate());
            investRepayModel.setInvestId(fakeInvestModel.getId());
            investRepayModel.setPeriod(i + 1);
            investRepayModel.setRepayDate(new DateTime().withDate(2015, 1, i + 1).withTimeAtStartOfDay().toDate());
            investRepayModel.setStatus(RepayStatus.REPAYING);
            investRepayModels.add(investRepayModel);
        }
        investRepayMapper.create(investRepayModels);

        int count = investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(fakeInvestModel.getId(), 2);

        assertEquals(3, count);

    }

    private UserModel getFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("loginName");
        fakeUserModel.setPassword("password");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUserModel;
    }

    private LoanModel getFakeLoanModel() {
        UserModel fakeUserModel = getFakeUserModel();
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName("name");
        fakeLoanModel.setAgentLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setLoanerLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("128347111111111111");
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        return fakeLoanModel;
    }

    private InvestModel getFakeInvestModel() {
        UserModel fakeUserModel = this.getFakeUserModel();
        LoanModel fakeLoanModel = this.getFakeLoanModel();
        userMapper.create(fakeUserModel);
        loanMapper.create(fakeLoanModel);
        InvestModel fakeInvestModel = new InvestModel(IdGenerator.generate(), fakeLoanModel.getId(), null, fakeUserModel.getLoginName(), 0L, 0.1, false, new Date(), Source.WEB, null);
        fakeInvestModel.setStatus(InvestStatus.SUCCESS);
        return fakeInvestModel;
    }

    @Test
    public void shouldFindByLoginNameAndTimeSuccessInvestRepayListIsOk() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LoanModel loanModel = getFakeLoanModel();
        loanModel.setProductType(ProductType.EXPERIENCE);
        loanModel.setStatus(LoanStatus.COMPLETE);
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        loanMapper.create(loanModel);
        InvestModel investModel = new InvestModel();
        investModel.setId(IdGenerator.generate());
        investModel.setLoginName(fakeUserModel.getLoginName());
        investModel.setLoanId(loanModel.getId());
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setInvestTime(sdf.parse("2016-05-20"));
        investModel.setTransferStatus(TransferStatus.SUCCESS);
        investModel.setSource(Source.IOS);
        Date startTime = DateUtils.addDays(new DateTime().dayOfMonth().withMinimumValue().toDate(), -1);
        Date endTime = DateUtils.addMonths(startTime, 1);
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setInvestId(investModel.getId());
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setRepayDate(new Date());
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(investRepayModel);
        investMapper.create(investModel);
        investRepayMapper.create(investRepayModels);
        List<InvestRepayModel> investRepayModelList = investRepayMapper.findByLoginNameAndTimeSuccessInvestRepayList(fakeUserModel.getLoginName(), startTime, endTime, 0, 10);
        assertNotNull(investRepayModelList);
    }

    @Test
    public void shouldFindByLoginNameAndTimeNotSuccessInvestRepayListIsOk() {
        LoanModel loanModel = getFakeLoanModel();
        loanModel.setProductType(ProductType.EXPERIENCE);
        loanModel.setStatus(LoanStatus.COMPLETE);
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);
        loanMapper.create(loanModel);
        InvestModel investModel = new InvestModel();
        investModel.setId(IdGenerator.generate());
        investModel.setLoginName(fakeUserModel.getLoginName());
        investModel.setLoanId(loanModel.getId());
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setInvestTime(new Date());
        investModel.setTransferStatus(TransferStatus.SUCCESS);
        investModel.setSource(Source.IOS);
        Date startTime = DateUtils.addDays(new DateTime().dayOfMonth().withMinimumValue().toDate(), -1);
        Date endTime = DateUtils.addMonths(startTime, 1);
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setInvestId(investModel.getId());
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setStatus(RepayStatus.REPAYING);
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setRepayDate(new Date());
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(investRepayModel);
        investMapper.create(investModel);
        investRepayMapper.create(investRepayModels);
        List<InvestRepayModel> investRepayModelList = investRepayMapper.findByLoginNameAndTimeNotSuccessInvestRepayList(fakeUserModel.getLoginName(), startTime, endTime, 0, 10);
        assertNotNull(investRepayModelList);
    }
}
