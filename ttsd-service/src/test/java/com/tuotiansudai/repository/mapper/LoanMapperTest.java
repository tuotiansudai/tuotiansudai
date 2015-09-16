package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
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
    public void createLoanTest() {
        LoanModel loanModel = createLoan();
        assertNotNull(loanMapper.findById(loanModel.getId()));
    }

    @Test
    public void updateLoanTest() {
        LoanModel loanModel = createLoan();
        loanModel.setDescriptionText("just for a test");
        loanMapper.update(loanModel);
        assertTrue(loanModel.getDescriptionText().equals(loanMapper.findById(loanModel.getId()).getDescriptionText()));
    }

    @Test
    public void findByIdTest() {
        LoanModel loanModel = createLoan();
        assertNotNull(loanMapper.findById(loanModel.getId()));
    }

    @Test
    public void shouldFindRepayingPaginationByLoanerLoginName() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        LoanModel fakeRepayingLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.REPAYING);
        LoanModel fakeRepayingLoan2 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.REPAYING);
        LoanModel fakeCompletedLoan1 = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName(), LoanStatus.COMPLETE);

        loanMapper.create(fakeRepayingLoan1);
        loanMapper.create(fakeRepayingLoan2);
        loanMapper.create(fakeCompletedLoan1);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 1).toDate(), new DateTime().withDate(2000, 1, 1).toDate(), 1, 1, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 2, 1).toDate(), null, 1, 1, 0);
        LoanRepayModel repayingLoan1Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan1, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 3, 1).toDate(), null, 1, 1, 0);

        LoanRepayModel repayingLoan2Repay1 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 1, RepayStatus.REPAYING, new DateTime().withDate(2000, 1, 2).toDate(), null, 1, 1, 0);
        LoanRepayModel repayingLoan2Repay2 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 2, RepayStatus.REPAYING, new DateTime().withDate(2000, 2, 2).toDate(), null, 1, 1, 0);
        LoanRepayModel repayingLoan2Repay3 = this.getFakeLoanRepayModel(fakeRepayingLoan2, 3, RepayStatus.REPAYING, new DateTime().withDate(2000, 3, 2).toDate(), null, 1, 1, 0);

        LoanRepayModel completedLoan3Repay1 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 1, RepayStatus.COMPLETE, new DateTime().withDate(2000, 1, 3).toDate(), new DateTime().withDate(2000, 1, 3).toDate(), 1, 1, 0);
        LoanRepayModel completedLoan3Repay2 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 2, RepayStatus.COMPLETE, new DateTime().withDate(2000, 2, 3).toDate(), new DateTime().withDate(2000, 1, 3).toDate(), 1, 1, 0);
        LoanRepayModel completedLoan3Repay3 = this.getFakeLoanRepayModel(fakeCompletedLoan1, 3, RepayStatus.COMPLETE, new DateTime().withDate(2000, 3, 3).toDate(), new DateTime().withDate(2000, 1, 3).toDate(), 1, 1, 0);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2, repayingLoan1Repay3,
                repayingLoan2Repay1, repayingLoan2Repay2, repayingLoan2Repay3,
                completedLoan3Repay1, completedLoan3Repay2, completedLoan3Repay3);

        loanRepayMapper.create(loanRepayModels);

        List<LoanModel> loanModels = loanMapper.findRepayingPaginationByLoanerLoginName(fakeUserModel.getLoginName(), 0, 1);

        assertTrue(CollectionUtils.isNotEmpty(loanModels));
    }

    private LoanModel createLoan() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setBasicRate("16.00");
        long id = idGenerator.generate();
        loanDto.setId(id);
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
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        loanDto.setLoanTitles(loanTitleRelationModelList);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.LOAN_TYPE_1);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
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
        fakeLoanRepayModel.setCorpus(0);
        fakeLoanRepayModel.setExpectedInterest(expectedInterest);
        fakeLoanRepayModel.setExpectedInterest(actualInterest);
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
}
