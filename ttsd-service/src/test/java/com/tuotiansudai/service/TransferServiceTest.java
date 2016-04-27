package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class TransferServiceTest {
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;


    public static final long loanId = 10000000001L;
    public static final long investId = 10000000002L;

    public static final long  transferSuccessloanId = 20000000001L;
    public static final long transferSuccessInvestId = 20000000002L;
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
        loanDto.setPeriods(5);
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

    private TransferApplicationModel createTransferApplicationModel(long loanId,long investId, TransferStatus transferStatus) {
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setId(1000009);
        transferApplicationModel.setName("ZR00001");
        transferApplicationModel.setLoanId(loanId);
        transferApplicationModel.setTransferInvestId(investId);
        transferApplicationModel.setInvestId(investId);
        transferApplicationModel.setPeriod(2);
        transferApplicationModel.setLoginName("testuser");
        transferApplicationModel.setInvestAmount(100000L);
        transferApplicationModel.setTransferAmount(90000L);
        transferApplicationModel.setTransferFee(10L);
        transferApplicationModel.setStatus(transferStatus);
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setTransferTime(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationModel.setLeftPeriod(4);
        transferApplicationMapper.create(transferApplicationModel);
        return transferApplicationModel;
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

    private void createInvests(String loginName, long loanId, long investId) {
        InvestModel model = new InvestModel(investId, loanId, null, 1, loginName, Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
    }

    private void createInvestsTransferSuccess(String loginName, long loanId, long investId) {
        InvestModel model = new InvestModel(investId, loanId, null, 1, loginName, Source.WEB, null);
        model.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(model);
    }

    private void createInvestRepay(long investId) {
        InvestRepayModel model1 = new InvestRepayModel(idGenerator.generate(), investId, 1, 0, 100, 10, strToDate("2016-02-29 23:59:59"), RepayStatus.COMPLETE);
        InvestRepayModel model2 = new InvestRepayModel(idGenerator.generate(), investId, 2, 0, 100, 10, strToDate("2016-03-29 23:59:59"), RepayStatus.REPAYING);
        InvestRepayModel model3 = new InvestRepayModel(idGenerator.generate(), investId, 3, 0, 100, 10, strToDate("2016-04-29 23:59:59"), RepayStatus.REPAYING);
        InvestRepayModel model4 = new InvestRepayModel(idGenerator.generate(), investId, 4, 0, 100, 10, strToDate("2016-05-29 23:59:59"), RepayStatus.REPAYING);
        InvestRepayModel model5 = new InvestRepayModel(idGenerator.generate(), investId, 5, 10000, 100, 10, strToDate("2016-06-29 23:59:59"), RepayStatus.REPAYING);
        List<InvestRepayModel> investRepayModels = new ArrayList<InvestRepayModel>();
        investRepayModels.add(model1);
        investRepayModels.add(model2);
        investRepayModels.add(model3);
        investRepayModels.add(model4);
        investRepayModels.add(model5);
        investRepayMapper.create(investRepayModels);
    }

    private  Date strToDate(String strDate){
        try {
            return DateUtils.parseDate(strDate, new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (ParseException e) {
           e.printStackTrace();
        }
        return null;
    }

    private void createAccount(String loginName) {
        AccountModel accountModel = new AccountModel(loginName, "aaa", "372930198709118793", "2", "2", new Date());
        accountModel.setBalance(1000);
        accountMapper.create(accountModel);
    }

    @Before
    public void setup() throws Exception {
        createUserByUserId("testuser");
        createAccount("testuser");

    }

    @Test
    public void shouldFindAllTransferApplicationPaginationListIsTest(){
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId, investId);
        createInvestRepay(investId);
        createTransferApplicationModel(loanId, investId,TransferStatus.TRANSFERRING);
        List<TransferStatus> transferStatuses = new ArrayList<TransferStatus>();
        transferStatuses.add(TransferStatus.TRANSFERRING);
        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = transferService.findAllTransferApplicationPaginationList(transferStatuses, 0.15, 0.18, 1, 10);

        assertThat(basePaginationDataDto.getRecords().size(), is(1));
    }

    @Test
    public void shouldGetTransferApplicationDetailDtoIsOk(){
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId, investId);
        createInvestRepay(investId);
        TransferApplicationModel transferApplicationModel = createTransferApplicationModel(loanId, investId, TransferStatus.TRANSFERRING);
        TransferApplicationDetailDto  transferApplicationDetailDto = transferService.getTransferApplicationDetailDto(transferApplicationModel.getId(), "testuser");

        assertThat(transferApplicationDetailDto.getDueDate(), is(strToDate("2016-06-29 23:59:59")));
        assertThat(transferApplicationDetailDto.getNextRefundDate(), is(strToDate("2016-03-29 23:59:59")));
        assertThat(transferApplicationDetailDto.getTransferAmount(), is("900.00"));
        assertThat(transferApplicationDetailDto.getExpecedInterest(), is("103.60"));
        assertThat(transferApplicationDetailDto.getInvestAmount(), is("1000.00"));
        assertThat(transferApplicationDetailDto.getExpecedInterest(), is("103.60"));
        assertThat(transferApplicationDetailDto.getNextExpecedInterest(), is("0.90"));
        assertThat(transferApplicationDetailDto.getBalance(), is("10.00"));
    }

    @Test
    public void shouldTransferApplicationRecodesDtoIsNoRecodes(){
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId, investId);
        createInvestRepay(investId);
        TransferApplicationModel transferApplicationModel = createTransferApplicationModel(loanId, investId,TransferStatus.TRANSFERRING);

        TransferApplicationRecodesDto transferApplicationRecodesDto = transferService.getTransferee(transferApplicationModel.getId(), "testuser");

        assertThat(transferApplicationRecodesDto.getStatus(), is(false));
    }

    @Test
    public void shouldTransferApplicationRecodesDtoIsHasRecords(){
        createLoanByUserId("testuser", transferSuccessloanId);
        createInvestsTransferSuccess("testuser",transferSuccessloanId, transferSuccessInvestId);
        createInvestRepay(transferSuccessInvestId);
        TransferApplicationModel transferApplicationModel = createTransferApplicationModel(transferSuccessloanId, transferSuccessInvestId, TransferStatus.SUCCESS);

        TransferApplicationRecodesDto transferApplicationRecodesDto = transferService.getTransferee(transferApplicationModel.getId(), "");

        assertThat(transferApplicationRecodesDto.getStatus(), is(true));
        assertThat(transferApplicationRecodesDto.getReceiveAmount(), is("900.00"));
        assertThat(transferApplicationRecodesDto.getTransferApplicationReceiver(), is("tes******"));
        assertThat(transferApplicationRecodesDto.getExpecedInterest(), is("103.60"));
        assertThat(transferApplicationRecodesDto.getSource(), is(Source.WEB));
        assertThat(transferApplicationRecodesDto.getInvestAmount(), is("1000.00"));
    }

}
