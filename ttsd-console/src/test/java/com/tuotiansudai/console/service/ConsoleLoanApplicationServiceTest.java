package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.enums.LoanApplicationStatus;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ConsoleLoanApplicationServiceTest {

    @Autowired
    FakeUserHelper userMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    LoanApplicationMapper loanApplicationMapper;

    @Autowired
    ConsoleLoanApplicationService consoleLoanApplicationService;

    static final private Random random = new Random(new Date().getTime());

    private UserModel createUserModel(String loginName) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setMobile(String.valueOf(random.nextLong()).substring(0, 10));
        userModel.setUserName("userName");
        userModel.setPassword("password");
        userModel.setSalt("salt");
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userMapper.create(userModel);
        return userModel;
    }

    private AccountModel createAccountModel(UserModel userModel) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), String.valueOf(random.nextLong()).substring(0, 10), String.valueOf(random.nextLong()).substring(0, 10), new Date());
        accountMapper.create(accountModel);
        return accountModel;
    }

    private void prepareData() {
        UserModel user1 = createUserModel("user1");
        UserModel user2 = createUserModel("user2");
        createUserModel("noAccount");
        createAccountModel(user1);
        createAccountModel(user2);
    }

    private LoanApplicationModel createLoanApplicationModel(String loginName) {
        LoanApplicationModel loanApplicationModel = new LoanApplicationModel();
        loanApplicationModel.setLoginName(loginName);
        loanApplicationModel.setMobile("18612341234");
        loanApplicationModel.setUserName("userName");
        loanApplicationModel.setAmount(1);
        loanApplicationModel.setPeriod(2);
        loanApplicationModel.setPledgeType(PledgeType.HOUSE);
        loanApplicationModel.setPledgeInfo("testInfo");
        loanApplicationModel.setComment("testComment");
        loanApplicationModel.setCreatedTime(DateTime.parse("2011-1-1").toDate());
        loanApplicationModel.setUpdatedBy("user1");
        loanApplicationModel.setUpdatedTime(DateTime.parse("2011-2-1").toDate());
        loanApplicationModel.setAddress("address");
        loanApplicationModel.setAge((short) 10);
        loanApplicationModel.setHaveCreditReport(true);
        loanApplicationModel.setHomeIncome(100);
        loanApplicationModel.setLoanUsage("loanUsage");
        loanApplicationModel.setStatus(LoanApplicationStatus.WAITING);
        loanApplicationMapper.create(loanApplicationModel);
        return loanApplicationModel;
    }

    private LoanApplicationDto fakeLoanApplicationDto(String loginName, int amount, int period) {
        LoanApplicationDto loanApplicationDto = new LoanApplicationDto();
        loanApplicationDto.setLoginName(loginName);
        loanApplicationDto.setRegion(LoanApplicationRegion.BEI_JING);
        loanApplicationDto.setAmount(amount);
        loanApplicationDto.setPeriod(period);
        loanApplicationDto.setPledgeType(PledgeType.HOUSE);
        loanApplicationDto.setPledgeInfo("testInfo");
        return loanApplicationDto;
    }

    @Test
    public void testComment() throws Exception {
        prepareData();
        LoanApplicationModel loanApplicationModel = createLoanApplicationModel("user1");
        List<LoanApplicationModel> loanApplicationViews = loanApplicationMapper.findPagination(0, 1);
        assertEquals(1, loanApplicationViews.size());
        LoanApplicationModel loanApplicationView = loanApplicationViews.get(0);

        loanApplicationView.setLoginName("user2");
        loanApplicationView.setMobile("22222");
        loanApplicationView.setUserName("userName2");
        loanApplicationView.setRegion(LoanApplicationRegion.CHENG_DE);
        loanApplicationView.setAmount(33);
        loanApplicationView.setPeriod(44);
        loanApplicationView.setPledgeType(PledgeType.VEHICLE);
        loanApplicationView.setPledgeInfo("updateInfo");
        loanApplicationView.setComment("updateComment");
        loanApplicationView.setUpdatedBy("user2");
        loanApplicationView.setCreatedTime(DateTime.parse("2019-1-1").toDate());
        loanApplicationView.setUpdatedTime(DateTime.parse("2020-1-1").toDate());
        BaseDto<BaseDataDto> baseDto = consoleLoanApplicationService.comment(loanApplicationView);
        assertEquals(true, baseDto.getData().getStatus());

        loanApplicationViews = loanApplicationMapper.findPagination(0, 1);
        assertEquals(1, loanApplicationViews.size());
        LoanApplicationModel updateLoanApplicationView = loanApplicationViews.get(0);
        assertEquals(loanApplicationModel.getId(), updateLoanApplicationView.getId());
        assertEquals(loanApplicationModel.getLoginName(), updateLoanApplicationView.getLoginName());
        assertEquals(loanApplicationModel.getRegion(), updateLoanApplicationView.getRegion());
        assertEquals(loanApplicationModel.getAmount(), updateLoanApplicationView.getAmount());
        assertEquals(loanApplicationModel.getPeriod(), updateLoanApplicationView.getPeriod());
        assertEquals(loanApplicationModel.getPledgeType(), updateLoanApplicationView.getPledgeType());
        assertEquals(loanApplicationModel.getPledgeInfo(), updateLoanApplicationView.getPledgeInfo());
        assertEquals(loanApplicationView.getComment(), updateLoanApplicationView.getComment());
        assertEquals(loanApplicationView.getUpdatedBy(), updateLoanApplicationView.getUpdatedBy());
        assertEquals(loanApplicationModel.getCreatedTime(), updateLoanApplicationView.getCreatedTime());

        loanApplicationView.setId(loanApplicationView.getId() + 100L);
        baseDto = consoleLoanApplicationService.comment(loanApplicationView);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("该借款申请不存在", baseDto.getData().getMessage());
    }

    @Test
    public void testGetPagination() throws Exception {
        prepareData();
        createLoanApplicationModel("user1");
        createLoanApplicationModel("user2");

        BasePaginationDataDto<LoanApplicationModel> basePaginationDataDto = consoleLoanApplicationService.getPagination(1, 10);
        assertEquals(2, basePaginationDataDto.getRecords().size());
    }
}
