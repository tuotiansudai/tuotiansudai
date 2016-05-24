package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class OperationDataServiceTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    OperationDataService operationDataService;

    @Autowired
    RedisWrapperClient redisWrapperClient;

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
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setProductType(productType);
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
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
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private InvestModel createInvest(String loginName, long loanId, long amount, Date createTime) {
        InvestModel model = new InvestModel();
        model.setAmount(amount);
        model.setCreatedTime(createTime);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
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
        createInvest("testUserInvest", 10003, 5000, new Date());
        createInvest("testUserInvest", 10003, 6000, new Date());


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
        createInvest("testUserInvest", 10003, 5000, testEndDate);
        createInvest("testUserInvest", 10003, 6000, testEndDate);

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
