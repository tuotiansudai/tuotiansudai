package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

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
    public long estimateInvestIncome(long loanId, String loginName, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);

        //根据loginName查询出会员的相关信息
        long expectedInterest = InterestCalculator.estimateExpectedInterest(loanModel, amount);

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        double investFeeRate = membershipModel != null ? membershipModel.getFee() : defaultFee;
        long expectedFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(investFeeRate)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
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
        int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
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

            LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
            if (loanModel.getProductType().equals(ProductType.EXPERIENCE)) {
                List<UserCouponModel> userCouponModelList = userCouponMapper.findByInvestId(investModel.getId());
                for (UserCouponModel userCouponModel : userCouponModelList) {
                    if (userCouponModel.getStatus().equals(InvestStatus.SUCCESS)) {
                        investModel.setAmount(couponMapper.findById(userCouponModel.getCouponId()).getAmount());
                        break;
                    }
                }
            }

            items.add(new InvestorInvestPaginationItemDataDto(loanModel, investModel,
                    nextInvestRepayOptional.isPresent() ? nextInvestRepayOptional.get() : null,
                    userCouponDtoList, CollectionUtils.isNotEmpty(investRepayModels)));
        }

        BasePaginationDataDto<InvestorInvestPaginationItemDataDto> dto = new BasePaginationDataDto<>(index, pageSize, count, items);
        dto.setStatus(true);

        return dto;
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
                                                       InvestStatus investStatus, LoanStatus loanStatus) {
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
            int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = investMapper.findInvestPagination(loanId, investorLoginName, channel, source, role, (index - 1) * pageSize, pageSize, startTime, endTime, investStatus, loanStatus);
            for (InvestPaginationItemView investPaginationItemView : items) {
                if (loanId != null) {
                    LoanModel loanModel = loanMapper.findById(loanId);
                    if (loanModel != null) {
                        investPaginationItemView.setLoanName(loanModel.getName());
                    }
                }
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
    @Transactional
    public boolean turnOnAutoInvest(String loginName, AutoInvestPlanDto dto, String ip) {
        if (Strings.isNullOrEmpty(loginName)) {
            return false;
        }

        AutoInvestPlanModel model = autoInvestPlanMapper.findByLoginName(loginName);
        if (model != null) {
            model.setMinInvestAmount(AmountConverter.convertStringToCent(dto.getMinInvestAmount()));
            model.setMaxInvestAmount(AmountConverter.convertStringToCent(dto.getMaxInvestAmount()));
            model.setRetentionAmount(AmountConverter.convertStringToCent(dto.getRetentionAmount()));
            model.setAutoInvestPeriods(dto.getAutoInvestPeriods());
            model.setCreatedTime(new Date());
            model.setEnabled(true);
            autoInvestPlanMapper.update(model);
        } else {
            AutoInvestPlanModel autoInvestPlanModel = new AutoInvestPlanModel();
            autoInvestPlanModel.setId(idGenerator.generate());
            autoInvestPlanModel.setMinInvestAmount(AmountConverter.convertStringToCent(dto.getMinInvestAmount()));
            autoInvestPlanModel.setMaxInvestAmount(AmountConverter.convertStringToCent(dto.getMaxInvestAmount()));
            autoInvestPlanModel.setRetentionAmount(AmountConverter.convertStringToCent(dto.getRetentionAmount()));
            autoInvestPlanModel.setAutoInvestPeriods(dto.getAutoInvestPeriods());
            autoInvestPlanModel.setCreatedTime(new Date());
            autoInvestPlanModel.setEnabled(true);
            autoInvestPlanMapper.create(autoInvestPlanModel);
        }

        return true;
    }

    @Override
    public boolean turnOffAutoInvest(String loginName, String ip) {
        AutoInvestPlanModel model = autoInvestPlanMapper.findByLoginName(loginName);
        if (model == null || !model.isEnabled()) {
            return false;
        }
        autoInvestPlanMapper.disable(loginName);
        return true;
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
    public boolean switchNoPasswordInvest(String loginName, boolean isTurnOn, String ip) {
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
