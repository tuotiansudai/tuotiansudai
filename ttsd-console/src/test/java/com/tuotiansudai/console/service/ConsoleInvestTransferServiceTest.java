package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ConsoleInvestTransferServiceTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private ConsoleInvestTransferService consoleInvestTransferService;

    private LoanModel createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setAgentLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(6);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        loanDto.setRecheckTime(new Date());
        loanDto.setProductType(ProductType._180);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
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

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, loginName, 1, 0.1, false, new Date(), Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    @Test
    public void shouldFindTransferApplicationPaginationListIsSuccess() {
        long loanId = IdGenerator.generate();
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

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = consoleInvestTransferService.findTransferApplicationPaginationList(null, null, null, null, null, transfereeModel.getMobile(), null, null, 1, 10);

        assertTrue(basePaginationDataDto.getStatus());
        assertNotNull(basePaginationDataDto.getRecords().get(0));
        assertEquals(1, basePaginationDataDto.getIndex());
        assertEquals(10, basePaginationDataDto.getPageSize());
        assertEquals(1, basePaginationDataDto.getCount());
        assertEquals("10.00", basePaginationDataDto.getRecords().get(0).getTransferAmount());
        assertEquals("12.00", basePaginationDataDto.getRecords().get(0).getInvestAmount());
        assertEquals(new DateTime("2016-01-02").toDate(), basePaginationDataDto.getRecords().get(0).getTransferTime());
        assertEquals("TRANSFERRING", basePaginationDataDto.getRecords().get(0).getTransferStatus());
        assertEquals(transfereeModel.getMobile(), basePaginationDataDto.getRecords().get(0).getTransfereeMobile());
        assertEquals(transferrerModel.getMobile(), basePaginationDataDto.getRecords().get(0).getTransferrerMobile());
    }
}
