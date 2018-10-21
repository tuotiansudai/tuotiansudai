package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.model.*;
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
public class LoanApplicationServiceTest {

    @Autowired
    FakeUserHelper userMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    LoanApplicationMapper loanApplicationMapper;

    @Autowired
    LoanApplicationService loanApplicationService;

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

    private LoanApplicationDto fakeLoanApplicationDto(String loginName, int amount, int period) {
        LoanApplicationDto loanApplicationDto = new LoanApplicationDto();
        loanApplicationDto.setLoginName(loginName);
        loanApplicationDto.setAmount(amount);
        loanApplicationDto.setPeriod(period);
        loanApplicationDto.setPledgeType(PledgeType.HOUSE);
        loanApplicationDto.setPledgeInfo("testInfo");
        loanApplicationDto.setAddress("address");
        loanApplicationDto.setAge((short) 10);
        loanApplicationDto.setHaveCreditReport(true);
        loanApplicationDto.setHomeIncome(100);
        loanApplicationDto.setLoanUsage("loanUsage");
        return loanApplicationDto;
    }

    @Test
    public void testCreate() throws Exception {
        prepareData();
        LoanApplicationDto loanApplicationDto = fakeLoanApplicationDto("noAccount", 1, 2);
        BaseDto<BaseDataDto> baseDto = loanApplicationService.create(loanApplicationDto);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("账户没有实名认证", baseDto.getData().getMessage());

        loanApplicationDto = fakeLoanApplicationDto("user1", 0, 1);
        baseDto = loanApplicationService.create(loanApplicationDto);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("借款金额必须是大于等于1的整数", baseDto.getData().getMessage());

        loanApplicationDto = fakeLoanApplicationDto("user1", 1, 0);
        baseDto = loanApplicationService.create(loanApplicationDto);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("借款期限必须是大于等于1的整数", baseDto.getData().getMessage());

        loanApplicationDto = fakeLoanApplicationDto("user1", 1, 2);
        baseDto = loanApplicationService.create(loanApplicationDto);
        assertEquals(true, baseDto.getData().getStatus());

        List<LoanApplicationModel> loanApplicationViews = loanApplicationMapper.findPagination(0, 1);
        assertEquals(1, loanApplicationViews.size());
        LoanApplicationModel loanApplicationView = loanApplicationViews.get(0);
        assertEquals(loanApplicationDto.getLoginName(), loanApplicationView.getLoginName());
        assertEquals(loanApplicationDto.getRegion(), loanApplicationView.getRegion());
        assertEquals(loanApplicationDto.getAmount(), loanApplicationView.getAmount());
        assertEquals(loanApplicationDto.getPeriod(), loanApplicationView.getPeriod());
        assertEquals(loanApplicationDto.getPledgeType(), loanApplicationView.getPledgeType());
        assertEquals(loanApplicationDto.getPledgeInfo(), loanApplicationView.getPledgeInfo());
        assertEquals(null, loanApplicationView.getComment());
        assertEquals(loanApplicationDto.getLoginName(), loanApplicationView.getUpdatedBy());
        assertEquals("userName", loanApplicationView.getUserName());
    }
}
