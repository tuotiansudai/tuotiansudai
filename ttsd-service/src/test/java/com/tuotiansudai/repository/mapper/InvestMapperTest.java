package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    private String User_ID = "helloworld";
    private String User_ID2 = "testuser";
    private long Loan_ID = 200093022L;
    private long Loan_ID2 = 300093022L;

    @Test
    public void shouldCreateInvest() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);

        InvestModel dbModel = investMapper.findById(investModel.getId());
        assertNotNull(dbModel);
        assertEquals(dbModel.getAmount(), investModel.getAmount());

        assertEquals(dbModel.getCreatedTime(), investModel.getCreatedTime());
    }

    @Test
    public void shouldUpdateInvest() throws Exception{
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);

        InvestModel dbModel = investMapper.findById(investModel.getId());
        assertNotNull(dbModel);
        assertEquals(dbModel.getAmount(), investModel.getAmount());
        assertEquals(dbModel.getStatus(), investModel.getStatus());


        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setAmount(111111111L);

        investMapper.update(investModel);
        InvestModel dbModel2 = investMapper.findById(investModel.getId());

        assertEquals(dbModel2.getStatus(), InvestStatus.SUCCESS);
        assertEquals(dbModel2.getAmount(), 111111111L);
    }

    @Test
    public void shouldUpdateInvestStatus(){
        InvestModel investModel = this.getFakeInvestModel();
        investModel.setStatus(InvestStatus.WAITING);
        investMapper.create(investModel);

        investMapper.updateStatus(investModel.getId(), InvestStatus.SUCCESS);
        InvestModel investModel1 = investMapper.findById(investModel.getId());
        assertEquals(investModel1.getStatus(), InvestStatus.SUCCESS);

        investMapper.updateStatus(investModel.getId(), InvestStatus.FAIL);
        InvestModel investModel2 = investMapper.findById(investModel.getId());
        assertEquals(investModel2.getStatus(), InvestStatus.FAIL);
    }

    @Test
    public void shouldFindByCorrectSortStyle(){
        createTestInvests();

        List<InvestModel> investModels1 = investMapper.findByLoanIdOrderByTime(Loan_ID2,SortStyle.Asc);
        assertEquals(investModels1.size(), 5);
        assert investModels1.get(0).getCreatedTime().before(investModels1.get(1).getCreatedTime());


        List<InvestModel> investModels2 = investMapper.findByLoginNameOrderByTime(User_ID2, SortStyle.Desc);
        assertEquals(investModels2.size(), 5);
        assert investModels2.get(4).getCreatedTime().before(investModels2.get(2).getCreatedTime());
    }

    @Test
    public void shouldGetCorrectAmountTotal(){
        createTestInvests();
        long amountTotal = investMapper.sumSuccessInvestAmount(Loan_ID);
        assertEquals(amountTotal, 1000000*5);
    }

    private void createTestInvests(){
        for(int i=0;i<10;i++) {
            InvestModel investModel = this.getFakeInvestModel();
            if(i<5) {
                investModel.setLoanId(Loan_ID2);
            }
            if(i%2 == 1){
                investModel.setLoginName(User_ID2);
            }
            investModel.setCreatedTime(DateUtils.addHours(new Date(), -i));
            investMapper.create(investModel);
        }
    }

    private InvestModel getFakeInvestModel() {
        InvestModel model = new InvestModel();
        model.setAmount(1000000);
        // 舍弃毫秒数
        Date currentDate = new Date((new Date().getTime()/1000)*1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(User_ID);
        model.setLoanId(Loan_ID);
        model.setSource(InvestSource.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }

    @Before
    public void createLoan() {
        createLoanByUserId(User_ID,Loan_ID);
        createLoanByUserId(User_ID2,Loan_ID2);
        assertNotNull(loanMapper.findById(Loan_ID));
    }

    private void createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
    }

    @Before
    public void createUser() throws Exception {
        createUserByUserId(User_ID);
        createUserByUserId(User_ID2);

        UserModel userModel = userMapper.findByLoginName(User_ID);
        assertNotNull(userModel);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }
}
