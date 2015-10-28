package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.security.MyUser;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;


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
    private InvestMapper investMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;


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
        AccountModel fakeAccount = new AccountModel(fakeUser.getLoginName(), "userName", "id", "payUserId", "payAccountId", new Date());
        accountMapper.create(fakeAccount);

        LoanDto loanDto = getLoanDto(fakeUser);
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertTrue(baseDto.getData().getStatus());
        assertNotNull(loanMapper.findById(loanDto.getId()));
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() > 0);
    }

    private LoanDto getLoanDto(UserModel userModel) {
        LoanDto loanDto = new LoanDto();
        long loanId = idGenerator.generate();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName(userModel.getLoginName());
        loanDto.setAgentLoginName(userModel.getLoginName());
        loanDto.setMaxInvestAmount("100.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("1000000.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
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
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setType(LoanType.LOAN_TYPE_1);
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
            loanTitleRelationModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        return this.loanService.createLoan(loanDto);
    }

    @Test
    public void updateLoanTest() {
        UserModel fakeUser = getFakeUser("loginName");
        userMapper.create(fakeUser);
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
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setType(LoanType.LOAN_TYPE_1);
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
            loanTitleRelationModel.setApplyMetarialUrl("www.baidu.com,www.google.com");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        loanService.updateLoan(loanDto);
        assertTrue(LoanStatus.WAITING_VERIFY == loanMapper.findById(loanId).getStatus());
    }

    @Test
    public void shouldGetLoanDetailTest(){
        mockLoginUser("loginName", "13900000000");

        long id = createLoanService();
        BaseDto<LoanDto> baseDto = loanService.getLoanDetail(id);
        Assert.assertNotNull(baseDto.getData().getId());
        Assert.assertNotNull(baseDto.getData().getLoanTitles().get(0).getApplyMetarialUrl());
        assertEquals(99.5, baseDto.getData().getAmountNeedRaised());
        assertEquals(0.00, baseDto.getData().getRaiseCompletedRate());
    }
    @Test
    public void shouldGetTheInvests(){
        long loanId = createLoanService();
        createTestInvests(loanId, "loginName", 10);
        BaseDto<BasePaginationDataDto> baseDto = loanService.getInvests(loanId, 1, 5);
        assertEquals(5, baseDto.getData().getRecords().size());
        assertEquals(true, baseDto.getData().isHasNextPage());
        assertEquals(false, baseDto.getData().isHasPreviousPage());
    }

    @Test
    public void shouldGetTheInvestsAndNextPagePreviousPage(){
        String mockUserName = "loginUser";
        createMockUser(mockUserName);
        long loanId = createLoanService();
        // 虽然这里创建了10条投资记录，但是在上个方法里，已经创建了三条投资记录，其中已经包含了一个success的记录
        createTestInvests(loanId, mockUserName, 10);

        BaseDto<BasePaginationDataDto> baseDto = loanService.getInvests(loanId, 1, 3);
        assertEquals(3, baseDto.getData().getRecords().size());
        assertEquals(11, baseDto.getData().getCount());

        baseDto = loanService.getInvests(loanId, 4, 3);
        BasePaginationDataDto data = baseDto.getData();
        assertEquals(2, data.getRecords().size());
        assertEquals(false, data.isHasNextPage());
        assertEquals(true, data.isHasPreviousPage());
    }
    private void createTestInvests(long loanId, String loginName, int count){

        for(int i=0;i<count;i++) {
            InvestModel investModel = this.getFakeInvestModel(idGenerator.generate(), loginName);
            investModel.setLoanId(loanId);
            investModel.setStatus(InvestStatus.SUCCESS);
            investModel.setCreatedTime(DateUtils.addHours(new Date(), -i));
            investMapper.create(investModel);
        }
    }

    private long createLoanService(){
        String fakeUserName = "loginName";
        String fakeUserName2 = "investorName";
        UserModel userModel = getFakeUser(fakeUserName);
        UserModel userModel2 = getFakeUser(fakeUserName2);
        userMapper.create(userModel);
        userMapper.create(userModel2);

        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(fakeUserName);
        loanModel.setBaseRate(16.00);
        long id = idGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(30);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestFeeRate(15);
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.LOAN_TYPE_1);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName(fakeUserName);
        loanMapper.create(loanModel);
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long titleId = idGenerator.generate();
        loanTitleModel.setId(titleId);
        loanTitleModel.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleModel.setTitle("房产证");
        loanTitleMapper.create(loanTitleModel);

        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 1; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(id);
            loanTitleRelationModel.setTitleId(titleId);
            loanTitleRelationModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModelList);

        InvestModel investModel1 = getFakeInvestModel(id, fakeUserName2);
        investModel1.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel2 = getFakeInvestModel(id, fakeUserName2);
        investModel2.setStatus(InvestStatus.FAIL);

        InvestModel investModel3 = getFakeInvestModel(id, fakeUserName2);
        investModel3.setStatus(InvestStatus.WAITING);

        investMapper.create(investModel1);
        investMapper.create(investModel2);
        investMapper.create(investModel3);

        return id;

    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        InvestModel model = new InvestModel();
        model.setAmount(50);
        // 舍弃毫秒数
        Date currentDate = new Date((new Date().getTime() / 1000) * 1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(InvestSource.ANDROID);
        model.setStatus(InvestStatus.WAITING);
        model.setCreatedTime(new Date());
        return model;
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

    private void createMockUser(String loginName){
        UserModel um = getFakeUser(loginName);
        userMapper.create(um);
    }

    private void mockLoginUser(String loginName, String mobile){
        MyUser user = new MyUser(loginName,"", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"), mobile, "fdafdsa");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }
}
