package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v3_0.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.LoanResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppLoanListV3Service;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MobileAppLoanListV3ServiceTest extends ServiceTestBase {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private MobileAppLoanListV3Service mobileAppLoanListV3Service;

    private UserModel createUser(String loginName) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("123abc");
        userModel.setEmail("12345@abc.com");
        userModel.setMobile(String.valueOf(idGenerator.generate()));
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModel);

        return userModel;
    }

    private LoanModel createLoan(String loanerLoginName, ActivityType activityType, ProductType productType, LoanStatus loanStatus, String raisingCompleteTime) {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(idGenerator.generate());
        loanModel.setName(loanerLoginName);
        loanModel.setLoanerLoginName(loanerLoginName);
        loanModel.setLoanerUserName(loanerLoginName);
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setAgentLoginName(loanerLoginName);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setPeriods(productType.getPeriods());
        loanModel.setStatus(loanStatus);
        loanModel.setActivityType(activityType);
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setCreatedTime(new Date());
        loanModel.setProductType(productType);
        loanModel.setActivityType(activityType);
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanModel.setRaisingCompleteTime(DateTime.parse(raisingCompleteTime).toDate());
        loanModel.setRecheckTime(new Date());
        loanMapper.create(loanModel);
        loanMapper.update(loanModel);

        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "", "MOBILE,WEB", "");
        loanDetailsMapper.create(loanDetailsModel);

        return loanModel;
    }

    private InvestModel getInvestModel(String loginName, long loanId) {
        InvestModel investModel = new InvestModel(idGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null, 0.1);
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);
        return investModel;
    }

    @Test
    public void testGenerateIndexLoan() throws Exception {
        UserModel user = createUser("testUser");
        createUser("loaner");
        //数据库中默认有新手体验标
        final long experienceLoanId = 1L;
        //没有投资过的
        BaseResponseDto baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        LoanListResponseDataDto loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        LoanResponseDataDto loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType.EXPERIENCE.name(), loanResponseDataDto.getProductNewType());
        assertEquals(ActivityType.NEWBIE.name(), loanResponseDataDto.getActivityType());
        //参数为空
        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(null);
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType.EXPERIENCE.name(), loanResponseDataDto.getProductNewType());
        assertEquals(ActivityType.NEWBIE.name(), loanResponseDataDto.getActivityType());

        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan("");
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType.EXPERIENCE.name(), loanResponseDataDto.getProductNewType());
        assertEquals(ActivityType.NEWBIE.name(), loanResponseDataDto.getActivityType());
        //没有可投标 && 只投资过体验标
        createLoan("loaner", ActivityType.NORMAL, ProductType._30, LoanStatus.COMPLETE, "2010-06-30T01:20");
        createLoan("loaner", ActivityType.NORMAL, ProductType._90, LoanStatus.COMPLETE, "2010-07-30T01:20");
        createLoan("loaner", ActivityType.NORMAL, ProductType._180, LoanStatus.COMPLETE, "2010-08-30T01:20");
        LoanModel loanModel = createLoan("loaner", ActivityType.NORMAL, ProductType._360, LoanStatus.COMPLETE, "2010-09-30T01:20");

        InvestModel investModel = getInvestModel(user.getLoginName(), experienceLoanId);
        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType._360.name(), loanResponseDataDto.getProductNewType());
        assertEquals(LoanStatus.COMPLETE.name().toLowerCase(), loanResponseDataDto.getLoanStatus());
        //没有可投标 && 投资过其它标
        investModel.setLoanId(loanModel.getId());
        investMapper.update(investModel);
        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType._360.name(), loanResponseDataDto.getProductNewType());
        assertEquals(LoanStatus.COMPLETE.name().toLowerCase(), loanResponseDataDto.getLoanStatus());
        //有可投标 && 投资过其它标
        createLoan("loaner", ActivityType.NORMAL, ProductType._30, LoanStatus.RAISING, "2010-06-30T01:20");
        createLoan("loaner", ActivityType.NORMAL, ProductType._90, LoanStatus.RAISING, "2010-07-30T01:20");
        createLoan("loaner", ActivityType.NORMAL, ProductType._180, LoanStatus.RAISING, "2010-08-30T01:20");

        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType._180.name(), loanResponseDataDto.getProductNewType());
        assertEquals(LoanStatus.RAISING.name().toLowerCase(), loanResponseDataDto.getLoanStatus());
        //有可投标 && 只投资过体验标
        investModel.setLoanId(experienceLoanId);
        investMapper.update(investModel);

        baseResponseDto = mobileAppLoanListV3Service.generateIndexLoan(user.getLoginName());
        loanListResponseDataDto = (LoanListResponseDataDto) baseResponseDto.getData();
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ReturnMessage.SUCCESS.getMsg(), baseResponseDto.getMessage());
        loanResponseDataDto = loanListResponseDataDto.getLoanList().get(0);
        assertEquals(ProductType._30.name(), loanResponseDataDto.getProductNewType());
        assertEquals(LoanStatus.RAISING.name().toLowerCase(), loanResponseDataDto.getLoanStatus());
    }
}
