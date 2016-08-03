package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanerDetailsMapperTest {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    LoanerDetailsMapper loanerDetailsMapper;

    private UserModel createUserModel() {
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        userModel.setMobile("13900000000");
        userModel.setPassword("password");
        userModel.setSalt("salt");
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userMapper.create(userModel);
        return userModel;
    }

    private LoanModel createLoanModel(long loanId) {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(loanId);
        loanModel.setName("loanName");
        loanModel.setLoanerLoginName("loginName");
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setAgentLoginName("loginName");
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setPeriods(3);
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setDescriptionHtml("html");
        loanModel.setDescriptionText("text");
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanModel.setCreatedTime(new Date());
        loanMapper.create(loanModel);
        return loanModel;
    }

    private void prepareData() {
        createUserModel();
        createLoanModel(9999L);
    }

    @Test
    public void testCreateLoanerDetailsAndGetLoanerDetailByLoanId() throws Exception {
        prepareData();

        LoanerDetailsModel loanerDetailsModel = new LoanerDetailsModel(9999L, "testLoaner", "loaner", Gender.MALE, 12,
                "123814134", Marriage.DIVORCE, "region", "income", "employment");

        loanerDetailsMapper.create(loanerDetailsModel);

        LoanerDetailsModel findLoanDetailsModel = loanerDetailsMapper.getLoanerDetailByLoanId(loanerDetailsModel.getLoanId());
        assertNotNull(findLoanDetailsModel);
        assertEquals(loanerDetailsModel.getLoanId(), findLoanDetailsModel.getLoanId());
        assertEquals(loanerDetailsModel.getAge(), findLoanDetailsModel.getAge());
        assertEquals(loanerDetailsModel.getEmploymentStatus(), findLoanDetailsModel.getEmploymentStatus());
        assertEquals(loanerDetailsModel.getGender(), findLoanDetailsModel.getGender());
        assertEquals(loanerDetailsModel.getIdentityNumber(), findLoanDetailsModel.getIdentityNumber());
        assertEquals(loanerDetailsModel.getIncome(), findLoanDetailsModel.getIncome());
        assertEquals(loanerDetailsModel.getMarriage(), findLoanDetailsModel.getMarriage());
        assertEquals(loanerDetailsModel.getUserName(), findLoanDetailsModel.getUserName());
        assertEquals(loanerDetailsModel.getRegion(), findLoanDetailsModel.getRegion());
        assertEquals(loanerDetailsModel.getLoginName(), findLoanDetailsModel.getLoginName());
    }

    @Test
    public void testUpdateByLoanIdupdateByLoanId() throws Exception {
        prepareData();
        LoanerDetailsModel loanerDetailsModel = new LoanerDetailsModel(9999L, "testLoaner", "loaner", Gender.MALE, 12,
                "123814134", Marriage.DIVORCE, "region", "income", "employment");
        loanerDetailsMapper.create(loanerDetailsModel);

        loanerDetailsModel.setLoginName("updateLoginName");
        loanerDetailsModel.setUserName("updateUserName");
        loanerDetailsModel.setGender(Gender.FEMALE);
        loanerDetailsModel.setAge(123);
        loanerDetailsModel.setIdentityNumber("324234");
        loanerDetailsModel.setMarriage(Marriage.MARRIED);
        loanerDetailsModel.setRegion("updateRegion");
        loanerDetailsModel.setIncome("updateIncome");
        loanerDetailsModel.setEmploymentStatus("updateStatus");

        loanerDetailsMapper.updateByLoanId(loanerDetailsModel);

        LoanerDetailsModel findLoanDetailsModel = loanerDetailsMapper.getLoanerDetailByLoanId(loanerDetailsModel.getLoanId());
        assertNotNull(findLoanDetailsModel);
        assertEquals(loanerDetailsModel.getAge(), findLoanDetailsModel.getAge());
        assertEquals(loanerDetailsModel.getEmploymentStatus(), findLoanDetailsModel.getEmploymentStatus());
        assertEquals(loanerDetailsModel.getGender(), findLoanDetailsModel.getGender());
        assertEquals(loanerDetailsModel.getIdentityNumber(), findLoanDetailsModel.getIdentityNumber());
        assertEquals(loanerDetailsModel.getIncome(), findLoanDetailsModel.getIncome());
        assertEquals(loanerDetailsModel.getMarriage(), findLoanDetailsModel.getMarriage());
        assertEquals(loanerDetailsModel.getUserName(), findLoanDetailsModel.getUserName());
        assertEquals(loanerDetailsModel.getRegion(), findLoanDetailsModel.getRegion());
        assertEquals(loanerDetailsModel.getLoginName(), findLoanDetailsModel.getLoginName());
    }
}
