package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PledgeHouseMapperTest {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    PledgeHouseMapper pledgeHouseMapper;

    private UserModel createUserModel() {
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        userModel.setMobile(RandomStringUtils.randomNumeric(11));
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
    public void testCreateAndGetPledgeHouseDetailByLoanId() throws Exception {
        prepareData();

        PledgeHouseModel pledgeHouseModel = new PledgeHouseModel(9999L, "pledgeLocation", "estimateAmount", "pledgeLoanAmount",
                "square", "propertyCardId", "estateRegisterId", "authenticAct");

        pledgeHouseMapper.create(pledgeHouseModel);

        PledgeHouseModel findPledgeHouseModel = (PledgeHouseModel) pledgeHouseMapper.getByLoanId(pledgeHouseModel.getLoanId());
        assertNotNull(findPledgeHouseModel);
        assertEquals(pledgeHouseModel.getLoanId(), findPledgeHouseModel.getLoanId());
        assertEquals(pledgeHouseModel.getEstimateAmount(), findPledgeHouseModel.getEstimateAmount());
        assertEquals(pledgeHouseModel.getLoanAmount(), findPledgeHouseModel.getLoanAmount());
        assertEquals(pledgeHouseModel.getPledgeLocation(), findPledgeHouseModel.getPledgeLocation());
        assertEquals(pledgeHouseModel.getAuthenticAct(), findPledgeHouseModel.getAuthenticAct());
        assertEquals(pledgeHouseModel.getEstateRegisterId(), findPledgeHouseModel.getEstateRegisterId());
        assertEquals(pledgeHouseModel.getPropertyCardId(), findPledgeHouseModel.getPropertyCardId());
        assertEquals(pledgeHouseModel.getSquare(), findPledgeHouseModel.getSquare());
    }

    @Test
    public void testUpdateByLoanId() throws Exception {
        prepareData();
        PledgeHouseModel pledgeHouseModel = new PledgeHouseModel(9999L, "pledgeLocation", "estimateAmount", "pledgeLoanAmount",
                "square", "propertyCardId", "estateRegisterId", "authenticAct");
        pledgeHouseMapper.create(pledgeHouseModel);

        pledgeHouseModel.setPledgeLocation("updateLocation");
        pledgeHouseModel.setEstimateAmount("updateValue");
        pledgeHouseModel.setLoanAmount("updateLoanAmount");
        pledgeHouseModel.setSquare("updateSquare");
        pledgeHouseModel.setPropertyCardId("updateCardId");
        pledgeHouseModel.setEstateRegisterId("updateRegisterId");
        pledgeHouseModel.setAuthenticAct("updateAct");

        pledgeHouseMapper.updateByLoanId(pledgeHouseModel);
        PledgeHouseModel findPledgeHouseModel = (PledgeHouseModel) pledgeHouseMapper.getByLoanId(pledgeHouseModel.getLoanId());
        assertNotNull(findPledgeHouseModel);
        assertEquals(pledgeHouseModel.getLoanId(), findPledgeHouseModel.getLoanId());
        assertEquals(pledgeHouseModel.getEstimateAmount(), findPledgeHouseModel.getEstimateAmount());
        assertEquals(pledgeHouseModel.getLoanAmount(), findPledgeHouseModel.getLoanAmount());
        assertEquals(pledgeHouseModel.getPledgeLocation(), findPledgeHouseModel.getPledgeLocation());
        assertEquals(pledgeHouseModel.getAuthenticAct(), findPledgeHouseModel.getAuthenticAct());
        assertEquals(pledgeHouseModel.getEstateRegisterId(), findPledgeHouseModel.getEstateRegisterId());
        assertEquals(pledgeHouseModel.getPropertyCardId(), findPledgeHouseModel.getPropertyCardId());
        assertEquals(pledgeHouseModel.getSquare(), findPledgeHouseModel.getSquare());
    }

    @Test
    public void testDeleteByLoanId() throws Exception {
        prepareData();
        PledgeHouseModel pledgeHouseModel = new PledgeHouseModel(9999L, "pledgeLocation", "estimateAmount", "pledgeLoanAmount",
                "square", "propertyCardId", "estateRegisterId", "authenticAct");
        pledgeHouseMapper.create(pledgeHouseModel);
        assertNotNull(pledgeHouseMapper.getByLoanId(pledgeHouseModel.getLoanId()));

        pledgeHouseMapper.deleteByLoanId(pledgeHouseModel.getLoanId());
        assertNull(pledgeHouseMapper.getByLoanId(pledgeHouseModel.getLoanId()));
    }
}