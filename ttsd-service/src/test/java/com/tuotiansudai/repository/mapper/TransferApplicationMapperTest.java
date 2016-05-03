package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class TransferApplicationMapperTest {
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private InvestMapper investMapper;

    @Test
    public void shouldFindTransferApplicationListIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel transferrerModel = createUserByUserId("transferrerTestuser");
        UserModel transfereeModel = createUserByUserId("transfereeTestUser");
        LoanModel loanModel = createLoanByUserId("transferrerTestUser", loanId);
        InvestModel transferrerInvestModel = createInvest(transferrerModel.getLoginName(), loanId);
        InvestModel transfereeInvestModel = createInvest(transfereeModel.getLoginName(), loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(transferrerModel.getLoginName());
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.TRANSFERRING);
        transferApplicationModel.setLoanId(loanModel.getId());
        transferApplicationModel.setTransferInvestId(transferrerInvestModel.getId());
        transferApplicationModel.setInvestId(transfereeInvestModel.getId());
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);

        List<TransferApplicationRecordDto> transferApplicationRecordDto = transferApplicationMapper.findTransferApplicationPaginationList(null, null,null,null,null,null,null,0,10);

        assertNotNull(transferApplicationRecordDto.get(0));
        assertEquals("name", transferApplicationRecordDto.get(0).getName());
        assertEquals(1000, transferApplicationRecordDto.get(0).getTransferAmount());
        assertEquals(1200, transferApplicationRecordDto.get(0).getInvestAmount());
        assertEquals(new DateTime("2016-01-02").toDate(), transferApplicationRecordDto.get(0).getTransferTime());
        assertEquals(TransferStatus.TRANSFERRING,transferApplicationRecordDto.get(0).getTransferStatus());
        assertEquals(transfereeInvestModel.getLoginName(),transferApplicationRecordDto.get(0).getTransfereeLoginName());
        assertEquals(transferrerInvestModel.getLoginName(),transferApplicationRecordDto.get(0).getTransferrerLoginName());
    }
    @Test
    public void shouldFindTransferApplicationPaginationByLoginNameIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(userModel.getLoginName());
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.TRANSFERRING);
        transferApplicationModel.setLoanId(loanModel.getId());
        transferApplicationModel.setTransferInvestId(investModel.getId());
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);

        List<TransferApplicationRecordDto> transferApplicationRecordDto = transferApplicationMapper.findTransferApplicationPaginationByLoginName(userModel.getLoginName(), Lists.newArrayList(TransferStatus.TRANSFERRING), 0, 10);
        System.out.println(transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(userModel.getLoginName(), Lists.newArrayList(TransferStatus.TRANSFERRING)));
        assertNotNull(transferApplicationRecordDto.get(0));
        assertEquals("name", transferApplicationRecordDto.get(0).getName());
        assertEquals(1000,transferApplicationRecordDto.get(0).getTransferAmount());
        assertEquals(1200,transferApplicationRecordDto.get(0).getInvestAmount());
        assertEquals(new DateTime("2016-01-02").toDate(),transferApplicationRecordDto.get(0).getTransferTime());
        assertEquals(0.12d,transferApplicationRecordDto.get(0).getActivityRate(),0);
        assertEquals(0.16d,transferApplicationRecordDto.get(0).getBaseRate(),0);
        assertEquals(TransferStatus.TRANSFERRING,transferApplicationRecordDto.get(0).getTransferStatus());

    }


    @Test
    public void shouldFindCountTransferApplicationPaginationIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel transferModel = createUserByUserId("transfer");
        UserModel transfereeModel = createUserByUserId("transferee");
        LoanModel loanModel = createLoanByUserId("transfer", loanId);
        InvestModel transferInvestModel = createInvest("transfer", loanId);
        InvestModel transfereeInvestModel = createInvest("transferee", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(transferModel.getLoginName());
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setLoanId(loanModel.getId());
        transferApplicationModel.setInvestId(transfereeInvestModel.getId());
        transferApplicationModel.setTransferInvestId(transferInvestModel.getId());
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationModel.setTransferFee(1300l);
        transferApplicationMapper.create(transferApplicationModel);

        List<TransferApplicationRecordDto> transferApplicationRecordDto = transferApplicationMapper.findTransferApplicationPaginationList(null, null, null, null, null, transfereeInvestModel.getLoginName(), null, 0, 1);
        assertNotNull(transferApplicationRecordDto.get(0));
        assertEquals("name", transferApplicationRecordDto.get(0).getName());
        assertEquals(1000, transferApplicationRecordDto.get(0).getTransferAmount());
        assertEquals(1200, transferApplicationRecordDto.get(0).getInvestAmount());
        assertEquals(new DateTime("2016-01-02").toDate(), transferApplicationRecordDto.get(0).getTransferTime());
        assertEquals(TransferStatus.SUCCESS,transferApplicationRecordDto.get(0).getTransferStatus());
        assertEquals(new Long(transferInvestModel.getId()),transferApplicationRecordDto.get(0).getTransferInvestId());
        assertEquals(transferModel.getLoginName(),transferApplicationRecordDto.get(0).getTransferrerLoginName());
        assertEquals(transfereeModel.getLoginName(),transferApplicationRecordDto.get(0).getTransfereeLoginName());
        assertEquals(1300,transferApplicationRecordDto.get(0).getTransferFee());

    }
    @Test
    public void shouldFindTransfereeApplicationPaginationByLoginNameIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel transferModel = createUserByUserId("transfer");
        UserModel transfereeModel = createUserByUserId("transferee");
        LoanModel loanModel = createLoanByUserId("transfer", loanId);
        InvestModel transferInvestModel = createInvest("transfer", loanId);
        InvestModel transfereeInvestModel = createInvest("transferee", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(transferModel.getLoginName());
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setLoanId(loanModel.getId());
        transferApplicationModel.setInvestId(transfereeInvestModel.getId());
        transferApplicationModel.setTransferInvestId(transferInvestModel.getId());
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);

        List<TransferApplicationRecordDto> transferApplicationRecordDto = transferApplicationMapper.findTransfereeApplicationPaginationByLoginName(transfereeModel.getLoginName(),0,10);

        assertNotNull(transferApplicationRecordDto.get(0));
        assertEquals("name", transferApplicationRecordDto.get(0).getName());
        assertEquals(1000,transferApplicationRecordDto.get(0).getTransferAmount());
        assertEquals(1200,transferApplicationRecordDto.get(0).getInvestAmount());
        assertEquals(new DateTime("2016-01-02").toDate(),transferApplicationRecordDto.get(0).getTransferTime());
        assertEquals(0.12d,transferApplicationRecordDto.get(0).getActivityRate(),0);
        assertEquals(0.16d,transferApplicationRecordDto.get(0).getBaseRate(),0);
        assertEquals(TransferStatus.SUCCESS,transferApplicationRecordDto.get(0).getTransferStatus());

    }

    @Test
    public void shouldFindCountTransfereeApplicationPaginationByLoginNameIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel transferModel = createUserByUserId("transfer");
        UserModel transfereeModel = createUserByUserId("transferee");
        LoanModel loanModel = createLoanByUserId("transfer", loanId);
        InvestModel transferInvestModel = createInvest("transfer", loanId);
        InvestModel transfereeInvestModel = createInvest("transferee", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(transferModel.getLoginName());
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setLoanId(loanModel.getId());
        transferApplicationModel.setInvestId(transfereeInvestModel.getId());
        transferApplicationModel.setTransferInvestId(transferInvestModel.getId());
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);

        int count = transferApplicationMapper.findCountTransfereeApplicationPaginationByLoginName(transfereeModel.getLoginName());

        assertEquals(1,count);


    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1, loginName, Source.WEB, null);
        model.setCreatedTime(new Date());
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
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
    private LoanModel createLoanByUserId(String userId, long loanId) {
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
        loanDto.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        return loanModel;
    }

}
