package com.tuotiansudai.repository.mapper;

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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanApplicationMapperTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    LoanApplicationMapper loanApplicationMapper;

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
        createAccountModel(user1);
        createAccountModel(user2);
    }

    private LoanApplicationModel createLoanApplicationModel(String loginName) {
        LoanApplicationModel loanApplicationModel = new LoanApplicationModel();
        loanApplicationModel.setLoginName(loginName);
        loanApplicationModel.setRegion(LoanApplicationRegion.BEI_JING);
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

    @Test
    public void testCreateAndFindById() throws Exception {
        prepareData();
        LoanApplicationModel loanApplicationModel = createLoanApplicationModel("user1");

        LoanApplicationModel findLoanApplicationModel = loanApplicationMapper.findById(loanApplicationModel.getId());
        assertNotNull(findLoanApplicationModel);
        assertEquals(loanApplicationModel.getLoginName(), findLoanApplicationModel.getLoginName());
        assertEquals(loanApplicationModel.getRegion(), findLoanApplicationModel.getRegion());
        assertEquals(loanApplicationModel.getAmount(), findLoanApplicationModel.getAmount());
        assertEquals(loanApplicationModel.getPeriod(), findLoanApplicationModel.getPeriod());
        assertEquals(loanApplicationModel.getPledgeType(), findLoanApplicationModel.getPledgeType());
        assertEquals(loanApplicationModel.getPledgeInfo(), findLoanApplicationModel.getPledgeInfo());
        assertEquals(loanApplicationModel.getComment(), findLoanApplicationModel.getComment());
        assertEquals(loanApplicationModel.getCreatedTime(), findLoanApplicationModel.getCreatedTime());
        assertEquals(loanApplicationModel.getUpdatedBy(), findLoanApplicationModel.getUpdatedBy());
        assertEquals(loanApplicationModel.getUpdatedTime(), findLoanApplicationModel.getUpdatedTime());
    }

    @Test
    public void testUpdate() throws Exception {
        prepareData();
        LoanApplicationModel originLoanApplicationModel1 = createLoanApplicationModel("user1");
        LoanApplicationModel originLoanApplicationModel2 = createLoanApplicationModel("user2");

        LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(originLoanApplicationModel1.getId());
        assertNotNull(loanApplicationModel);
        loanApplicationModel.setLoginName("user2");
        loanApplicationModel.setRegion(LoanApplicationRegion.CHENG_DE);
        loanApplicationModel.setAmount(2);
        loanApplicationModel.setPeriod(3);
        loanApplicationModel.setPledgeType(PledgeType.VEHICLE);
        loanApplicationModel.setPledgeInfo("testInfoUpdate");
        loanApplicationModel.setComment("testCommentUpdate");
        loanApplicationModel.setCreatedTime(DateTime.parse("2011-1-2").toDate());
        loanApplicationModel.setUpdatedBy("user2");
        loanApplicationModel.setUpdatedTime(DateTime.parse("2011-2-2").toDate());

        loanApplicationMapper.update(loanApplicationModel);

        LoanApplicationModel updateLoanApplicationModel1 = loanApplicationMapper.findById(originLoanApplicationModel1.getId());
        assertEquals(loanApplicationModel.getLoginName(), updateLoanApplicationModel1.getLoginName());
        assertEquals(loanApplicationModel.getRegion(), updateLoanApplicationModel1.getRegion());
        assertEquals(loanApplicationModel.getAmount(), updateLoanApplicationModel1.getAmount());
        assertEquals(loanApplicationModel.getPeriod(), updateLoanApplicationModel1.getPeriod());
        assertEquals(loanApplicationModel.getPledgeType(), updateLoanApplicationModel1.getPledgeType());
        assertEquals(loanApplicationModel.getPledgeInfo(), updateLoanApplicationModel1.getPledgeInfo());
        assertEquals(loanApplicationModel.getComment(), updateLoanApplicationModel1.getComment());
        assertEquals(loanApplicationModel.getCreatedTime(), updateLoanApplicationModel1.getCreatedTime());
        assertEquals(loanApplicationModel.getUpdatedBy(), updateLoanApplicationModel1.getUpdatedBy());
        assertEquals(loanApplicationModel.getUpdatedTime(), updateLoanApplicationModel1.getUpdatedTime());

        LoanApplicationModel findLoanApplicationModel2 = loanApplicationMapper.findById(originLoanApplicationModel2.getId());
        assertEquals(originLoanApplicationModel2.getLoginName(), findLoanApplicationModel2.getLoginName());
        assertEquals(originLoanApplicationModel2.getRegion(), findLoanApplicationModel2.getRegion());
        assertEquals(originLoanApplicationModel2.getAmount(), findLoanApplicationModel2.getAmount());
        assertEquals(originLoanApplicationModel2.getPeriod(), findLoanApplicationModel2.getPeriod());
        assertEquals(originLoanApplicationModel2.getPledgeType(), findLoanApplicationModel2.getPledgeType());
        assertEquals(originLoanApplicationModel2.getPledgeInfo(), findLoanApplicationModel2.getPledgeInfo());
        assertEquals(originLoanApplicationModel2.getComment(), findLoanApplicationModel2.getComment());
        assertEquals(originLoanApplicationModel2.getCreatedTime(), findLoanApplicationModel2.getCreatedTime());
        assertEquals(originLoanApplicationModel2.getUpdatedBy(), findLoanApplicationModel2.getUpdatedBy());
        assertEquals(originLoanApplicationModel2.getUpdatedTime(), findLoanApplicationModel2.getUpdatedTime());
    }

    @Test
    public void testFindCountAndFindViewPagination() throws Exception {
        prepareData();
        LoanApplicationModel originLoanApplicationModel1 = createLoanApplicationModel("user1");
        LoanApplicationModel originLoanApplicationModel2 = createLoanApplicationModel("user2");
        assertEquals(2L, loanApplicationMapper.findCount());

        List<LoanApplicationView> loanApplicationViews = loanApplicationMapper.findViewPagination(0, 10);
        assertEquals(2, loanApplicationViews.size());
        for (LoanApplicationView loanApplicationView : loanApplicationViews) {
            if (loanApplicationView.getId() == originLoanApplicationModel1.getId()) {
                assertEquals(originLoanApplicationModel1.getLoginName(), loanApplicationView.getLoginName());
            } else if (loanApplicationView.getId() == originLoanApplicationModel2.getId()) {
                assertEquals(originLoanApplicationModel2.getLoginName(), loanApplicationView.getLoginName());
            }
            assertEquals(originLoanApplicationModel1.getRegion(), loanApplicationView.getRegion());
            assertEquals(originLoanApplicationModel1.getAmount(), loanApplicationView.getAmount());
            assertEquals(originLoanApplicationModel1.getPeriod(), loanApplicationView.getPeriod());
            assertEquals(originLoanApplicationModel1.getPledgeType(), loanApplicationView.getPledgeType());
            assertEquals(originLoanApplicationModel1.getPledgeInfo(), loanApplicationView.getPledgeInfo());
            assertEquals(originLoanApplicationModel1.getComment(), loanApplicationView.getComment());
            assertEquals(originLoanApplicationModel1.getCreatedTime(), loanApplicationView.getCreatedTime());
            assertEquals(originLoanApplicationModel1.getUpdatedBy(), loanApplicationView.getUpdatedBy());
            assertEquals(originLoanApplicationModel1.getUpdatedTime(), loanApplicationView.getUpdatedTime());
        }

        loanApplicationViews = loanApplicationMapper.findViewPagination(0, 1);
        assertEquals(1, loanApplicationViews.size());
    }
}
