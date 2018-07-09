package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.FinanceReportDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class FinanceReportServiceTest {
    @Autowired
    FinanceReportService financeReportService;

    @Autowired
    FakeUserHelper userMapper;

    @Autowired
    BankAccountMapper bankAccountMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    InvestMapper investMapper;

    @Autowired
    InvestRepayMapper investRepayMapper;

    @Autowired
    InvestReferrerRewardMapper investReferrerRewardMapper;

    private UserModel createUserModel(String loginName, String referrer) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("123abc");
        userModel.setEmail("12345@abc.com");
        userModel.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModel.setUserName("userName");
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userModel.setReferrer(referrer);
        userMapper.create(userModel);
        return userModel;
    }

    private BankAccountModel createAccountModel(UserModel userModel) {
        BankAccountModel accountModel = new BankAccountModel(userModel.getLoginName(), String.valueOf(RandomStringUtils.randomNumeric(32)),
                String.valueOf(RandomStringUtils.randomNumeric(14)), "111", "111");
        bankAccountMapper.createInvestor(accountModel);
        return accountModel;
    }

    private LoanModel createLoanModel(long loanId, String loanName, UserModel agent, BankAccountModel loaner, LoanType type, LoanStatus loanStatus) {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(loanId);
        loanModel.setName(loanName);
        loanModel.setType(type);
        loanModel.setAgentLoginName(agent.getLoginName());
        loanModel.setLoanerUserName(agent.getUserName());
        loanModel.setBaseRate(12.5);
        loanModel.setActivityRate(3.2);
        loanModel.setDuration(90);
        loanModel.setLoanAmount(1000000L);
        loanModel.setVerifyTime(DateTime.parse("2015-06-12T01:20").toDate());
        loanModel.setRecheckTime(DateTime.parse("2016-06-12T01:20").toDate());

        loanModel.setLoanerLoginName(loaner.getLoginName());
        loanModel.setLoanerIdentityNumber(loaner.getLoginName());
        loanModel.setPeriods(6);
        loanModel.setDescriptionText("text");
        loanModel.setDescriptionHtml("html");
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setStatus(loanStatus);
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private InvestModel createInvestModel(long investId, long loanId, BankAccountModel investor, Date investTime) {
        InvestModel investModel = new InvestModel(investId, loanId, null, investor.getLoginName(), 1000L, 0.01, false, investTime, Source.WEB, "channel");
        investMapper.create(investModel);
        return investModel;
    }

    private InvestRepayModel createInvestRepayModel(long investId, int period, RepayStatus repayStatus) {
        InvestRepayModel investRepayModel = new InvestRepayModel(IdGenerator.generate(), investId, period, 900L, 9999L, 99L, DateTime.parse("2016-12-12T01:20").toDate(), repayStatus);
        if (RepayStatus.COMPLETE == repayStatus) {
            investRepayModel.setActualFee(2000L);
            investRepayModel.setActualInterest(8000L);
            investRepayModel.setRepayAmount(6000L);
        } else {
            investRepayModel.setActualFee(0L);
            investRepayModel.setActualInterest(0L);
            investRepayModel.setRepayAmount(0L);
        }

        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        return investRepayModel;
    }

    private InvestReferrerRewardModel createInvestReferrerRewardModel(long investId, String referrerLoginName, Role referrerRole) {
        InvestReferrerRewardModel investReferrerRewardModel = new InvestReferrerRewardModel(IdGenerator.generate(),
                investId, 3000L, referrerLoginName, referrerRole);
        investReferrerRewardMapper.create(investReferrerRewardModel);
        return investReferrerRewardModel;
    }

    private FinanceReportItemView combineFinanceReportModel(LoanModel loanModel, InvestModel investModel, UserModel userModel, InvestRepayModel investRepayModel) {
        FinanceReportItemView financeReportItemView = new FinanceReportItemView();
        financeReportItemView.setLoanId(loanModel.getId());
        financeReportItemView.setLoanName(loanModel.getName());
        financeReportItemView.setLoanType(loanModel.getType());
        financeReportItemView.setLoanerUserName(loanModel.getLoanerUserName());
        financeReportItemView.setAgentLoginName(loanModel.getAgentLoginName());
        financeReportItemView.setBaseRate(loanModel.getBaseRate());
        financeReportItemView.setActivityRate(loanModel.getActivityRate());
        financeReportItemView.setDuration(loanModel.getDuration());
        financeReportItemView.setLoanAmount(loanModel.getLoanAmount());
        financeReportItemView.setVerifyTime(loanModel.getVerifyTime());
        financeReportItemView.setRecheckTime(loanModel.getRecheckTime());
        financeReportItemView.setInvestTime(investModel.getInvestTime());
        financeReportItemView.setInvestLoginName(investModel.getLoginName());
        financeReportItemView.setInvestRealName(userModel.getUserName());
        financeReportItemView.setReferrer("referrer");
        financeReportItemView.setInvestAmount(investModel.getAmount());
        financeReportItemView.setRepayTime(investRepayModel.getRepayDate());
        financeReportItemView.setPeriod(investRepayModel.getPeriod());
        financeReportItemView.setActualInterest(investRepayModel.getActualInterest());
        financeReportItemView.setFee(investRepayModel.getActualFee());
        financeReportItemView.setActualRepayAmount(investRepayModel.getRepayAmount());

        return financeReportItemView;
    }

    private List<FinanceReportItemView> prepareData() {
        UserModel referrerUserModel = createUserModel("referrer", null);
        BankAccountModel referrerAccountModel = createAccountModel(referrerUserModel);

        UserModel loanerUserModel = createUserModel("loaner", null);
        BankAccountModel loanerAccountModel = createAccountModel(loanerUserModel);

        UserModel agentUserModel = createUserModel("agent", null);
        BankAccountModel agentAccountModel = createAccountModel(agentUserModel);

        UserModel investorWithReferrerUserModel = createUserModel("investorWithReferrer", "referrer");
        BankAccountModel investorWithReferrerAccountModel = createAccountModel(investorWithReferrerUserModel);

        UserModel investorWithoutReferrerUserModel = createUserModel("investorWithoutReferrer", null);
        BankAccountModel investorWithoutReferrerAccountModel = createAccountModel(investorWithoutReferrerUserModel);

        List<FinanceReportItemView> financeReportItemViews = new ArrayList<>();

        LoanModel loanModel = createLoanModel(1000, "房产", agentUserModel, loanerAccountModel, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, LoanStatus.COMPLETE);
        InvestModel investModel = createInvestModel(1, loanModel.getId(), investorWithReferrerAccountModel, DateTime.parse("2016-10-12T01:20").toDate());
        InvestRepayModel investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        InvestReferrerRewardModel investReferrerRewardModel = createInvestReferrerRewardModel(1, "referrer", Role.SD_STAFF);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 2, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 3, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investModel = createInvestModel(2, loanModel.getId(), investorWithoutReferrerAccountModel, DateTime.parse("2016-11-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));

        loanModel = createLoanModel(2, "车辆", agentUserModel, loanerAccountModel, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY, LoanStatus.COMPLETE);
        investModel = createInvestModel(3, loanModel.getId(), investorWithReferrerAccountModel, DateTime.parse("2016-10-12T01:20").toDate());
        investReferrerRewardModel = createInvestReferrerRewardModel(3, "referrer", Role.USER);
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 2, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investRepayModel = createInvestRepayModel(investModel.getId(), 3, RepayStatus.REPAYING);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));
        investModel = createInvestModel(4, loanModel.getId(), investorWithoutReferrerAccountModel, DateTime.parse("2016-11-12T01:20").toDate());
        investRepayModel = createInvestRepayModel(investModel.getId(), 1, RepayStatus.COMPLETE);
        financeReportItemViews.add(combineFinanceReportModel(loanModel, investModel, investorWithReferrerUserModel, investRepayModel));

        return financeReportItemViews;
    }

    @Test
    public void testFinanceReportModel() throws Exception {
        List<FinanceReportItemView> originalFinanceReportItemViewList = prepareData();

        BasePaginationDataDto<FinanceReportDto> basePaginationDataDto = financeReportService.getFinanceReportDtos(null, null, null, null, null, null, 2, 3);
        assertEquals(true, basePaginationDataDto.isHasNextPage());
        assertEquals(true, basePaginationDataDto.isHasPreviousPage());
        assertEquals(14, basePaginationDataDto.getCount());
        assertEquals(2, basePaginationDataDto.getIndex());
        assertEquals(3, basePaginationDataDto.getRecords().size());

        basePaginationDataDto = financeReportService.getFinanceReportDtos(1000L, 1, "investorWithReferrer", null, null, null, 1, 100);
        assertEquals(1, basePaginationDataDto.getRecords().size());
        FinanceReportItemView financeReportItemView = originalFinanceReportItemViewList.get(0);
        FinanceReportDto financeReportDto = basePaginationDataDto.getRecords().get(0);
        assertEquals(financeReportItemView.getLoanId(), financeReportDto.getLoanId());
        assertEquals(financeReportItemView.getLoanName(), financeReportDto.getLoanName());
        assertEquals(financeReportItemView.getLoanType().getInterestType(), financeReportDto.getLoanType());
        assertEquals(financeReportItemView.getLoanerUserName(), financeReportDto.getLoanerUserName());
        assertEquals(financeReportItemView.getAgentLoginName(), financeReportDto.getAgentLoginName());
        assertEquals(financeReportItemView.getBaseRate() + "%+" + financeReportItemView.getActivityRate() + "%", financeReportDto.getBenefitRate());
        assertEquals(financeReportItemView.getDuration(), financeReportDto.getDuration());
        assertEquals(AmountConverter.convertCentToString(financeReportItemView.getLoanAmount()), financeReportDto.getLoanAmount());
        assertEquals(financeReportItemView.getVerifyTime(), financeReportDto.getVerifyTime());
        assertEquals(financeReportItemView.getInvestTime(), financeReportDto.getInvestTime());
        assertEquals(financeReportItemView.getRecheckTime(), financeReportDto.getRecheckTime());
        assertEquals(financeReportItemView.getInvestLoginName(), financeReportDto.getInvestLoginName());
        assertEquals(financeReportItemView.getInvestRealName(), financeReportDto.getInvestRealName());
        assertEquals(financeReportItemView.getReferrer() + "(业务员)", financeReportDto.getReferrer());
        assertEquals(AmountConverter.convertCentToString(financeReportItemView.getInvestAmount()), financeReportDto.getInvestAmount());
        assertEquals(62, financeReportDto.getBenefitDays());
        assertEquals(financeReportItemView.getRepayTime(), financeReportDto.getRepayTime());
        assertEquals(financeReportItemView.getPeriod(), financeReportDto.getPeriod());
        assertEquals(AmountConverter.convertCentToString(financeReportItemView.getActualInterest()), financeReportDto.getActualInterest());
        assertEquals(AmountConverter.convertCentToString(financeReportItemView.getFee()), financeReportDto.getFee());
        assertEquals(AmountConverter.convertCentToString(financeReportItemView.getActualRepayAmount()), financeReportDto.getActualRepayAmount());
        assertEquals("30.00", financeReportDto.getRecommendAmount());
        basePaginationDataDto = financeReportService.getFinanceReportDtos(2L, 1, "investorWithReferrer", null, null, null, 1, 100);
        assertEquals(1, basePaginationDataDto.getRecords().size());
        financeReportDto = basePaginationDataDto.getRecords().get(0);
        assertEquals(184, financeReportDto.getBenefitDays());
        basePaginationDataDto = financeReportService.getFinanceReportDtos(1000L, 2, "investorWithReferrer", null, null, null, 1, 100);
        assertEquals(1, basePaginationDataDto.getRecords().size());
        financeReportDto = basePaginationDataDto.getRecords().get(0);
        assertEquals(0, financeReportDto.getBenefitDays());
        basePaginationDataDto = financeReportService.getFinanceReportDtos(2L, 1, "investorWithReferrer", null, null, null, 1, 100);
        assertEquals(1, basePaginationDataDto.getRecords().size());
        financeReportDto = basePaginationDataDto.getRecords().get(0);
        assertEquals("referrer", financeReportDto.getReferrer());
    }

    @Test
    public void testGetFinanceReportCsvData() throws Exception {
        prepareData();

        List<List<String>> csvData = financeReportService.getFinanceReportCsvData(null, null, null, null, null, null);
        assertEquals(14, csvData.size());
    }
}
