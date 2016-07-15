package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
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
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        loanTitleModel.setId(idGenerator.generate());
        loanTitleModel.setTitle("身份证");
        loanTitleModel.setType(LoanTitleType.BASE_TITLE_TYPE);
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
        loanDto.setLoanAmount("1000000");
        loanDto.setMinInvestAmount(String.valueOf(minAmount));
        loanDto.setInvestIncreasingAmount("1000");
        loanDto.setMaxInvestAmount(String.valueOf(maxAmount));
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

    private BaseDto<BaseDataDto> createLoan(PledgeType pledgeType, Date fundraisingStartTime, Date fundraisingEndTime,
                                            long minAmount, long maxAmount) {
        UserModel creator = getFakeUser("creator");
        if(null == userMapper.findByLoginName(creator.getLoginName())) {
            userMapper.create(creator);
        }
        UserModel agent = getFakeUser("agent");
        if(null == userMapper.findByLoginName(agent.getLoginName())) {
            userMapper.create(agent);
            userRoleMapper.create(Lists.newArrayList(new UserRoleModel(agent.getLoginName(), Role.LOANER)));
            AccountModel accountModel = new AccountModel(agent.getLoginName(), "loanerUserName", "identify", "payUserId",
                    "payAccountId", DateTime.parse("2009-1-1").toDate());
            accountMapper.create(accountModel);
        }

        LoanDetailsDto loanDetailsDto = getFakeLoanDetailsDto();
        LoanerDetailsDto loanerDetailsDto = getFakeLoanerDetailsDto();
        PledgeHouseDto pledgeHouseDto = (PledgeHouseDto)getPledgeDetailsDto(PledgeType.HOUSE);
        PledgeVehicleDto pledgeVehicleDto = (PledgeVehicleDto)getPledgeDetailsDto(PledgeType.VEHICLE);

        LoanDto loanDto = getFakeLoanDto(creator.getLoginName(), agent.getLoginName(), fundraisingStartTime, fundraisingEndTime,
                minAmount, maxAmount);
        if(pledgeType == PledgeType.HOUSE) {
            return loanService.createLoan(loanDto, loanDetailsDto, loanerDetailsDto, pledgeHouseDto);
        } else if(pledgeType == PledgeType.VEHICLE) {
            return loanService.createLoan(loanDto, loanDetailsDto, loanerDetailsDto, pledgeVehicleDto);
        }
        return null;
    }

    @Test
    public void testCreateLoan() throws Exception {
        long loanId = 0;
        //test success House
        BaseDto<BaseDataDto> baseDto = createLoan(PledgeType.HOUSE, DateTime.parse("2011-1-1").toDate(),
                DateTime.parse("2011-1-2").toDate(), 100, 10000);
        assertEquals(true, baseDto.getData().getStatus());
        loanId = Long.valueOf(baseDto.getData().getMessage());
        assertNotNull(loanMapper.findById(loanId));
        assertTrue(loanTitleRelationMapper.findByLoanId(loanId).size() > 0);
        assertNotNull(loanDetailsMapper.getLoanDetailsByLoanId(loanId));
        assertNotNull(loanerDetailsMapper.getLoanerDetailByLoanId(loanId));
        assertNotNull(pledgeHouseMapper.getPledgeHouseDetailByLoanId(loanId));
        //test success Vehicle
        baseDto = createLoan(PledgeType.VEHICLE, DateTime.parse("2011-1-1").toDate(), DateTime.parse("2011-1-2").toDate(),
                100, 10000);
        assertEquals(true, baseDto.getData().getStatus());
        loanId = Long.valueOf(baseDto.getData().getMessage());
        assertNotNull(loanMapper.findById(loanId));
        assertTrue(loanTitleRelationMapper.findByLoanId(loanId).size() > 0);
        assertNotNull(loanDetailsMapper.getLoanDetailsByLoanId(loanId));
        assertNotNull(loanerDetailsMapper.getLoanerDetailByLoanId(loanId));
        assertNotNull(pledgeVehicleMapper.getPledgeVehicleDetailByLoanId(loanId));
        //建标时，起投时间晚于结束时间
        baseDto = createLoan(PledgeType.HOUSE, DateTime.parse("2011-2-1").toDate(), DateTime.parse("2011-1-1").toDate(),
                100, 10000);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("筹款启动时间不得晚于筹款截止时间", baseDto.getData().getMessage());
        //投资最大金额小于投资最小金额
        baseDto = createLoan(PledgeType.HOUSE, DateTime.parse("2011-2-1").toDate(), DateTime.parse("2011-1-1").toDate(),
                10000, 100);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("最小投资金额不得大于最大投资金额", baseDto.getData().getMessage());
        //投资金额小于投资最大金额
        baseDto = createLoan(PledgeType.HOUSE, DateTime.parse("2011-2-1").toDate(), DateTime.parse("2011-1-1").toDate(),
                9999999, 99999999);
        assertEquals(false, baseDto.getData().getStatus());
        assertEquals("最大投资金额不得大于预计出借金额", baseDto.getData().getMessage());
    }

    @Test
    public void findLoanListServiceTest() {
        BaseDto<BaseDataDto> baseDto = createLoan(PledgeType.HOUSE, DateTime.parse("2011-1-1").toDate(),
                DateTime.parse("2011-1-2").toDate(), 100, 10000);
        long loanId = Long.valueOf(baseDto.getData().getMessage());

        List<LoanListDto> loanListDtos = loanService.findLoanList(LoanStatus.REPAYING, loanId, "", DateTime.parse("2010-1-1").toDate(),
                DateTime.parse("2012-1-2").toDate(), 0, 10);
        int loanListCount = loanService.findLoanListCount(LoanStatus.REPAYING, loanId, "", DateTime.parse("2010-1-1").toDate(),
                DateTime.parse("2012-1-2").toDate());
        assertThat(loanListDtos.size(), is(loanListCount));
    }

    public void updateLoanTest() {
        BaseDto<BaseDataDto> baseDto = createLoan(PledgeType.HOUSE, DateTime.parse("2011-1-1").toDate(), DateTime.parse("2011-1-2").toDate(), 100, 10000);
        long loanId = Long.valueOf(baseDto.getData().getMessage());

        LoanDto loanDto = getFakeLoanDto("creator", "agent", DateTime.parse("2012-1-1").toDate(),
                DateTime.parse("2012-2-1").toDate(), 1000, 20000);
        loanDto.setId(loanId);
        loanService.updateLoan(loanDto);
        LoanModel findLoanModel = loanMapper.findById(loanId);
        assertEquals(DateTime.parse("2012-1-1").toDate(), findLoanModel.getFundraisingStartTime());
        assertEquals(DateTime.parse("2012-2-1").toDate(), findLoanModel.getFundraisingEndTime());
        assertEquals(1000, findLoanModel.getMinInvestAmount());
        assertEquals(20000, findLoanModel.getMaxInvestAmount());
    }

    @Test
    public void shouldShowEncryptLoginNameWhenAnonymousAndExcludeShowRandomLoginNameList() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName("loginName1");
        investModel1.setId(100000L);
        assertEquals("log***", randomUtils.encryptLoginName("", investModel1.getLoginName(), 3, investModel1.getId()));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenAnonymousAndIncludeShowRandomLoginNameList() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName("ttdblvjing");
        investModel1.setId(1000002L);

        assertEquals(this.getDefaultkey(), randomUtils.encryptLoginName("", investModel1.getLoginName(), 3, investModel1.getId()));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameSameAsInvestorLoginName() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName("ttdblvjing");
        investModel1.setId(1000002L);

        assertEquals("ttdblvjing", randomUtils.encryptLoginName("ttdblvjing", investModel1.getLoginName(), 3, investModel1.getId()));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameNotSameAsInvestorLoginNameAndIncludeShowRandomLoginNameList() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName("ttdblvjing");
        investModel1.setId(1000002L);

        assertEquals(this.getDefaultkey(), randomUtils.encryptLoginName("loginName2", investModel1.getLoginName(), 3, investModel1.getId()));
    }

    @Test
    public void shouldShowEncryptLoginNameWhenLoginNameNotSameAsInvestorLoginNameAndExcludeShowRandomLoginNameList() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setLoginName("loginName3");
        investModel1.setId(1000003L);

        assertEquals("log***", randomUtils.encryptLoginName("loginName2", investModel1.getLoginName(), 3, investModel1.getId()));
    }

    private String getDefaultkey(){
        redisWrapperClient.set("webmobile:1000002:ttdblvjing:showinvestorname","bxh***");
        return redisWrapperClient.get("webmobile:1000002:ttdblvjing:showinvestorname");
    }
}
