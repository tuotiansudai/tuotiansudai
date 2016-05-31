package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@Transactional
public class PointTaskServiceTest {

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldGetEachSumInvestTaskLevelOf0IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,100);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findByLoginNameAndIdAndTaskLevel(loginName,pointTaskModel.getId(),0);
        assertTrue(count == 0);
    }

    @Test
    public void shouldGetEachSumInvestTaskLevelOf1IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,500000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 1);
    }

    @Test
    public void shouldGetEachSumInvestTaskLevelOf2IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,1000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 2);
    }

    @Test
    public void shouldGetEachSumInvestTaskLevelOf3IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,5000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 3);
    }

    @Test
    public void shouldGetEachSumInvestTaskLevelOf4IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,10000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 4);
    }

    @Test
    public void shouldGetEachSumInvestTaskLevelOf5IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,50000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 5);
    }

    @Test
    public void shouldGetEachSumInvestTaskLevelOf6IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,100000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 6);
    }

    @Test
    public void shouldGetEachSumInvestTaskLevelOf7IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,250000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.EACH_SUM_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 7);
    }

    @Test
    public void shouldGetFirstSingleInvestTaskLevelOf0IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,100000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.FIRST_SINGLE_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 0);
    }

    @Test
    public void shouldGetFirstSingleInvestTaskLevelOf1IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,1000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.FIRST_SINGLE_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 1);
    }

    @Test
    public void shouldGetFirstSingleInvestTaskLevelOf2IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,1000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.FIRST_SINGLE_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 2);
    }

    @Test
    public void shouldGetFirstSingleInvestTaskLevelOf3IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,1000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.FIRST_SINGLE_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 3);
    }

    @Test
    public void shouldGetFirstSingleInvestTaskLevelOf4IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,1000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.FIRST_SINGLE_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 4);
    }

    @Test
    public void shouldGetFirstSingleInvestTaskLevelOf5IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,1000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.FIRST_SINGLE_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 5);
    }

    @Test
    public void shouldGetFirstSingleInvestTaskLevelOf6IsOk(){
        long loanId = this.idGenerator.generate();
        String loginName = "investor";
        InvestModel investModel = createData(loginName,loanId,1000000);
        investService.investSuccess(investModel);
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(PointTask.FIRST_SINGLE_INVEST);
        long count = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName,pointTaskModel.getId());
        assertTrue(count == 6);
    }


    public InvestModel createData(String loginName,long loanId,long amount){
        this.createUserByUserId(loginName);
        AccountModel investorAccountModel = createAccountByUserId(loginName);
        accountMapper.create(investorAccountModel);
        LoanModel loanModel = new LoanModel(getLoanDto(loanId));
        loanModel.setAgentLoginName(loginName);
        loanMapper.create(loanModel);
        InvestModel investModel = new InvestModel();
        investModel.setId(idGenerator.generate());
        investModel.setLoanId(loanId);
        investModel.setLoginName(loginName);
        investModel.setAmount(amount);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.SUCCESS);
        investModel.setSource(Source.IOS);
        investMapper.create(investModel);
        return investModel;
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

    private AccountModel createAccountByUserId(String userId) {
        AccountModel accountModel = new AccountModel(userId, userId, "120101198810012010", "", "", new Date());
        accountModel.setAutoInvest(true);
        accountModel.setBalance(10000);
        accountModel.setFreeze(10000);
        return accountModel;
    }

    public LoanDto getLoanDto(long loanId){
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("loaner");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("id");
        loanDto.setAgentLoginName("loaner");
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(1);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("1000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        loanDto.setProductType(ProductType._30);
        return loanDto;
    }

}
