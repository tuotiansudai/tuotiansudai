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
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class InvestServiceImpl implements InvestService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    private final static String INVEST_NO_PASSWORD_REMIND_MAP = "invest_no_password_remind_map";

    @Value(value = "${web.coupon.lock.seconds}")
    private int couponLockSeconds;

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
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException {
        investDto.setNoPassword(false);
        this.checkInvestAvailable(investDto);
        return payWrapperClient.invest(investDto);
    }

    @Override
    public BaseDto<PayDataDto> noPasswordInvest(InvestDto investDto) throws InvestException {
        investDto.setNoPassword(true);
        this.checkInvestAvailable(investDto);
        return payWrapperClient.noPasswordInvest(investDto);
    }

    private void checkInvestAvailable(InvestDto investDto) throws InvestException {
        long loanId = Long.parseLong(investDto.getLoanId());
        LoanModel loan = loanMapper.findById(loanId);

        if (loan == null) {
            throw new InvestException(InvestExceptionType.NOT_ENOUGH_BALANCE);
        }

        if (investDto.getLoginName().equalsIgnoreCase(loan.getAgentLoginName())) {
            throw new InvestException(InvestExceptionType.INVESTOR_IS_LOANER);
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

        UserCouponDto maxBenefitUserCoupon = userCouponService.getMaxBenefitUserCoupon(investDto.getLoginName(), loanId, investAmount);
        if (maxBenefitUserCoupon != null && CollectionUtils.isEmpty(investDto.getUserCouponIds())) {
            throw new InvestException(InvestExceptionType.NONE_COUPON_SELECTED);
        }

        // 验证优惠券是否可用
        List<Long> userCouponIds = investDto.getUserCouponIds();
        if (CollectionUtils.isNotEmpty(userCouponIds)) {
            for (long userCouponId : userCouponIds) {
                UserCouponModel userCouponModel = userCouponMapper.lockById(userCouponId);
                Date usedTime = userCouponModel.getUsedTime();
                if (usedTime != null && new DateTime(usedTime).plusSeconds(couponLockSeconds).isAfter(new DateTime())) {
                    throw new InvestException(InvestExceptionType.COUPON_IS_UNUSABLE);
                }
            }
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

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getLoanDetailsByLoanId(loanId);

        long extraRateInterest = 0;
        long extraRateFee = 0;
        if (!Strings.isNullOrEmpty(loanDetailsModel.getExtraSource()) && loanDetailsModel.getExtraSource().contains(Source.WEB.name())) {
            extraRateInterest = getExtraRate(loanId, amount, loanModel.getDuration());
            extraRateFee = new BigDecimal(extraRateInterest).multiply(new BigDecimal(investFeeRate)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        }

        return (expectedInterest - expectedFee) + (extraRateInterest - extraRateFee);
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
                    userCouponDtoList.add(new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel, 0));
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

            InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investModel.getId());

            items.add(new InvestorInvestPaginationItemDataDto(loanModel, investModel,
                    nextInvestRepayOptional.isPresent() ? nextInvestRepayOptional.get() : null,
                    userCouponDtoList, CollectionUtils.isNotEmpty(investRepayModels), investExtraRateModel));
        }

        BasePaginationDataDto<InvestorInvestPaginationItemDataDto> dto = new BasePaginationDataDto<>(index, pageSize, count, items);
        dto.setStatus(true);

        return dto;
    }

    @Override
    public InvestPaginationDataDto getInvestPagination(Long loanId, String investorMobile, String channel, Source source,
                                                       Role role, Date startTime, Date endTime, InvestStatus investStatus,
                                                       PreferenceType preferenceType, int index, int pageSize) {
        List<InvestPaginationItemView> items = Lists.newArrayList();

        String investorLoginName = null;
        if (!StringUtils.isEmpty(investorMobile)) {
            UserModel userModel = userMapper.findByMobile(investorMobile);
            if (null != userModel) {
                investorLoginName = userMapper.findByMobile(investorMobile).getLoginName();
            } else {
                investorLoginName = investorMobile;
            }
        }

        final long count = investMapper.findCountInvestPagination(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, preferenceType);
        final long investAmountSum = investMapper.sumInvestAmountConsole(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, preferenceType);
        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = investMapper.findInvestPagination(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, preferenceType, (index - 1) * pageSize, pageSize);
        }

        List<InvestPaginationItemDataDto> records = Lists.transform(items, new Function<InvestPaginationItemView, InvestPaginationItemDataDto>() {
            @Override
            public InvestPaginationItemDataDto apply(InvestPaginationItemView view) {
                InvestPaginationItemDataDto investPaginationItemDataDto = new InvestPaginationItemDataDto(view);
                CouponModel couponModel = couponMapper.findById(view.getCouponId());
                if (null != couponModel) {
                    long couponActualInterest = 0;
                    if (couponModel.getCouponType().equals(CouponType.RED_ENVELOPE)) {
                        List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessByInvestId(view.getInvestId());
                        for (UserCouponModel userCouponModel : userCouponModels) {
                            couponActualInterest += userCouponModel.getActualInterest();
                        }
                    } else {
                        List<CouponRepayModel> couponRepayModels = couponRepayMapper.findByUserCouponByInvestId(view.getInvestId());
                        for (CouponRepayModel couponRepayModel : couponRepayModels) {
                            couponActualInterest += couponRepayModel.getActualInterest();
                        }
                    }
                    investPaginationItemDataDto.setCouponActualInterest(couponActualInterest);
                    investPaginationItemDataDto.setCouponDetail(couponModel);
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
            autoInvestPlanModel.setLoginName(loginName);
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

    private long getExtraRate(long loanId, long amount, int duration) {
        double rate = 0;
        List<ExtraLoanRateModel> extraLoanRateModelList = extraLoanRateMapper.findByLoanIdOrderByRate(loanId);
        for (ExtraLoanRateModel extraLoanRateModel : extraLoanRateModelList) {
            if (extraLoanRateModel.getMinInvestAmount() <= amount && extraLoanRateModel.getMaxInvestAmount() == 0) {
                rate = extraLoanRateModel.getRate();
                break;
            }
            if (extraLoanRateModel.getMinInvestAmount() <= amount && amount < extraLoanRateModel.getMaxInvestAmount()) {
                rate = extraLoanRateModel.getRate();
                break;
            }
        }

        return rate == 0 ? 0 : new BigDecimal(duration * amount).multiply(new BigDecimal(rate)).divide(new BigDecimal(InterestCalculator.DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
    }

    public long calculateMembershipPreference(String loginName, long loanId, long investAmount) {
        long preference = 0;
        UserMembershipModel userMembershipModel = userMembershipMapper.findCurrentMaxByLoginName(loginName);
        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());
        LoanModel loanModel = loanMapper.findById(loanId);
        double preferenceRate = defaultFee - membershipModel.getFee();
        long interest = InterestCalculator.estimateExpectedInterest(loanModel, investAmount);
        preference = new BigDecimal(preferenceRate).multiply(new BigDecimal(interest)).longValue();
        return preference;
    }
}
