package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.LoanResponseDataDto;
import com.tuotiansudai.api.service.v2_0.impl.MobileAppLoanListV2ServiceImpl;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppLoanListV2ServiceTest extends ServiceTestBase{

    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private MobileAppLoanListV2ServiceImpl mobileAppLoanListV2Service;
    @Autowired
    private UserMapper userMapper;
    @Mock
    private UserMembershipEvaluator userMembershipEvaluator;
    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;
    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Test
    public void shouldNoLoginNameGenerateIndexLoanIsOk(){
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName,ActivityType.NORMAL,ProductType._90,LoanStatus.RAISING);
        loanMapper.create(loanModel);
        InvestModel investModel = getInvestModel(loginName,loanModel.getId());
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName,ActivityType.NEWBIE,ProductType.EXPERIENCE,LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(null);
        assertTrue(dto.getData().getLoanList().get(0).getProductNewType().equals("EXPERIENCE"));
    }

    @Test
    public void shouldLoginInvestExperienceGenerateIndexLoanIsOk(){
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName,ActivityType.NORMAL,ProductType._90,LoanStatus.RAISING);
        loanMapper.create(loanModel);
        InvestModel investModel = getInvestModel(loginName, loanModel.getId());
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType.EXPERIENCE, LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        InvestModel investModel1 = getInvestModel(loginName, loanModel1.getId());
        investMapper.create(investModel1);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(loginName);
        assertTrue(!dto.getData().getLoanList().get(0).getProductNewType().equals("EXPERIENCE"));
        assertTrue(!dto.getData().getLoanList().get(0).getActivityType().equals("NEWBIE"));
    }

    @Test
    public void shouldLoginInvestNewBieGenerateIndexLoanIsOk(){
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName, ActivityType.NORMAL, ProductType._90, LoanStatus.RAISING);
        loanMapper.create(loanModel);
        InvestModel investModel = getInvestModel(loginName, loanModel.getId());
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType._30, LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        InvestModel investModel1 = getInvestModel(loginName, loanModel1.getId());
        investModel1.setInvestTime(DateTime.parse("2016-06-12").toDate());
        investMapper.create(investModel1);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(loginName);
        assertTrue(!dto.getData().getLoanList().get(0).getProductNewType().equals("NEWBIE"));
    }

    @Test
    public void shouldLoginNormalGenerateIndexLoanIsOk(){
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName,ActivityType.NORMAL,ProductType._90,LoanStatus.RAISING);
        loanMapper.create(loanModel);
        InvestModel investModel = getInvestModel(loginName, loanModel.getId());
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType._30, LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        InvestModel investModel1 = getInvestModel(loginName, loanModel1.getId());
        investModel1.setInvestTime(DateTime.parse("2016-06-12").toDate());
        investMapper.create(investModel1);
        LoanModel loanModel2 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType.EXPERIENCE, LoanStatus.RAISING);
        loanMapper.create(loanModel2);
        InvestModel investModel2 = getInvestModel(loginName,loanModel1.getId());
        investMapper.create(investModel2);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(loginName);
        assertTrue(dto.getData().getLoanList().get(0).getActivityType().equals("NORMAL"));
    }

    @Test
    public void shouldValidInterestPerTenThousandsIsOk(){
        String loginName = "testExtraRate";
        long loanId = idGenerator.generate();
        UserModel userModel = getUserModelTest(loginName);
        userMapper.create(userModel);
        LoanModel loanModel = createLoanByUserId(loginName, loanId);
        List<ExtraLoanRateModel> extraLoanRateModels = createExtraLoanRate(loanId);
        extraLoanRateMapper.create(extraLoanRateModels);

        loanDetailsMapper.create(createLoanDetails(loanId));

        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(loginName);

        List<LoanResponseDataDto> loanList = dto.getData().getLoanList();
        assertTrue(CollectionUtils.isNotEmpty(loanList));
        Optional<LoanResponseDataDto> first = loanList.stream()
                .filter(loanResponseDataDto -> Long.parseLong(loanResponseDataDto.getLoanId()) == loanModel.getId())
                .findFirst();

        assertTrue(Long.parseLong(first.get().getInterestPerTenThousands()) == 42904l);
    }

    private LoanDetailsModel createLoanDetails(long loanId){
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setId(idGenerator.generate());
        loanDetailsModel.setDeclaration("声明材料");
        loanDetailsModel.setExtraSource(Lists.newArrayList(Source.WEB));
        loanDetailsModel.setLoanId(loanId);
        return loanDetailsModel;
    }

    private List<ExtraLoanRateModel> createExtraLoanRate(long loanId) {
        ExtraLoanRateModel model = new ExtraLoanRateModel();
        model.setLoanId(loanId);
        model.setExtraRateRuleId(100001);
        model.setCreatedTime(new Date());
        model.setMinInvestAmount(100000);
        model.setMaxInvestAmount(1000000);
        model.setRate(0.1);
        ExtraLoanRateModel model2 = new ExtraLoanRateModel();
        model2.setLoanId(loanId);
        model2.setExtraRateRuleId(100001);
        model2.setCreatedTime(new Date());
        model2.setMinInvestAmount(1000000);
        model2.setMaxInvestAmount(5000000);
        model2.setRate(0.3);
        ExtraLoanRateModel model3 = new ExtraLoanRateModel();
        model3.setLoanId(loanId);
        model3.setExtraRateRuleId(100001);
        model3.setCreatedTime(new Date());
        model3.setMinInvestAmount(5000000);
        model3.setMaxInvestAmount(0);
        model3.setRate(0.5);
        List<ExtraLoanRateModel> list = Lists.newArrayList();
        list.add(model);
        list.add(model2);
        list.add(model3);
        return list;
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
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        loanDto.setVerifyTime(DateTime.now().toDate());
        loanDto.setRecheckTime(DateTime.now().toDate());
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private InvestModel getInvestModel(String loginName,long loanId){
        InvestModel investModel = new InvestModel(idGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null,0.1);
        investModel.setStatus(InvestStatus.SUCCESS);
        return investModel;
    }

    private UserModel getUserModelTest(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanModel getFakeLoan(String loanerLoginName,ActivityType activityType,ProductType productType,LoanStatus loanStatus) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName(loanerLoginName);
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName(loanerLoginName);
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(loanerLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(productType);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        return fakeLoanModel;
    }

}
