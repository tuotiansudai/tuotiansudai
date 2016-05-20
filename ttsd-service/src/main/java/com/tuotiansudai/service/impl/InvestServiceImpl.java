package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InvestServiceImpl implements InvestService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    private final static String INVEST_NO_PASSWORD_REMIND_MAP = "invest_no_password_remind_map";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

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

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException {
        checkInvestAmount(investDto);

        return payWrapperClient.invest(investDto);
    }

    @Override
    public BaseDto<PayDataDto> noPasswordInvest(InvestDto investDto) throws InvestException {
        investDto.setNoPassword(true);
        this.checkInvestAmount(investDto);
        return payWrapperClient.noPasswordInvest(investDto);
    }

    private void checkInvestAmount(InvestDto investDto) throws InvestException {
        long loanId = Long.parseLong(investDto.getLoanId());
        LoanModel loan = loanMapper.findById(loanId);


        if (loan == null) {
            throw new InvestException(InvestExceptionType.NOT_ENOUGH_BALANCE);
        }

        long userInvestMinAmount = loan.getMinInvestAmount();
        long investAmount = AmountConverter.convertStringToCent(investDto.getAmount());
        long userInvestIncreasingAmount = loan.getInvestIncreasingAmount();

        AccountModel accountModel = accountMapper.findByLoginName(investDto.getLoginName());
        if (accountModel.getBalance() < investAmount) {
            throw new InvestException(InvestExceptionType.NOT_ENOUGH_BALANCE);
        }

        // 尚未开启免密投资
        if (investDto.isNoPassword() && !accountModel.isNoPasswordInvest()) {
            throw new InvestException(InvestExceptionType.PASSWORD_INVEST_OFF);
        }

        // 标的状态不对
        if (LoanStatus.RAISING != loan.getStatus()) {
            throw new InvestException(InvestExceptionType.ILLEGAL_LOAN_STATUS);
        }

        if (ActivityType.NEWBIE == loan.getActivityType() && !canInvestNewbieLoan(investDto.getLoginName())) {
            throw new InvestException(InvestExceptionType.OUT_OF_NOVICE_INVEST_LIMIT);
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
        int newbieInvestCount = investMapper.sumSuccessInvestCountByLoginName(loginName);
        return newbieInvestLimit == 0 || newbieInvestCount < newbieInvestLimit;
    }

    @Override
    public long estimateInvestIncome(long loanId, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        long expectedInterest = InterestCalculator.estimateExpectedInterest(loanModel, amount);
        long expectedFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        return expectedInterest - expectedFee;
    }

    @Override
    public BasePaginationDataDto<InvestorInvestPaginationItemDataDto> getInvestPagination(String loginName, int index, int pageSize, Date startTime, Date endTime, LoanStatus loanStatus) {
        startTime = new DateTime(startTime == null ? 0L : startTime).withTimeAtStartOfDay().toDate();
        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = investMapper.countInvestorInvestPagination(loginName, loanStatus, startTime, endTime);
        int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
        index = index > totalPages ? totalPages : index;

        List<InvestModel> investModels = investMapper.findInvestorInvestPagination(loginName, loanStatus, (index - 1) * pageSize, pageSize, startTime, endTime);
        List<InvestorInvestPaginationItemDataDto> items = Lists.newArrayList();

        for (InvestModel investModel : investModels) {
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            Optional<InvestRepayModel> nextInvestRepayOptional = Iterators.tryFind(investRepayModels.iterator(), new Predicate<InvestRepayModel>() {
                @Override
                public boolean apply(InvestRepayModel input) {
                    return Lists.newArrayList(RepayStatus.REPAYING, RepayStatus.OVERDUE).contains(input.getStatus());
                }
            });

            List<UserCouponDto> userCouponDtoList = Lists.newArrayList();
            if (investModel.getStatus() == InvestStatus.SUCCESS) {
                List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(investModel.getId());
                for (UserCouponModel userCouponModel : userCouponModels) {
                    userCouponDtoList.add(new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel));
                }
            }

            items.add(new InvestorInvestPaginationItemDataDto(loanMapper.findById(investModel.getLoanId()).getName(), investModel,
                    nextInvestRepayOptional.isPresent() ? nextInvestRepayOptional.get() : null,
                    userCouponDtoList, CollectionUtils.isNotEmpty(investRepayModels)));
        }

        BasePaginationDataDto<InvestorInvestPaginationItemDataDto> dto = new BasePaginationDataDto<>(index, pageSize, count, items);
        dto.setStatus(true);

        return dto;
    }

    @Override
    public BasePaginationDataDto<InvestPaginationItemDataDto> getTransferApplicationTransferablePagination(String investorLoginName, int index, int pageSize, Date startTime, Date endTime, LoanStatus loanStatus) {
        InvestPaginationDataDto investPaginationDataDto = getInvestPagination(null, investorLoginName, null, null, null, index, pageSize, startTime, endTime, null, loanStatus,false);
        UnmodifiableIterator<InvestPaginationItemDataDto> filter = Iterators.filter(investPaginationDataDto.getRecords().iterator(), new Predicate<InvestPaginationItemDataDto>() {
            @Override
            public boolean apply(InvestPaginationItemDataDto input) {
                return TransferStatus.TRANSFERABLE.getDescription().equals(input.getTransferStatus()) && investTransferService.isTransferable(input.getInvestId());
            }
        });
        List<InvestPaginationItemDataDto>  items = Lists.newArrayList(filter);
        int fromIndex = (index - 1) * pageSize;
        int toIndex = fromIndex + pageSize;
        if(fromIndex >= items.size()){
            fromIndex = items.size();
        }
        if(toIndex >= items.size()){
            toIndex = items.size();
        }
        return new InvestPaginationDataDto(index,pageSize,items.size(),items.subList(fromIndex, toIndex));
    }

    @Override
    public long findCountInvestPagination(Long loanId, String investorLoginName,
                                          String channel, Source source, String role,
                                          Date startTime, Date endTime,
                                          InvestStatus investStatus, LoanStatus loanStatus) {
        startTime = new DateTime(startTime == null ? 0L : startTime).withTimeAtStartOfDay().toDate();

        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }
        return investMapper.findCountInvestPagination(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, loanStatus);
    }

    @Override
    public InvestPaginationDataDto getInvestPagination(Long loanId, String investorLoginName,
                                                       String channel, Source source, String role,
                                                       int index, int pageSize,
                                                       Date startTime, Date endTime,
                                                       InvestStatus investStatus, LoanStatus loanStatus, boolean isPagination) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        startTime = new DateTime(startTime == null ? 0 : startTime).withTimeAtStartOfDay().toDate();
        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        List<InvestPaginationItemView> items = Lists.newArrayList();

        long count = investMapper.findCountInvestPagination(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, loanStatus);
        long investAmountSum = 0;

        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = investMapper.findInvestPagination(loanId, investorLoginName, channel, source, role, (index - 1) * pageSize, pageSize, startTime, endTime, investStatus, loanStatus,isPagination);
            for (InvestPaginationItemView investPaginationItemView : items) {
                List<UserCouponModel> userCouponModels = userCouponMapper.findBirthdaySuccessByLoginNameAndInvestId(investorLoginName, investPaginationItemView.getId());
                investPaginationItemView.setBirthdayCoupon(CollectionUtils.isNotEmpty(userCouponModels));
                if (CollectionUtils.isNotEmpty(userCouponModels)) {
                    investPaginationItemView.setBirthdayBenefit(couponMapper.findById(userCouponModels.get(0).getCouponId()).getBirthdayBenefit());
                }
            }
            investAmountSum = investMapper.sumInvestAmount(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, loanStatus);
        }

        List<InvestPaginationItemDataDto> records = Lists.transform(items, new Function<InvestPaginationItemView, InvestPaginationItemDataDto>() {
            @Override
            public InvestPaginationItemDataDto apply(InvestPaginationItemView view) {
                InvestPaginationItemDataDto investPaginationItemDataDto = new InvestPaginationItemDataDto(view);
                investPaginationItemDataDto.setTransferStatus(view.getTransferStatus().getDescription());
                investPaginationItemDataDto.setLastRepayDate(loanRepayMapper.findLastRepayDateByLoanId(view.getLoanId()));
                LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(view.getLoanId());
                if (loanRepayModel != null) {
                    int leftPeriod = investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(view.getId(), loanRepayModel.getPeriod());
                    investPaginationItemDataDto.setLeftPeriod(leftPeriod);
                }
                return investPaginationItemDataDto;
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
    public AutoInvestPlanModel findAutoInvestPlan(String loginName) {
        return autoInvestPlanMapper.findByLoginName(loginName);
    }

    @Override
    public List<String> findAllChannel() {
        return investMapper.findAllChannels();
    }

    @Override
    public List<String> findAllInvestChannels() {
        return investMapper.findAllInvestChannels();
    }

    @Override
    public InvestModel findById(long investId) {
        return investMapper.findById(investId);
    }

    @Override
    public InvestModel findLatestSuccessInvest(String loginName) {
        InvestModel investModel = investMapper.findLatestSuccessInvest(loginName);
        if (investModel == null) {
            return null;
        }

        return investModel;
    }

    @Override
    @Transactional
    public boolean switchNoPasswordInvest(String loginName, boolean isTurnOn) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        accountModel.setNoPasswordInvest(isTurnOn);
        accountMapper.update(accountModel);
        return true;
    }

    @Override
    public void markNoPasswordRemind(String loginName) {
        redisWrapperClient.hsetSeri(INVEST_NO_PASSWORD_REMIND_MAP, loginName, "1");
    }
}
