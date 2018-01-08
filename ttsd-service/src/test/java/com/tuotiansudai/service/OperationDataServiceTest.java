package com.tuotiansudai.service;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class OperationDataServiceTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private FakeUserHelper fakeUserHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private OperationDataService operationDataService;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String REDIS_INFO_PUBLISH_CHART_KEY_TEMPLATE = "web:info:publish:chart:{0}";
    private final String REDIS_INFO_PUBLISH_TABLE_KEY_TEMPLATE = "web:info:publish:table:{0}";

    private final String USERS_COUNT = "userCount";
    private final String TRADE_AMOUNT = "tradeAmount";
    private final String OPERATION_DATA_MONTH = "operationDataMonth";
    private final String OPERATION_DATA_MONTH_AMOUNT = "operationDataMonthAmount";

    private LoanModel createLoanByUserId(String userId, long loanId, ProductType productType) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(31);
        loanDto.setActivityType(ActivityType.NEWBIE);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setProductType(productType);
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setUserName("userName");
        userModelTest.setIdentityNumber("11100419900512000");
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));

        fakeUserHelper.create(userModelTest);
        return userModelTest;
    }

    private UserModel createUserByUserIdAndIdentityNumber(String userId, String identityNumber, Date registerTime) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(registerTime);
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userModelTest.setIdentityNumber(identityNumber);
        fakeUserHelper.create(userModelTest);
        return userModelTest;
    }


    private AccountModel createAccountByloginName(String loginName) {
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new DateTime().minusDays(30).toDate());
        accountMapper.create(accountModel);
        return accountModel;
    }

    private InvestModel createInvest(String loginName, long loanId, long amount, Date createTime) {
        InvestModel model = new InvestModel();
        model.setAmount(amount);
        model.setCreatedTime(createTime);
        model.setId(IdGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private InvestModel createInvest(String loginName, long loanId, long amount, Date createTime, Date investTime) {
        InvestModel model = new InvestModel();
        model.setAmount(amount);
        model.setCreatedTime(createTime);
        model.setId(IdGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setInvestTime(investTime);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    @Test
    public void shouldGetInvestDetail() {
        Date testEndDate = new DateTime().withDate(2016, 5, 10).withTimeAtStartOfDay().toDate();

        UserModel userModelInvest = createUserByUserId("testUserInvest");
        UserModel userModelLoaner = createUserByUserId("testUserLoaner");

        createLoanByUserId("testUserLoaner", 10001, ProductType._30);
        createLoanByUserId("testUserLoaner", 10002, ProductType._90);
        createLoanByUserId("testUserLoaner", 10003, ProductType._180);


        createInvest("testUserInvest", 10001, 1000, new Date());
        createInvest("testUserInvest", 10001, 2000, new Date());
        createInvest("testUserInvest", 10002, 3000, new Date());
        createInvest("testUserInvest", 10002, 4000, new Date());
        InvestModel investModelStart = createInvest("testUserInvest", 10003, 5000, new Date());
        investModelStart.setCreatedTime(DateTime.parse("2016-08-01 00:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        investMapper.update(investModelStart);
        InvestModel investModel = createInvest("testUserInvest", 10003, 6000, new Date());
        investModel.setCreatedTime(DateTime.parse("2016-08-31 23:59:59", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        investMapper.update(investModel);


        List<InvestDataView> investDataViewList = new ArrayList<InvestDataView>();

        assertTrue(operationDataService.getInvestDetail(testEndDate).size() >= 0);
    }

    @Test
    public void testGetOperationDataFromRedis() {
        Date testEndDate = new DateTime().withDate(2016, 5, 10).withTimeAtStartOfDay().toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String redisKey = MessageFormat.format(REDIS_INFO_PUBLISH_CHART_KEY_TEMPLATE, simpleDateFormat.format(testEndDate));

        long originUsersCount = userMapper.findUsersCount();
        long originInvestAmount = investMapper.sumInvestAmount(null, null, null,
                null, null, new DateTime().withDate(2015, 7, 15).withTimeAtStartOfDay().toDate(),
                new DateTime().withMillis(testEndDate.getTime()).withTimeAtStartOfDay().toDate(),
                InvestStatus.SUCCESS, null);

        UserModel userModelInvest = createUserByUserId("testUserInvest");
        UserModel userModelLoaner = createUserByUserId("testUserLoaner");

        createLoanByUserId("testUserLoaner", 10001, ProductType._30);
        createLoanByUserId("testUserLoaner", 10002, ProductType._90);
        createLoanByUserId("testUserLoaner", 10003, ProductType._180);

        createInvest("testUserInvest", 10001, 1000, testEndDate);
        createInvest("testUserInvest", 10001, 2000, testEndDate);
        createInvest("testUserInvest", 10002, 3000, testEndDate);
        createInvest("testUserInvest", 10002, 4000, testEndDate);

        InvestModel investModelStart = createInvest("testUserInvest", 10003, 6000, testEndDate);
        investModelStart.setCreatedTime(DateTime.parse("2016-04-01 00:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        investMapper.update(investModelStart);
        InvestModel investModel = createInvest("testUserInvest", 10003, 5000, testEndDate);
        investModel.setCreatedTime(DateTime.parse("2016-04-30 23:59:59", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        investMapper.update(investModel);

        //测试从数据库取出的数据
        OperationDataDto operationDataDtoFromRedis = operationDataService.getOperationDataFromRedis(testEndDate);
        assertEquals(314, operationDataDtoFromRedis.getOperationDays());
        assertEquals(2 + originUsersCount, operationDataDtoFromRedis.getUsersCount());
        assertEquals(10, operationDataDtoFromRedis.getMonth().size());
        assertEquals("2015.7", operationDataDtoFromRedis.getMonth().get(0));
        assertEquals(10, operationDataDtoFromRedis.getMoney().size());
        assertEquals(AmountConverter.convertCentToString(originInvestAmount + 21000), operationDataDtoFromRedis.getTradeAmount());

        //测试redis中的缓存
        assertEquals(2 + originUsersCount, Integer.parseInt(redisWrapperClient.hget(redisKey, USERS_COUNT)));
        assertEquals(AmountConverter.convertCentToString(originInvestAmount + 21000), redisWrapperClient.hget(redisKey, TRADE_AMOUNT));
        assertEquals("2015.7,2015.8,2015.9,2015.10,2015.11,2015.12,2016.1,2016.2,2016.3,2016.4", redisWrapperClient.hget(redisKey, OPERATION_DATA_MONTH));

        //测试从redis中拿出的数据
        operationDataDtoFromRedis = operationDataService.getOperationDataFromRedis(testEndDate);
        assertEquals(2 + originUsersCount, operationDataDtoFromRedis.getUsersCount());
        assertEquals(10, operationDataDtoFromRedis.getMonth().size());
        assertEquals("2015.7", operationDataDtoFromRedis.getMonth().get(0));
        assertEquals(10, operationDataDtoFromRedis.getMoney().size());
        assertEquals(AmountConverter.convertCentToString(originInvestAmount + 21000), operationDataDtoFromRedis.getTradeAmount());
    }

    @Test
    public void testFindScaleByGender() {
        Date testEndDate = new DateTime().plusDays(30).toDate();
        UserModel userModel1 = createUserByUserIdAndIdentityNumber("testUserInvest1", "42138119880520241X", new DateTime().plusDays(4).toDate());
        UserModel userModel2 = createUserByUserIdAndIdentityNumber("testUserInvest2", "22012219881003356X", new DateTime().plusDays(5).toDate());
        createUserByUserId("testUserLoaner");
        createAccountByloginName(userModel1.getLoginName());
        createAccountByloginName(userModel2.getLoginName());
        LoanModel loanModel = createLoanByUserId("testUserLoaner", 1000001, ProductType._30);
        createInvest(userModel1.getLoginName(), loanModel.getId(), 100000, testEndDate, new DateTime().plusDays(10).toDate());
        createInvest(userModel2.getLoginName(), loanModel.getId(), 100000, testEndDate, new DateTime().plusDays(10).toDate());
        List<Integer> intList = operationDataService.findScaleByGender(testEndDate);
        assertTrue(intList.size() > 0);
    }

    @After
    public void ClearData() {
        Date testEndDate = new DateTime().withDate(2016, 5, 10).withTimeAtStartOfDay().toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String redisKeyChart = MessageFormat.format(REDIS_INFO_PUBLISH_CHART_KEY_TEMPLATE, simpleDateFormat.format(testEndDate));
        redisWrapperClient.del(redisKeyChart);
        String redisKeyTable = MessageFormat.format(REDIS_INFO_PUBLISH_TABLE_KEY_TEMPLATE, simpleDateFormat.format(testEndDate));
        redisWrapperClient.del(redisKeyTable);
    }
}
