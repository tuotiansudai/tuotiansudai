package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class InvestServiceImpl implements InvestService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "${web.newbie.invest.limit}")
    private int newbieInvestLimit;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException {
        checkInvestAmount(investDto);

        return payWrapperClient.invest(investDto);
    }

    private void checkInvestAmount(InvestDto investDto) throws InvestException {
        long loanId = investDto.getLoanIdLong();
        LoanModel loan = loanMapper.findById(loanId);
        long userInvestMinAmount = loan.getMinInvestAmount();
        long investAmount = AmountConverter.convertStringToCent(investDto.getAmount());
        long userInvestIncreasingAmount = loan.getInvestIncreasingAmount();

        // 标的状态不对
        if (LoanStatus.RAISING != loan.getStatus()) {
            throw new InvestException(InvestExceptionType.ILLEGAL_LOAN_STATUS);
        }

        // 不满足新手标投资限制约束
        if (ActivityType.NEWBIE == loan.getActivityType()) {
            if (!canInvestNewbieLoan(investDto.getLoginName())) {
                throw new InvestException(InvestExceptionType.OUT_OF_NOVICE_INVEST_LIMIT);
            }
        }

        // 不满足最小投资限制
        if (investAmount < userInvestMinAmount) {
            throw new InvestException(InvestExceptionType.LESS_THAN_MIN_INVEST_AMOUNT);
        }

        // 不满足递增规则
        if ((investAmount - userInvestMinAmount) % userInvestIncreasingAmount > 0) {
            throw new InvestException(InvestExceptionType.ILLEGAL_INVEST_AMOUNT);
        }

        long userInvestMaxAmount = loan.getMaxInvestAmount();
        long successInvestAmount = investMapper.sumSuccessInvestAmount(loanId);
        long loanNeedAmount = loan.getLoanAmount() - successInvestAmount;

        // 标已满
        if (loanNeedAmount <= 0) {
            throw new InvestException(InvestExceptionType.LOAN_IS_FULL);
        }

        // 超投
        if (loanNeedAmount < investAmount) {
            throw new InvestException(InvestExceptionType.EXCEED_MONEY_NEED_RAISED);
        }

        long userInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanId, investDto.getLoginName());

        // 不满足单用户投资限额
        if (investAmount > userInvestMaxAmount - userInvestAmount) {
            throw new InvestException(InvestExceptionType.MORE_THAN_MAX_INVEST_AMOUNT);
        }

    }

    private boolean canInvestNewbieLoan(String loginName) {
        if (newbieInvestLimit == 0) {
            return true;
        }
        int newbieInvestCount = investMapper.sumSuccessNewbieInvestCountByLoginName(loginName);
        return (newbieInvestCount < newbieInvestLimit);
    }

    @Override
    public long estimateInvestIncome(long loanId, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        int repayTimes = loanModel.calculateLoanRepayTimes();

        int daysOfMonth = 30;
        int duration = loanModel.getPeriods();
        if (loanModel.getType().getLoanPeriodUnit() == LoanPeriodUnit.MONTH) {
            duration = repayTimes * daysOfMonth;
        }
        long expectedInterest = InterestCalculator.calculateInterest(loanModel, amount * duration);
        long expectedFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        return expectedInterest - expectedFee;
    }

    @Override
    public BasePaginationDataDto<InvestPaginationItemDataDto> getInvestPagination(String investorLoginName, int index, int pageSize, Date startTime, Date endTime, LoanStatus loanStatus) {
        return getInvestPagination(null, investorLoginName, null, null, null, index, pageSize, startTime, endTime, null, loanStatus);
    }

    @Override
    public long findCountInvestPagination(Long loanId, String investorLoginName,
                                          String channel, Source source, String role,
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
        String strSource = source == null ? null : source.name();
        return investMapper.findCountInvestPagination(loanId, investorLoginName, channel, strSource, role, startTime, endTime, investStatus, loanStatus);
    }

    @Override
    public InvestPaginationDataDto getInvestPagination(Long loanId, String investorLoginName,
                                                                                  String channel, Source source, String role,
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

        String strSource = source == null ? null : source.name();

        long count = investMapper.findCountInvestPagination(loanId, investorLoginName, channel, strSource, role, startTime, endTime, investStatus, loanStatus);
        long investAmountSum = 0;

        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = investMapper.findInvestPagination(loanId, investorLoginName, channel, strSource, role, (index - 1) * pageSize, pageSize, startTime, endTime, investStatus, loanStatus);

            investAmountSum = investMapper.sumInvestAmount(loanId, investorLoginName, channel, strSource, role, startTime, endTime, investStatus, loanStatus);
        }

        List<InvestPaginationItemDataDto> records = Lists.transform(items, new Function<InvestPaginationItemView, InvestPaginationItemDataDto>() {
            @Override
            public InvestPaginationItemDataDto apply(InvestPaginationItemView view) {
                return new InvestPaginationItemDataDto(view);
            }
        });

        InvestPaginationDataDto dto = new InvestPaginationDataDto(index, pageSize, count, records);
        dto.setSumAmount(investAmountSum);

        dto.setStatus(true);

        return dto;
    }

    @Override
    public void turnOnAutoInvest(AutoInvestPlanModel model) {
        if (StringUtils.isBlank(model.getLoginName())) {
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

    @Override
    public List<String> findAllChannel() {
        return investMapper.findAllChannels();
    }

    @Override
    public InvestModel findById(long investId) {
        return investMapper.findById(investId);
    }
}
