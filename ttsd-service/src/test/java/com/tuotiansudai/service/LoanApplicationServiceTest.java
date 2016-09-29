package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanApplicationServiceTest {

    @Autowired
    UserMapper userMapper;

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
        userModel.setMobile(String.valueOf(random.nextLong()));
        userModel.setPassword("password");
        userModel.setSalt("salt");
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userMapper.create(userModel);
        return userModel;
    }

    private AccountModel createAccountModel(UserModel userModel) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), "userName", String.valueOf(random.nextLong()).substring(0, 10),
                String.valueOf(random.nextLong()).substring(0, 10), String.valueOf(random.nextLong()).substring(0, 10), new Date());
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
        loanApplicationModel.setRegion(LoanApplicationRegion.北京);
        loanApplicationModel.setAmount(1);
        loanApplicationModel.setPeriod(2);
        loanApplicationModel.setPledgeType(PledgeType.HOUSE);
        loanApplicationModel.setPledgeInfo("testInfo");
        loanApplicationModel.setComment("testComment");
        loanApplicationModel.setCreatedTime(DateTime.parse("2011-1-1").toDate());
        loanApplicationModel.setUpdatedBy("user1");
        loanApplicationModel.setUpdatedTime(DateTime.parse("2011-2-1").toDate());

        loanApplicationMapper.create(loanApplicationModel);
        return loanApplicationModel;
    }

    private LoanApplicationDto fakeLoanApplicationDto(String loginName, int amount, int period) {
        LoanApplicationDto loanApplicationDto = new LoanApplicationDto();
        loanApplicationDto.setLoginName(loginName);
        loanApplicationDto.setRegion(LoanApplicationRegion.北京);
        loanApplicationDto.setAmount(amount);
        loanApplicationDto.setPeriod(period);
        loanApplicationDto.setPledgeType(PledgeType.HOUSE);
        loanApplicationDto.setPledgeInfo("testInfo");
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

        List<LoanApplicationView> loanApplicationViews = loanApplicationMapper.findViewPagination(0, 1);
        assertEquals(1, loanApplicationViews.size());
        LoanApplicationView loanApplicationView = loanApplicationViews.get(0);
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

    @Test
    public void testComment() throws Exception {
        prepareData();
        LoanApplicationModel loanApplicationModel = createLoanApplicationModel("user1");
        List<LoanApplicationView> loanApplicationViews = loanApplicationMapper.findViewPagination(0, 1);
        assertEquals(1, loanApplicationViews.size());
        LoanApplicationView loanApplicationView = loanApplicationViews.get(0);

        loanApplicationView.setLoginName("user2");
        loanApplicationView.setRegion(LoanApplicationRegion.承德);
        loanApplicationView.setAmount(33);
        loanApplicationView.setPeriod(44);
        loanApplicationView.setPledgeType(PledgeType.VEHICLE);
        loanApplicationView.setPledgeInfo("updateInfo");
        loanApplicationView.setComment("updateComment");
        loanApplicationView.setUpdatedBy("user2");
        loanApplicationView.setCreatedTime(DateTime.parse("2019-1-1").toDate());
        loanApplicationView.setUpdatedTime(DateTime.parse("2020-1-1").toDate());
        BaseDto<BaseDataDto> baseDto = loanApplicationService.comment(loanApplicationView);
        assertEquals(true, baseDto.getData().getStatus());

        loanApplicationViews = loanApplicationMapper.findViewPagination(0, 1);
        assertEquals(1, loanApplicationViews.size());
        LoanApplicationView updateLoanApplicationView = loanApplicationViews.get(0);
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
        baseDto = loanApplicationService.comment(loanApplicationView);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("该借款申请不存在", baseDto.getData().getMessage());
    }

    @Test
    public void testGetPagination() throws Exception {
        prepareData();
        createLoanApplicationModel("user1");
        createLoanApplicationModel("user2");

        BasePaginationDataDto<LoanApplicationView> basePaginationDataDto = loanApplicationService.getPagination(1, 10);
        assertEquals(2, basePaginationDataDto.getRecords().size());
    }
}
