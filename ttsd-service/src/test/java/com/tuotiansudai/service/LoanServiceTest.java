package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanServiceTest {
    @Autowired
    private LoanService loanService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Before
    public void createLoanTitle(){
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        loanTitleModel.setId(idGenerator.generate());
        loanTitleModel.setTitle("身份证");
        loanTitleModel.setType(LoanTitleType.BASE_TITLE_TYPE);
        loanTitleMapper.create(loanTitleModel);
    }


<<<<<<< HEAD
    @Test
    public void shouldGetLoanDetailTest(){
        long id = createLoanService();
        BaseDto<LoanDto> baseDto = loanService.getLoanDetail(id);
        Assert.assertNotNull(baseDto.getData().getId());
        Assert.assertNotNull(baseDto.getData().getLoanTitles().get(0).getApplyMetarialUrl());
        assertEquals(99.5, baseDto.getData().getAmountNeedRaised());
        assertEquals(0.01, baseDto.getData().getRaiseCompletedRate());
    }
    @Test
    public void shouldGetTheInvests(){
        createTestInvests();
        BasePaginationDto<InvestPaginationDataDto> dto = loanService.getInvests(1, 1, 5);
        assertEquals(5, dto.getRecordDtoList().size());
        assertEquals(true, dto.isHasNextPage());
        assertEquals(false, dto.isHasPreviousPage());
    }

    @Test
    public void shouldGetTheInvestsAndNextPagePreviousPage(){
        createTestInvests();
        BasePaginationDto<InvestPaginationDataDto> dto = loanService.getInvests(1, 4, 3);
        assertEquals(1, dto.getRecordDtoList().size());
        assertEquals(false, dto.isHasNextPage());
        assertEquals(true, dto.isHasPreviousPage());
    }
    private void createTestInvests(){

        for(int i=0;i<10;i++) {
            InvestModel investModel = this.getFakeInvestModel(idGenerator.generate());
            investModel.setLoanId(1);
            investModel.setLoginName("hourglass");
            investModel.setStatus(InvestStatus.SUCCESS);
            investModel.setCreatedTime(DateUtils.addHours(new Date(), -i));
            investMapper.create(investModel);
        }
    }

    private long createLoanService(){
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("xiangjie");
        loanModel.setBaseRate(16.00);
        long id = idGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(30l);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestFeeRate(15);
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.LOAN_TYPE_1);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName("loaner");
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
            loanTitleRelationModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModelList);

        InvestModel investModel1 = getFakeInvestModel(id);
        investModel1.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel2 = getFakeInvestModel(id);
        investModel2.setStatus(InvestStatus.FAIL);

        InvestModel investModel3 = getFakeInvestModel(id);
        investModel3.setStatus(InvestStatus.WAITING);

        investMapper.create(investModel1);
        investMapper.create(investModel2);
        investMapper.create(investModel3);




        return id;

    }

    private InvestModel getFakeInvestModel(long loanId) {
        InvestModel model = new InvestModel();
        model.setAmount(50);
        // 舍弃毫秒数
        Date currentDate = new Date((new Date().getTime() / 1000) * 1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName("hourglass");
        model.setLoanId(loanId);
        model.setSource(InvestSource.ANDROID);
        model.setStatus(InvestStatus.WAITING);
        model.setCreatedTime(new Date());
        return model;
    }

    @Test
    public void updateLoanTest() {
        List<LoanModel> loanModelList = loanMapper.findByStatus(LoanStatus.VERIFY_FAIL);
        long loanId = loanModelList.get(0).getId();;
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("liming");
        loanDto.setLoanAmount("5000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setProjectName("店铺资金周转更新");
        loanDto.setActivityRate("12.00");
        loanDto.setBasicRate("16.00");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 5; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanDto.getId());
            List<LoanTitleModel> loanTitleModelList = loanTitleMapper.findAll();
            if (loanTitleModelList != null && loanTitleModelList.size() > 0){
                loanTitleRelationModel.setTitleId(loanTitleModelList.get(0).getId());
            }
            loanTitleRelationModel.setApplyMetarialUrl("www.baidu.com,www.google.com");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        loanService.updateLoan(loanDto);
        assertTrue(LoanStatus.VERIFY_FAIL == loanMapper.findById(loanId).getStatus());
    }

    public void shouldNotifyInvestorsLoanOutSuccessfulByEmail() {
        LoanModel model = new LoanModel();
        long id = idGenerator.generate();
        model.setId(id);
        model.setName("hourglass");
        model.setLoanerLoginName("loaner");
        model.setAgentLoginName("agent");
        model.setType(LoanType.LOAN_TYPE_1);
        model.setPeriods(3l);
        model.setLoanAmount(1000l);
        model.setInvestFeeRate(1.0);
        model.setActivityType(ActivityType.EXCLUSIVE);
        model.setBaseRate(1.0);
        model.setContractId(123);
        model.setDescriptionText("text");
        model.setDescriptionHtml("html");
        model.setFundraisingStartTime(new Date());
        model.setFundraisingEndTime(new Date());
        model.setCreatedTime(new Date());
        model.setStatus(LoanStatus.RAISING);
        loanMapper.create(model);

        InvestModel investModel = new InvestModel();
        investModel.setId(idGenerator.generate());
        investModel.setLoginName("zhuyanyan");
        investModel.setLoanId(id);
        investModel.setAmount(100);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setSource(InvestSource.ANDROID);
        investModel.setIsAutoInvest(true);
        investModel.setCreatedTime(new Date());
        investMapper.create(investModel);

        loanService.notifyInvestorsLoanOutSuccessfulByEmail(model);

    }
}
