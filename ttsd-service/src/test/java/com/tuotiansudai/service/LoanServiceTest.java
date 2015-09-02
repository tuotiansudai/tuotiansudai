package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestRecordDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import junit.framework.TestCase;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanServiceTest {
    @Autowired
    private LoanService loanService;
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    /**
     * 正常建标
     */
    @Test
    public void createLoanServiceTest_1() {
        LoanDto loanDto = new LoanDto();
        long loanId = idGenerator.generate();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setMaxInvestAmount("100.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("10000.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertTrue(baseDto.getData().getStatus());
        assertNotNull(loanMapper.findById(loanDto.getId()));
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() > 0);
    }

    /**
     * 建标时，起投时间晚于结束时间
     */
    @Test
    public void createLoanServiceTest_2() {
        LoanDto loanDto = new LoanDto();
        long id = idGenerator.generate();
        loanDto.setId(id);
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setMaxInvestAmount("100000000000.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("100000000000.00");
        loanDto.setFundraisingStartTime(new Date(System.currentTimeMillis() + 1000));
        loanDto.setFundraisingEndTime(new Date(System.currentTimeMillis()));
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(id).size() == 0);
        assertNull(loanMapper.findById(id));
    }

    /**
     * 投资最大金额小于投资最小金额
     */
    @Test
    public void createLoanServiceTest_3() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("10000.00");
        loanDto.setMaxInvestAmount("99.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 投资金额小于投资最大金额
     */
    @Test
    public void createLoanServiceTest_4() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("998.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 借款人不存在
     */
    @Test
    public void createLoanServiceTest_5() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("liming");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("1000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 代理人不存在
     */
    @Test
    public void createLoanServiceTest_6() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("liming");
        loanDto.setLoanAmount("1000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = loanService.createLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    public BaseDto<PayDataDto> creteLoan(LoanDto loanDto) {
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
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
            loanTitleRelationModel.setTitleId(Long.parseLong("12312312312"));
            loanTitleRelationModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        return loanService.createLoan(loanDto);
    }


    @Test
    public void shouldGetLoanDetailTest(){
        long id = createLoanService();
        BaseDto<LoanDto> baseDto = loanService.getLoanDetail(id);
        Assert.assertNotNull(baseDto.getData().getId());
        Assert.assertNotNull(baseDto.getData().getLoanTitles().get(0).getApplyMetarialUrl());
        assertEquals(99.5, baseDto.getData().getAmountNeedRaised());
        assertEquals(0.01,baseDto.getData().getRaiseCompletedRate());
    }
    @Test
    public void shouldGetTheInvests(){
        createTestInvests();
        BaseDto<InvestRecordDataDto> baseDto = loanService.getInvests(1,1,5);
        assertEquals(5, baseDto.getData().getInvestRecordDtoList().size());
        assertEquals(true,baseDto.getData().isHasNextPage());
        assertEquals(false,baseDto.getData().isHasPreviousPage());
    }

    @Test
    public void shouldGetTheInvestsAndNextPagePreviousPage(){
        createTestInvests();
        BaseDto<InvestRecordDataDto> baseDto = loanService.getInvests(1,4,3);
        assertEquals(1, baseDto.getData().getInvestRecordDtoList().size());
        assertEquals(false,baseDto.getData().isHasNextPage());
        assertEquals(true,baseDto.getData().isHasPreviousPage());
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
        Date currentDate = new Date((new Date().getTime()/1000)*1000);
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
}
