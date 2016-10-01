package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
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
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanServiceTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private PledgeHouseMapper pledgeHouseMapper;

    @Autowired
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private RandomUtils randomUtils;

    @Before
    public void createLoanTitle(){
        LoanTitleModel loanTitleModel = new LoanTitleModel(idGenerator.generate(), LoanTitleType.BASE_TITLE_TYPE, "身份证");
        loanTitleMapper.create(loanTitleModel);
    }

    public UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13" + RandomStringUtils.randomNumeric(9));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanDto getFakeLoanDto(String creator, String agent, Date fundraisingStartTime, Date fundraisingEndTime,
                                   long minAmount, long maxAmount) {
        LoanDto loanDto = new LoanDto();
        loanDto.setProjectName("name");
        loanDto.setAgentLoginName(agent);
        loanDto.setLoanerLoginName("loaner");
        loanDto.setLoanerUserName("loanerUsername");
        loanDto.setLoanerIdentityNumber("identifyNumber");
        loanDto.setType(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY);
        loanDto.setPeriods(3);
        loanDto.setDuration(90);
        loanDto.setPledgeType(PledgeType.HOUSE);
        loanDto.setLoanAmount("100000");
        loanDto.setMinInvestAmount(AmountConverter.convertCentToString(minAmount));
        loanDto.setInvestIncreasingAmount("1000");
        loanDto.setMaxInvestAmount(AmountConverter.convertCentToString(maxAmount));
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setProductType(ProductType._90);
        loanDto.setActivityRate("0.1");
        loanDto.setBasicRate("0.1");
        loanDto.setContractId(100);
        loanDto.setFundraisingStartTime(fundraisingStartTime);
        loanDto.setFundraisingEndTime(fundraisingEndTime);
        loanDto.setRaisingCompleteTime(DateTime.parse("2013-1-1").toDate());
        loanDto.setShowOnHome(false);
        loanDto.setCreatedTime(DateTime.parse("2010-1-1").toDate());
        loanDto.setCreatedLoginName(creator);
        loanDto.setVerifyTime(DateTime.parse("2010-1-2").toDate());
        loanDto.setVerifyLoginName(creator);
        loanDto.setRecheckTime(DateTime.parse("2010-1-3").toDate());
        loanDto.setRecheckLoginName(creator);
        loanDto.setLoanStatus(LoanStatus.REPAYING);

        LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
        loanTitleRelationModel.setId(idGenerator.generate());
        loanTitleRelationModel.setTitle("身份证");
        loanTitleRelationModel.setTitleId(1);
        loanTitleRelationModel.setApplicationMaterialUrls("urls");
        loanDto.setLoanTitles(Lists.newArrayList(loanTitleRelationModel));
        return loanDto;
    }

    private LoanDetailsDto getFakeLoanDetailsDto() {
        LoanDetailsDto loanDetailsDto = new LoanDetailsDto();
        loanDetailsDto.setDeclaration("declaration");
        return loanDetailsDto;
    }

    private LoanerDetailsDto getFakeLoanerDetailsDto() {
        LoanerDetailsDto loanerDetailsDto = new LoanerDetailsDto();
        loanerDetailsDto.setLoanerLoginName("loaner");
        loanerDetailsDto.setLoanerUserName("loanerUserName");
        loanerDetailsDto.setLoanerAge(10);
        loanerDetailsDto.setLoanerIdentityNumber("identifyNumber");
        loanerDetailsDto.setLoanerGender(Gender.FEMALE);
        loanerDetailsDto.setLoanerIncome("income");
        loanerDetailsDto.setLoanerMarriage(Marriage.DIVORCE);
        loanerDetailsDto.setLoanerRegion("region");
        loanerDetailsDto.setLoanerEmploymentStatus("employment");
        return loanerDetailsDto;
    }

    private AbstractPledgeDetailsDto getPledgeDetailsDto(PledgeType pledgeType) {
        AbstractPledgeDetailsDto pledgeDetailsDto = null;
        if(pledgeType == PledgeType.HOUSE) {
            pledgeDetailsDto = new PledgeHouseDto(idGenerator.generate(), "location", "estimateAmount", "loanAmount", "square", "propertyId", "registerId", "authenticAct");
        } else if(pledgeType == PledgeType.VEHICLE) {
            pledgeDetailsDto = new PledgeVehicleDto(idGenerator.generate(), "location", "estimateAmount", "loanAmount", "brand", "model");
        }
        return pledgeDetailsDto;
    }

    @Test
    public void shouldShowEncryptLoginNameWhenAnonymousAndExcludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(100000L);
        assertEquals(fakeUser.getMobile().substring(0,3)+"****"+fakeUser.getMobile().substring(7), randomUtils.encryptMobile("", investModel1.getLoginName(), investModel1.getId(),Source.WEB));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenAnonymousAndIncludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        UserModel fakeUser2 = getFakeUser("loginName2");
        userMapper.create(fakeUser2);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000002L);

        assertEquals(fakeUser.getMobile().substring(0,3) + "****", randomUtils.encryptMobile("", investModel1.getLoginName(), investModel1.getId(),Source.WEB).substring(0,7));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameSameAsInvestorLoginName() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        UserModel fakeUser2 = getFakeUser("loginName2");
        userMapper.create(fakeUser2);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000002L);
        UserModel userModel = this.createUserByUserId("ttdblvjing", "13333333333");

        assertEquals(fakeUser.getMobile().substring(0,3)+"****"+fakeUser.getMobile().substring(7), randomUtils.encryptMobile(userModel.getLoginName(), investModel1.getLoginName(), investModel1.getId(),Source.WEB));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameNotSameAsInvestorLoginNameAndIncludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName1");
        userMapper.create(fakeUser);
        UserModel fakeUser2 = getFakeUser("loginName2");
        userMapper.create(fakeUser2);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000002L);
        assertEquals(fakeUser.getMobile().substring(0, 3)+"****", randomUtils.encryptMobile("loginName2", investModel1.getLoginName(), investModel1.getId(),Source.WEB).substring(0,7));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameNotSameAsInvestorLoginNameAndExcludeShowRandomLoginNameList() {
        UserModel fakeUser = getFakeUser("loginName3");
        userMapper.create(fakeUser);
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName(fakeUser.getLoginName());
        investModel1.setId(1000003L);

        UserModel userModel2 = createUserByUserId("loginName2", "13444444444");
        assertEquals(fakeUser.getMobile().substring(0,3)+"****" + fakeUser.getMobile().substring(7), randomUtils.encryptMobile(userModel2.getLoginName(), investModel1.getLoginName(), investModel1.getId(),Source.WEB));
    }

    private String getDefaultkey(){
        redisWrapperClient.set("webmobile:1000002:ttdblvjing:showinvestorname","13333333333");
        return redisWrapperClient.get("webmobile:1000002:ttdblvjing:showinvestorname");
    }

    private UserModel createUserByUserId(String userId, String mobile) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(mobile);
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }
}
