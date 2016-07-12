package com.tuotiansudai.paywrapper.service;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.RandomStringUtils;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@Transactional
public class PointTaskServiceTest {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;


    @Test
    public void shouldCompletedEachSumInvestTask() {
        String loginName = "investor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._30);
        this.createFakeInvest(loginName, fakeLoan.getId(), 100000000);

        pointTaskService.completeAdvancedTask(PointTask.EACH_SUM_INVEST, loginName);

        long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.EACH_SUM_INVEST);
        assertThat(maxTaskLevel, is(6L));

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        assertThat(accountModel.getPoint(), is(168000L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(6));
        assertThat(pointBillModels.get(5).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(5).getPoint(), is(1000L));

        assertThat(pointBillModels.get(4).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(4).getPoint(), is(2000L));

        assertThat(pointBillModels.get(3).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(3).getPoint(), is(5000L));

        assertThat(pointBillModels.get(2).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(2).getPoint(), is(10000L));

        assertThat(pointBillModels.get(1).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(1).getPoint(), is(50000L));

        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(100000L));
    }

    @Test
    public void shouldCompletedEachSingleInvestTask() {
        String loginName = "investor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._30);
        this.createFakeInvest(loginName, fakeLoan.getId(), 50000000L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_SINGLE_INVEST, loginName);

        this.createFakeInvest(loginName, fakeLoan.getId(), 50000000L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_SINGLE_INVEST, loginName);

        long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_SINGLE_INVEST);
        assertThat(maxTaskLevel, is(5L));

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        assertThat(accountModel.getPoint(), is(87000L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(5));
        assertThat(pointBillModels.get(4).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(4).getPoint(), is(2000L));

        assertThat(pointBillModels.get(3).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(3).getPoint(), is(5000L));

        assertThat(pointBillModels.get(2).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(2).getPoint(), is(10000L));

        assertThat(pointBillModels.get(1).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(1).getPoint(), is(20000L));

        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(50000L));
    }

    @Test
    public void shouldCompletedEachRecommendTask() throws Exception {
        UserModel referrer = this.createFakeUser("referrer", null);
        UserModel newbie1 = this.createFakeUser("newbie1", referrer.getLoginName());

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND, newbie1.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.EACH_RECOMMEND), is(1L));
        assertThat(accountMapper.findByLoginName(referrer.getLoginName()).getPoint(), is(200L));

        UserModel newbie2 = this.createFakeUser("newbie2", referrer.getLoginName());

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND, newbie2.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.EACH_RECOMMEND), is(2L));
        assertThat(accountMapper.findByLoginName(referrer.getLoginName()).getPoint(), is(400L));
    }

    @Test
    public void shouldCompletedFirstReferrerInvestTask() throws Exception {
        LoanModel fakeLoan = this.createFakeLoan(ProductType._30);
        UserModel referrer = this.createFakeUser("referrer", null);
        UserModel newbie1 = this.createFakeUser("newbie1", referrer.getLoginName());
        this.createFakeInvest(newbie1.getLoginName(), fakeLoan.getId(), 1L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_REFERRER_INVEST, newbie1.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.FIRST_REFERRER_INVEST), is(1L));
        assertThat(accountMapper.findByLoginName(referrer.getLoginName()).getPoint(), is(5000L));

        UserModel newbie2 = this.createFakeUser("newbie2", referrer.getLoginName());
        this.createFakeInvest(newbie2.getLoginName(), fakeLoan.getId(), 2L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_REFERRER_INVEST, newbie2.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.FIRST_REFERRER_INVEST), is(1L));
        assertThat(accountMapper.findByLoginName(referrer.getLoginName()).getPoint(), is(5000L));
    }

    @Test
    public void shouldCompletedEachReferrerInvestTask() throws Exception {
        LoanModel fakeLoan = this.createFakeLoan(ProductType._30);
        UserModel referrer = this.createFakeUser("referrer", null);
        UserModel newbie1 = this.createFakeUser("newbie1", referrer.getLoginName());
        this.createFakeInvest(newbie1.getLoginName(), fakeLoan.getId(), 100000L);

        pointTaskService.completeAdvancedTask(PointTask.EACH_REFERRER_INVEST, newbie1.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.EACH_REFERRER_INVEST), is(1L));
        assertThat(accountMapper.findByLoginName(referrer.getLoginName()).getPoint(), is(1000L));

        UserModel newbie2 = this.createFakeUser("newbie2", referrer.getLoginName());
        this.createFakeInvest(newbie2.getLoginName(), fakeLoan.getId(), 200000L);

        pointTaskService.completeAdvancedTask(PointTask.EACH_REFERRER_INVEST, newbie2.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.EACH_REFERRER_INVEST), is(2L));
        assertThat(accountMapper.findByLoginName(referrer.getLoginName()).getPoint(), is(2000L));

        UserModel newbie3 = this.createFakeUser("newbie3", referrer.getLoginName());
        this.createFakeInvest(newbie3.getLoginName(), fakeLoan.getId(), 99999L);

        pointTaskService.completeAdvancedTask(PointTask.EACH_REFERRER_INVEST, newbie3.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.EACH_REFERRER_INVEST), is(2L));
        assertThat(accountMapper.findByLoginName(referrer.getLoginName()).getPoint(), is(2000L));
    }

    @Test
    public void shouldCompletedFirstInvest180InvestTask() {
        String loginName = "investor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._180);
        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_180, loginName);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_180), is(1L));
        assertThat(accountMapper.findByLoginName(loginName).getPoint(), is(1000L));

        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_180), is(1L));
        assertThat(accountMapper.findByLoginName(loginName).getPoint(), is(1000L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(1));
        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(1000L));
    }

    @Test
    public void shouldCompletedFirstInvest3600InvestTask() {
        String loginName = "investor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._360);
        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_360, loginName);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_360), is(1L));
        assertThat(accountMapper.findByLoginName(loginName).getPoint(), is(1000L));

        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_360), is(1L));
        assertThat(accountMapper.findByLoginName(loginName).getPoint(), is(1000L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(1));
        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(1000L));
    }


    private InvestModel createFakeInvest(String loginName, long loanId, long amount) {
        InvestModel investModel = new InvestModel();
        investModel.setId(idGenerator.generate());
        investModel.setLoanId(loanId);
        investModel.setLoginName(loginName);
        investModel.setAmount(amount);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.TRANSFERABLE);
        investModel.setSource(Source.WEB);
        investMapper.create(investModel);
        return investModel;
    }

    private UserModel createFakeUser(String loginName, String referrer) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("123abc");
        fakeUser.setEmail("12345@abc.com");
        fakeUser.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUser.setRegisterTime(new Date());
        if (!Strings.isNullOrEmpty(referrer)) {
            fakeUser.setReferrer(referrer);
        }
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        AccountModel accountModel = new AccountModel(fakeUser.getLoginName(), fakeUser.getLoginName(), "120101198810012010", "", "", new Date());
        accountMapper.create(accountModel);
        return fakeUser;
    }

    private LoanModel createFakeLoan(ProductType productType) {
        UserModel loaner = this.createFakeUser("loaner", null);
        LoanDto loanDto = new LoanDto();
        loanDto.setId(idGenerator.generate());
        loanDto.setLoanerLoginName("loaner");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("id");
        loanDto.setAgentLoginName("loaner");
        loanDto.setBasicRate("16.00");
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(1);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("1000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        loanDto.setProductType(productType);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setAgentLoginName(loaner.getLoginName());
        loanMapper.create(loanModel);
        return loanModel;
    }

}
