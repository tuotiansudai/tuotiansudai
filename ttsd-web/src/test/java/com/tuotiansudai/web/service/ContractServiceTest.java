package com.tuotiansudai.web.service;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertFalse;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ContractServiceTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private ContractService contractService;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void shouldGenerateTransferContractIsOk() throws ParseException {
        UserModel userModel = getUserModel();
        userMapper.create(userModel);
        LoanModel loanModel = getLoanModel();
        loanMapper.create(loanModel);
        InvestModel investModel = getInvest(loanModel.getId());
        investMapper.create(investModel);
        TransferApplicationModel transferApplicationModel = getTransferApplicationModel(loanModel.getId(), investModel.getId());
        transferApplicationMapper.create(transferApplicationModel);
        AccountModel accountModel = getAccountModel();
        accountMapper.create(accountModel);


        String pdfStr = contractService.generateTransferContract(transferApplicationModel.getId());
        if(pdfStr.indexOf("baisong") == -1){
            assertFalse(true);
        }
        if(pdfStr.indexOf("5") == -1){
            assertFalse(true);
        }

        if(pdfStr.indexOf("0.5") == -1){
            assertFalse(true);
        }
    }

    @Test
    public void shouldGenerateTransferAgreementIsOk(){
        String pdfStr = contractService.generateTransferAgreement();
        if(pdfStr.indexOf("201601") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("张三") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("zhangsan") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("37040319214531243X") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("赵四") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("zhaosi") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("37020319341204601X") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("5") == -1){ assertFalse(true);}
        if(pdfStr.indexOf("0.5") == -1){ assertFalse(true);}

    }

    public LoanModel getLoanModel() throws ParseException {
        LoanModel lm = new LoanModel();
        lm.setId(idGenerator.generate());
        lm.setName("12标的");
        lm.setLoanerUserName("baisong");
        lm.setAgentLoginName("baisong");
        lm.setLoanerLoginName("baisong");
        lm.setLoanerLoginName("baisong");
        lm.setLoanerIdentityNumber("22012219881003356X");
        lm.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        lm.setPeriods(3);
        lm.setDescriptionText("123");
        lm.setDescriptionHtml("<p>123</p>");
        lm.setLoanAmount(1300);
        lm.setInvestFeeRate(0.1);
        lm.setMinInvestAmount(100);
        lm.setMaxInvestAmount(1300);
        lm.setInvestIncreasingAmount(1);
        lm.setActivityType(ActivityType.NORMAL);
        lm.setProductType(ProductType.WYX);
        lm.setBaseRate(0.12);
        lm.setActivityRate(0);
        lm.setContractId(789098123);
        lm.setFundraisingStartTime(df.parse("2016-03-09 12:00:00"));
        lm.setFundraisingEndTime(df.parse("2016-03-15 12:00:00"));
        lm.setVerifyTime(df.parse("2016-03-09 11:46:18"));
        lm.setVerifyLoginName("baisong");
        lm.setStatus(LoanStatus.RECHECK);
        lm.setShowOnHome(false);
        lm.setCreatedTime(df.parse("2016-03-09 11:46:05"));
        lm.setCreatedLoginName("baisong");
        lm.setUpdateTime(df.parse("2016-03-09 11:46:18"));
        return lm;
    }

    public InvestModel getInvest(long loanId) throws ParseException {
        InvestModel im = new InvestModel();
        im.setId(idGenerator.generate());
        im.setLoginName("baisong");
        im.setLoanId(loanId);
        im.setAmount(2577);
        im.setStatus(InvestStatus.SUCCESS);
        im.setTransferStatus(TransferStatus.NONTRANSFERABLE);
        im.setSource(Source.ANDROID);
        im.setIsAutoInvest(false);
        im.setCreatedTime(df.parse("2016-03-09 17:52:38"));
        im.setChannel("tuotiansudai");
        return im;
    }

    public UserModel getUserModel() throws ParseException {
        UserModel um = new UserModel();
        um.setId(idGenerator.generate());
        um.setLoginName("baisong");
        um.setPassword("1234567");
        um.setMobile("1823123123");
        um.setRegisterTime(df.parse("2016-03-08 16:27:46"));
        um.setLastModifiedTime(df.parse("2016-03-09 17:06:57"));
        um.setLastModifiedUser("baisong");
        um.setStatus(UserStatus.ACTIVE);
        um.setSalt("12313");
        um.setChannel("123");
        um.setProvince("北京");
        um.setCity("北京");
        um.setSource(Source.WEB);
        return um;
    }

    public TransferApplicationModel getTransferApplicationModel(long loanId,long investId) throws ParseException {
        TransferApplicationModel al = new TransferApplicationModel();
        al.setId(idGenerator.generate());
        al.setName("测试");
        al.setLoanId(loanId);
        al.setTransferInvestId(investId);
        al.setInvestId(investId);
        al.setPeriod(2);
        al.setLeftPeriod(1);
        al.setLoginName("baisong");
        al.setInvestAmount(100);
        al.setTransferAmount(100);
        al.setTransferFee(1);
        al.setStatus(TransferStatus.SUCCESS);
        al.setDeadline(df.parse("2014-11-10 00:00:00"));
        al.setTransferTime(df.parse("2014-11-10 00:00:00"));
        al.setApplicationTime(df.parse("2014-11-10 00:00:00"));
        return al;
    }

    public AccountModel getAccountModel(){
        AccountModel accountModel = new AccountModel("baisong", "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        return accountModel;
    }

    public TransferRuleModel getTransferRuleModel(){
        TransferRuleModel tr = new TransferRuleModel();
        tr.setId(idGenerator.generate());
        tr.setLevelOneFee(0.01);
        tr.setLevelOneLower(1);
        tr.setLevelOneUpper(29);
        tr.setLevelTwoFee(0.005);
        tr.setLevelTwoLower(30);
        tr.setLevelTwoUpper(90);
        tr.setLevelThreeFee(0);
        tr.setLevelThreeLower(91);
        tr.setLevelThreeUpper(365);
        tr.setDiscount(0.005);
        tr.setDaysLimit(5);
        return tr;
    }
}