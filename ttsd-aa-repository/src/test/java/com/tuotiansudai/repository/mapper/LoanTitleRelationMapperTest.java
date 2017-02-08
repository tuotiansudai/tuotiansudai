package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanTitleRelationMapperTest {
    @Autowired
    LoanTitleRelationMapper loanTitleRelationMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;


    private void createMockUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }
    @Test
    public void createLoanTitleTest(){
        createMockUser("xiangjie");
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long titleId = idGenerator.generate();
        loanTitleModel.setId(titleId);
        loanTitleModel.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleModel.setTitle("房产证");
        loanTitleMapper.create(loanTitleModel);
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setBasicRate("16.00");
        long loanId = idGenerator.generate();
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
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        List<LoanTitleRelationModel> loanTitleRelationModels = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 1; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanId);
            loanTitleRelationModel.setTitleId(titleId);
            loanTitleRelationModel.setApplicationMaterialUrls("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModels.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModels);
        assertTrue(loanTitleRelationMapper.findByLoanId(loanId).size() > 0);
    }

    @Test
    public void findLoanTitlesTest(){
        createMockUser("xiangjie");
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long titleId = idGenerator.generate();
        loanTitleModel.setId(titleId);
        loanTitleModel.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleModel.setTitle("房产证");
        loanTitleMapper.create(loanTitleModel);
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setBasicRate("16.00");
        long loanId = idGenerator.generate();
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
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        List<LoanTitleRelationModel> loanTitleRelationModels = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 1; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanId);
            loanTitleRelationModel.setTitleId(titleId);
            loanTitleRelationModel.setApplicationMaterialUrls("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModels.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModels);
        List<LoanTitleRelationModel> models = loanTitleRelationMapper.findByLoanId(loanId);
        assertNotNull(models);
        assertNotNull(models.get(0).getTitleId());
    }

    @Test
    public void deleteTest(){
        createMockUser("xiangjie");
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long titleId = idGenerator.generate();
        loanTitleModel.setId(titleId);
        loanTitleModel.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleModel.setTitle("房产证");
        loanTitleMapper.create(loanTitleModel);
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setBasicRate("16.00");
        long loanId = idGenerator.generate();
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
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        List<LoanTitleRelationModel> loanTitleRelationModels = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 1; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanId);
            loanTitleRelationModel.setTitleId(titleId);
            loanTitleRelationModel.setApplicationMaterialUrls("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModels.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModels);
        loanTitleRelationMapper.delete(loanId);
        assertTrue(loanTitleRelationMapper.findByLoanId(loanId).size() == 0);
    }

    @Test
    public void findLoanTitleRelationAndTitleByLoanIdText(){
        createMockUser("xiaoming");
        LoanTitleModel loanTitleModel1 = new LoanTitleModel();
        long loanTitleModel1Id = idGenerator.generate();
        loanTitleModel1.setId(loanTitleModel1Id);
        loanTitleModel1.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleModel1.setTitle("身份证");
        LoanTitleModel loanTitleModel2 = new LoanTitleModel();
        long loanTitleModel2Id = idGenerator.generate();
        loanTitleModel2.setId(loanTitleModel2Id);
        loanTitleModel2.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleModel2.setTitle("房产证");
        loanTitleMapper.create(loanTitleModel1);
        loanTitleMapper.create(loanTitleModel2);
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiaoming");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName("xiaoming");
        loanDto.setBasicRate("16.00");
        long loanId = idGenerator.generate();
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
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
        Long loanTitleRelationModelId = idGenerator.generate();
        loanTitleRelationModel.setId(loanTitleRelationModelId);
        loanTitleRelationModel.setTitleId(loanTitleModel1Id);
        loanTitleRelationModel.setApplicationMaterialUrls("ceshi/20160409/text.jpg");
        loanTitleRelationModel.setLoanId(loanId);
        LoanTitleRelationModel loanTitleRelationModelT = new LoanTitleRelationModel();
        loanTitleRelationModelT.setTitleId(loanTitleModel2Id);
        loanTitleRelationModelT.setApplicationMaterialUrls("ceshi/20160409/fangchan.jpg");
        loanTitleRelationModelT.setId(idGenerator.generate());
        loanTitleRelationModelT.setLoanId(loanId);
        List<LoanTitleRelationModel> LoanTitleRelationModel = new ArrayList<>();
        LoanTitleRelationModel.add(loanTitleRelationModel);
        LoanTitleRelationModel.add(loanTitleRelationModelT);
        loanTitleRelationMapper.create(LoanTitleRelationModel);


        List<LoanTitleRelationModel> LoanTitleRelationModelList = loanTitleRelationMapper.findLoanTitleRelationAndTitleByLoanId(loanId);


        assertEquals(LoanTitleRelationModelList.get(0).getTitle(), loanTitleModel1.getTitle());
        assertEquals(LoanTitleRelationModelList.get(0).getApplicationMaterialUrls(), loanTitleRelationModel.getApplicationMaterialUrls());
        assertEquals(LoanTitleRelationModelList.get(1).getTitle(),loanTitleModel2.getTitle());
        assertEquals(LoanTitleRelationModelList.get(1).getApplicationMaterialUrls(),loanTitleRelationModelT.getApplicationMaterialUrls());

    }
}
