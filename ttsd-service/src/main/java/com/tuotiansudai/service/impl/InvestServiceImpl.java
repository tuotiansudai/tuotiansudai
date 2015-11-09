package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.AmountConverter;
import com.tuotiansudai.utils.InterestCalculator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvestServiceImpl implements InvestService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "${novice.invest.limit.count}")
    private int noviceInvestLimitCount;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException {
        String loginName = LoginUserInfo.getLoginName();
        investDto.setLoginName(loginName);

        checkInvestAmount(investDto);

        return payWrapperClient.invest(investDto);

    }

    private void checkInvestAmount(InvestDto investDto) throws InvestException{
        long loanId = investDto.getLoanIdLong();
        LoanModel loan = loanMapper.findById(loanId);
        long userInvestMinAmount = loan.getMinInvestAmount();
        long investAmount = AmountConverter.convertStringToCent(investDto.getAmount());
        long userInvestIncreasingAmount = loan.getInvestIncreasingAmount();

        // 不满足新手标投资限制约束
        if(ActivityType.NOVICE == loan.getActivityType()){
            if(!canInvestNoviceLoan(investDto.getLoginName())){
                throw new InvestException("你的新手标投资已超上限");
            }
        }

        // 不满足最小投资限制
        if (investAmount < userInvestMinAmount) {
            throw new InvestException("投资金额小于标的最小投资金额");
        }

        // 不满足递增规则
        if ((investAmount - userInvestMinAmount) % userInvestIncreasingAmount > 0) {
            throw new InvestException("投资金额不符合递增金额要求");
        }

        long userInvestMaxAmount = loan.getMaxInvestAmount();
        long successInvestAmount = investMapper.sumSuccessInvestAmount(loanId);
        long loanNeedAmount = loan.getLoanAmount() - successInvestAmount;

        // 标已满
        if (loanNeedAmount <= 0) {
            throw new InvestException("标的已满");
        }

        // 超投
        if (loanNeedAmount < investAmount) {
            throw new InvestException("标的可投金额不足");
        }

        long userInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanId, investDto.getLoginName());

        // 不满足单用户投资限额
        if (investAmount > userInvestMaxAmount - userInvestAmount) {
            throw new InvestException("投资金额超过了用户投资限额");
        }

    }

    private boolean canInvestNoviceLoan(String loginName) {
        if(noviceInvestLimitCount == 0){
            return true;
        }
        int noviceInvestCount = investMapper.sumSuccessNoviceInvestCountByLoginName(loginName);
        return (noviceInvestCount < noviceInvestLimitCount);
    }

    @Override
    public long calculateExpectedInterest(long loanId, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        int repayTimes = loanModel.calculateLoanRepayTimes();
        LoanType loanType = loanModel.getType();

        int daysOfMonth = 30;
        int duration = loanModel.getPeriods();
        if (loanType.getLoanPeriodUnit() == LoanPeriodUnit.MONTH) {
            duration = repayTimes * daysOfMonth;
        }
        return InterestCalculator.calculateInterest(loanModel, amount * duration);
    }

    @Override
    public BasePaginationDataDto<InvestPaginationItemDataDto> getInvestPagination(Long loanId, String investorLoginName,
                                                                                  int index, int pageSize,
                                                                                  Date startTime, Date endTime,
                                                                                  InvestStatus investStatus, LoanStatus loanStatus) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        List<InvestPaginationItemView> items = Lists.newArrayList();

        long count = investMapper.findCountInvestPagination(loanId, investorLoginName, startTime, endTime, investStatus, loanStatus);


        if (count > 0 ) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = investMapper.findInvestPagination(loanId, investorLoginName, (index - 1) * pageSize, pageSize, startTime, endTime, investStatus, loanStatus);
        }

        List<InvestPaginationItemDataDto> records = Lists.transform(items, new Function<InvestPaginationItemView, InvestPaginationItemDataDto>() {
            @Override
            public InvestPaginationItemDataDto apply(InvestPaginationItemView view) {
                return new InvestPaginationItemDataDto(view);
            }
        });

        BasePaginationDataDto<InvestPaginationItemDataDto> dto = new BasePaginationDataDto<>(index, pageSize, count, records);

        dto.setStatus(true);

        return dto;
    }

    @Override
    public void turnOnAutoInvest(AutoInvestPlanModel model) {
        if(StringUtils.isBlank(model.getLoginName())){
            throw new NullPointerException("Not Login");
        }

        AutoInvestPlanModel planModel = autoInvestPlanMapper.findByLoginName(model.getLoginName());
        model.setCreatedTime(new Date());
        model.setEnabled(true);

        if (planModel != null) {
            model.setId(planModel.getId());
            autoInvestPlanMapper.update(model);
        } else {
            model.setId(idGenerator.generate());
            autoInvestPlanMapper.create(model);
        }
    }

    @Override
    public void turnOffAutoInvest(String loginName) {
        autoInvestPlanMapper.disable(loginName);
    }

    @Override
    public AutoInvestPlanModel findUserAutoInvestPlan(String loginName) {
        return autoInvestPlanMapper.findByLoginName(loginName);
    }

}
