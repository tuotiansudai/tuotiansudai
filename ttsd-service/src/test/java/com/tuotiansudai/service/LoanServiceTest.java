package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.*;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


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

    /**
     * 正常建标
     */
    @Test
    public void createLoanServiceTest_1() {
        UserModel fakeUser = getFakeUser("loginName");
        userMapper.create(fakeUser);
        userRoleMapper.create(Lists.newArrayList(new UserRoleModel(fakeUser.getLoginName(), Role.LOANER)));
        AccountModel fakeAccount = new AccountModel(fakeUser.getLoginName(), "userName", "id", "payUserId", "payAccountId", new Date());
        accountMapper.create(fakeAccount);
        LoanDto loanDto = getLoanDto(fakeUser);
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertThat(baseDto.getData().getStatus(),is(true));
        assertNotNull(loanMapper.findById(loanDto.getId()));
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() > 0);
    }

    private LoanDto getLoanDto(UserModel userModel, long loanId, Date date) {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName(userModel.getLoginName());
        loanDto.setAgentLoginName(userModel.getLoginName());
        loanDto.setMaxInvestAmount("100.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("1000000.00");
        loanDto.setFundraisingEndTime(date);
        loanDto.setFundraisingStartTime(date);
        loanDto.setProductType(ProductType._30);
        return loanDto;
    }

    private LoanDto getLoanDto(UserModel userModel) {
        LoanDto loanDto = new LoanDto();
        long loanId = idGenerator.generate();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName(userModel.getLoginName());
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userModel.getLoginName());
        loanDto.setMaxInvestAmount("100.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("1000000.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setProductType(ProductType._30);
        return loanDto;
    }

    /**
     * 建标时，起投时间晚于结束时间
     */
    @Test
    public void createLoanServiceTest_2() {
        LoanDto loanDto = new LoanDto();
        long id = idGenerator.generate();
        loanDto.setId(id);
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setMaxInvestAmount("100000000000.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("100000000000.00");
        loanDto.setFundraisingStartTime(new Date(System.currentTimeMillis() + 1000));
        loanDto.setFundraisingEndTime(new Date(System.currentTimeMillis()));
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(id).size() == 0);
        assertNull(loanMapper.findById(id));
    }

    /**
     * 投资最大金额小于投资最小金额
     */
    @Test
    public void createLoanServiceTest_3() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("10000.00");
        loanDto.setMaxInvestAmount("99.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 投资金额小于投资最大金额
     */
    @Test
    public void createLoanServiceTest_4() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("998.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 借款人不存在
     */
    @Test
    public void createLoanServiceTest_5() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("liming");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("1000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 代理人不存在
     */
    @Test
    public void createLoanServiceTest_6() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("liming");
        loanDto.setLoanAmount("1000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = loanService.createLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    private BaseDto<PayDataDto> creteLoan(LoanDto loanDto) {
        loanDto.setProjectName("just for a test");
        loanDto.setActivityRate("12");
        loanDto.setBasicRate("16.00");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(10);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 5; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanDto.getId());
            List<LoanTitleModel> loanTitleModelList = loanTitleMapper.findAll();
            if (loanTitleModelList != null && loanTitleModelList.size() > 0){
                loanTitleRelationModel.setTitleId(loanTitleModelList.get(0).getId());
            }
            loanTitleRelationModel.setApplicationMaterialUrls("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        return this.loanService.createLoan(loanDto);
    }

    @Test
    public void findLoanListServiceTest() {
        Date date = new Date();
        long id = idGenerator.generate();
        UserModel fakeUser = getFakeUser("loginName");
        userMapper.create(fakeUser);
        AccountModel fakeAccount = new AccountModel(fakeUser.getLoginName(), "userName", "id", "payUserId", "payAccountId", new Date());
        accountMapper.create(fakeAccount);

        LoanDto loanDto = getLoanDto(fakeUser,id,date);
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        List<LoanListDto> loanListDtos = loanService.findLoanList(LoanStatus.REPAYING, id, "", date, date, 0, 10);
        int loanListCount = loanService.findLoanListCount(LoanStatus.REPAYING,id,"",date,date);
        assertThat(loanListDtos.size(), is(loanListCount));
    }

    public void updateLoanTest() {
        UserModel fakeUser = getFakeUser("loginName");
        userMapper.create(fakeUser);
        userRoleMapper.create(Lists.newArrayList(new UserRoleModel(fakeUser.getLoginName(), Role.LOANER)));
        AccountModel fakeAccount = new AccountModel(fakeUser.getLoginName(), "userName", "id", "payUserId", "payAccountId", new Date());
        accountMapper.create(fakeAccount);

        this.creteLoan(getLoanDto(fakeUser));
        List<LoanModel> loanModelList = loanMapper.findByStatus(LoanStatus.WAITING_VERIFY);
        long loanId = loanModelList.get(0).getId();
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName(fakeUser.getLoginName());
        loanDto.setAgentLoginName(fakeUser.getLoginName());
        loanDto.setLoanAmount("5000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setProjectName("店铺资金周转更新");
        loanDto.setActivityRate("12.00");
        loanDto.setBasicRate("16.00");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 5; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanDto.getId());
            List<LoanTitleModel> loanTitleModelList = loanTitleMapper.findAll();
            if (loanTitleModelList != null && loanTitleModelList.size() > 0) {
                loanTitleRelationModel.setTitleId(loanTitleModelList.get(0).getId());
            }
            loanTitleRelationModel.setApplicationMaterialUrls("www.baidu.com,www.google.com");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        loanService.updateLoan(loanDto);
        assertTrue(LoanStatus.WAITING_VERIFY == loanMapper.findById(loanId).getStatus());
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
