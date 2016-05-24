package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.security.MyUser;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
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

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanDetailServiceTest {

    @Autowired
    private LoanDetailService loanDetailService;

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
    private UserMapper userMapper;

    @Test
    public void shouldGetTheInvests(){
        long loanId = createLoanService();
        createTestInvests(loanId, "loginName", 10);
        BaseDto<BasePaginationDataDto> baseDto = loanDetailService.getInvests(null, loanId, 1, 5);
        assertEquals(5, baseDto.getData().getRecords().size());
    }

    @Test
    public void shouldGetTheInvestsAndNextPagePreviousPage(){
        String mockUserName = "loginUser";
        createMockUser(mockUserName);
        long loanId = createLoanService();
        // 虽然这里创建了10条投资记录，但是在上个方法里，已经创建了三条投资记录，其中已经包含了一个success的记录
        createTestInvests(loanId, mockUserName, 10);

        BaseDto<BasePaginationDataDto> baseDto = loanDetailService.getInvests(null, loanId, 1, 3);
        assertEquals(3, baseDto.getData().getRecords().size());
        assertEquals(11, baseDto.getData().getCount());

        baseDto = loanDetailService.getInvests(null, loanId, 4, 3);
        BasePaginationDataDto data = baseDto.getData();
        assertEquals(2, data.getRecords().size());
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
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName(fakeUserName);
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
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
            loanTitleRelationModel.setApplicationMaterialUrls("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModelList);

        InvestModel investModel1 = getFakeInvestModel(id, fakeUserName2);
        investModel1.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel2 = getFakeInvestModel(id, fakeUserName2);
        investModel2.setStatus(InvestStatus.FAIL);

        InvestModel investModel3 = getFakeInvestModel(id, fakeUserName2);
        investModel3.setStatus(InvestStatus.WAIT_PAY);

        investMapper.create(investModel1);
        investMapper.create(investModel2);
        investMapper.create(investModel3);

        return id;

    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        return new InvestModel(idGenerator.generate(), loanId, null, 50, loginName, null, Source.WEB, null);
    }

    private void createMockUser(String loginName){
        UserModel um = getFakeUser(loginName);
        userMapper.create(um);
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

}
