package com.tuotiansudai.paywrapper.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.paywrapper.loanout.LoanOutInvestCalculationService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class LoanOutInvestCalculationServiceTest {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;

    @Autowired
    private LoanOutInvestCalculationService loanOutInvestCalculationService;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;


    @Test
    @Transactional
    public void loanOutInvestWithLoanTypeIsLoanInterestLumpSumRepayCalculation() {
        UserModel userModel = createFakeUser("buildbox123123131", "13612341234");
        LoanModel loanModel = fakeLoanModel(userModel, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY);
        loanMapper.create(loanModel);
        LoanDetailsModel loanDetailsModel = fakeLoanLoanDetailsModel(loanModel);
        loanDetailsMapper.create(loanDetailsModel);

        List<ExtraLoanRateModel> extraLoanRateModels = fakeExtraLoanRate(loanModel);
        extraLoanRateMapper.create(extraLoanRateModels);

        UserModel test1Model = createFakeUser("test0001", "13333333333");
        InvestModel test1InvestModel = new InvestModel(IdGenerator.generate(), loanModel.getId(), null, 25000, test1Model.getLoginName(),
                new Date(), Source.WEB, "tuotiansudai", 0.1);
        test1InvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(test1InvestModel);

        UserModel test2Model = createFakeUser("test0002", "18999999999");
        InvestModel test2InvestModel = new InvestModel(IdGenerator.generate(), loanModel.getId(), null, 31000, test2Model.getLoginName(),
                new Date(), Source.WEB, "tuotiansudai", 0.1);
        test2InvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(test2InvestModel);

        loanOutInvestCalculationService.rateIncreases(loanModel.getId());

        InvestExtraRateModel test1investExtraRateModel = investExtraRateMapper.findByInvestId(test1InvestModel.getId());

        assertThat(new DateTime(loanModel.getDeadline()).toString("yyyy-MM-dd"),
                is(new DateTime(test1investExtraRateModel.getRepayDate()).toString("yyyy-MM-dd")));
        assertThat(test1investExtraRateModel.getExpectedInterest(), is(24l));
        assertThat(test1investExtraRateModel.getExpectedFee(), is(2l));

        InvestExtraRateModel test2investExtraRateModel = investExtraRateMapper.findByInvestId(test2InvestModel.getId());
        assertThat(new DateTime(loanModel.getDeadline()).toString("yyyy-MM-dd"),
                is(new DateTime(test2investExtraRateModel.getRepayDate()).toString("yyyy-MM-dd")));
        assertThat(test2investExtraRateModel.getExpectedInterest(), is(61l));
        assertThat(test2investExtraRateModel.getExpectedFee(), is(6l));

        loanOutInvestCalculationService.rateIncreases(loanModel.getId());
    }


    @Test
    @Transactional
    public void loanOutInvestWithLoanTypeIsInvestInterestLumpSumRepayCalculation() {
        UserModel userModel = createFakeUser("buildbox123123131", "13612341234");
        LoanModel loanModel = fakeLoanModel(userModel, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY);
        loanMapper.create(loanModel);
        LoanDetailsModel loanDetailsModel = fakeLoanLoanDetailsModel(loanModel);
        loanDetailsMapper.create(loanDetailsModel);

        List<ExtraLoanRateModel> extraLoanRateModels = fakeExtraLoanRate(loanModel);
        extraLoanRateMapper.create(extraLoanRateModels);

        UserModel test1Model = createFakeUser("test0001", "13333333333");
        InvestModel test1InvestModel = new InvestModel(IdGenerator.generate(), loanModel.getId(), null, 25000, test1Model.getLoginName(),
                new Date(), Source.WEB, "tuotiansudai", 0.1);
        test1InvestModel.setTradingTime(new Date());
        test1InvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(test1InvestModel);

        UserModel test2Model = createFakeUser("test0002", "18999999999");
        InvestModel test2InvestModel = new InvestModel(IdGenerator.generate(), loanModel.getId(), null, 31000, test2Model.getLoginName(),
                new Date(), Source.WEB, "tuotiansudai", 0.1);
        test2InvestModel.setTradingTime(new Date());
        test2InvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(test2InvestModel);

        loanOutInvestCalculationService.rateIncreases(loanModel.getId());

        InvestExtraRateModel test1investExtraRateModel = investExtraRateMapper.findByInvestId(test1InvestModel.getId());

        assertThat(new DateTime(loanModel.getDeadline()).toString("yyyy-MM-dd"),
                is(new DateTime(test1investExtraRateModel.getRepayDate()).toString("yyyy-MM-dd")));
        assertThat(test1investExtraRateModel.getExpectedInterest(), is(24l));
        assertThat(test1investExtraRateModel.getExpectedFee(), is(2l));

        InvestExtraRateModel test2investExtraRateModel = investExtraRateMapper.findByInvestId(test2InvestModel.getId());
        assertThat(new DateTime(loanModel.getDeadline()).toString("yyyy-MM-dd"),
                is(new DateTime(test2investExtraRateModel.getRepayDate()).toString("yyyy-MM-dd")));
        assertThat(test2investExtraRateModel.getExpectedInterest(), is(61l));
        assertThat(test2investExtraRateModel.getExpectedFee(), is(6l));

        loanOutInvestCalculationService.rateIncreases(loanModel.getId());
    }

    private UserModel createFakeUser(String loginName, String mobile) {
        UserModel model = new UserModel();
        model.setLoginName(loginName);
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile(mobile);
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        MembershipModel membershipModel = membershipMapper.findByLevel(0);
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, membershipModel.getId(), new DateTime().plusDays(1).toDate(), UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);
        return model;
    }

    private LoanModel fakeLoanModel(UserModel userModel, LoanType loanType) {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(userModel.getLoginName());
        loanModel.setBaseRate(16.00);
        long id = IdGenerator.generate();
        loanModel.setId(id);
        loanModel.setProductType(ProductType._90);
        loanModel.setName("房产抵押借款");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(3);
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(100000L);
        loanModel.setType(loanType);
        loanModel.setMaxInvestAmount(100000000000L);
        loanModel.setMinInvestAmount(1);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setLoanerLoginName(userModel.getLoginName());
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setRecheckTime(new Date());
        loanModel.setDuration(90);
        loanModel.setDeadline(new DateTime().plusDays(89).toDate());
        loanModel.setPledgeType(PledgeType.HOUSE);
        return loanModel;
    }

    private List<ExtraLoanRateModel> fakeExtraLoanRate(LoanModel loanModel) {
        List<ExtraLoanRateRuleModel> extraLoanRateRuleModels = extraLoanRateRuleMapper.findExtraLoanRateRuleByNameAndProductType(loanModel.getName(), loanModel.getProductType());
        List<ExtraLoanRateModel> extraLoanRateModels = new ArrayList<>();
        ExtraLoanRateModel level1 = new ExtraLoanRateModel();
        level1.setLoanId(loanModel.getId());
        level1.setExtraRateRuleId(extraLoanRateRuleModels.get(0).getId());
        level1.setRate(extraLoanRateRuleModels.get(0).getRate());
        level1.setMinInvestAmount(10000);
        level1.setMaxInvestAmount(20000);
        extraLoanRateModels.add(level1);

        ExtraLoanRateModel level2 = new ExtraLoanRateModel();
        level2.setLoanId(loanModel.getId());
        level2.setRate(extraLoanRateRuleModels.get(1).getRate());
        level2.setExtraRateRuleId(extraLoanRateRuleModels.get(1).getId());
        level2.setMinInvestAmount(20000);
        level2.setMaxInvestAmount(30000);
        extraLoanRateModels.add(level2);

        ExtraLoanRateModel level3 = new ExtraLoanRateModel();
        level3.setLoanId(loanModel.getId());
        level3.setRate(extraLoanRateRuleModels.get(2).getRate());
        level3.setExtraRateRuleId(extraLoanRateRuleModels.get(2).getId());
        level3.setMinInvestAmount(30000);
        level3.setMaxInvestAmount(0);
        extraLoanRateModels.add(level3);
        return extraLoanRateModels;
    }

    private LoanDetailsModel fakeLoanLoanDetailsModel(LoanModel loanModel) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        long id = IdGenerator.generate();
        loanDetailsModel.setId(id);
        loanDetailsModel.setLoanId(loanModel.getId());
        loanDetailsModel.setDeclaration("材料声明");
        loanDetailsModel.setExtraSource(Lists.newArrayList(Source.WEB, Source.MOBILE));
        return loanDetailsModel;
    }
}
