package com.tuotiansudai.service.impl;

import com.google.common.base.*;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class InvestServiceImpl implements InvestService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    private final static String INVEST_NO_PASSWORD_REMIND_MAP = "invest_no_password_remind_map";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value(value = "${web.coupon.lock.seconds}")
    private int couponLockSeconds;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "${web.newbie.invest.limit}")
    private int newbieInvestLimit;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

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

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Autowired
    private UserOpLogService userOpLogService;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException {
        accountMapper.lockByLoginName(investDto.getLoginName());
        investDto.setNoPassword(false);
        this.checkInvestAvailable(investDto);
        return payWrapperClient.invest(investDto);
    }

    @Override
    @Transactional
    public BaseDto<PayDataDto> noPasswordInvest(InvestDto investDto) throws InvestException {
        accountMapper.lockByLoginName(investDto.getLoginName());
        investDto.setNoPassword(true);
        this.checkInvestAvailable(investDto);
        return payWrapperClient.noPasswordInvest(investDto);
    }

    private void checkInvestAvailable(InvestDto investDto) throws InvestException {
        AccountModel accountModel = accountMapper.findByLoginName(investDto.getLoginName());

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

        long userInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanId, investDto.getLoginName(), true);

        // 不满足单用户投资限额
        if (investAmount > userInvestMaxAmount - userInvestAmount) {
            throw new InvestException(InvestExceptionType.MORE_THAN_MAX_INVEST_AMOUNT);
        }

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);
        if (CollectionUtils.isNotEmpty(investDto.getUserCouponIds()) && loanDetailsModel.getDisableCoupon()){
            throw new InvestException(InvestExceptionType.NOT_USE_COUPON);
        }

        if (!loanDetailsModel.getDisableCoupon()){
            this.checkUserCouponIsAvailable(investDto);
        }
    }

    // 验证优惠券是否可用
    private void checkUserCouponIsAvailable(InvestDto investDto) throws InvestException {
        long loanId = Long.parseLong(investDto.getLoanId());

        LoanModel loanModel = loanMapper.findById(loanId);
        String loginName = investDto.getLoginName();
        long investAmount = AmountConverter.convertStringToCent(investDto.getAmount());

        logger.info(MessageFormat.format("user({0}) invest (loan = {1} amount = {2}) with user coupon({3})",
                investDto.getLoginName(),
                String.valueOf(loanId),
                investDto.getAmount(),
                CollectionUtils.isNotEmpty(investDto.getUserCouponIds()) ? String.valueOf(investDto.getUserCouponIds().get(0)) : "empty"));

        UserCouponDto maxBenefitUserCoupon = userCouponService.getMaxBenefitUserCoupon(loginName, loanId, investAmount);
        if (maxBenefitUserCoupon != null && CollectionUtils.isEmpty(investDto.getUserCouponIds())) {
            logger.warn(MessageFormat.format("user({0}) invest (loan = {1} amount = {2}) with no user coupon, but max benefit user coupon({3}) is existed",
                    investDto.getLoginName(),
                    String.valueOf(loanId),
                    investDto.getAmount(),
                    String.valueOf(maxBenefitUserCoupon.getId())));
            throw new InvestException(InvestExceptionType.NONE_COUPON_SELECTED);
        }

        List<Long> userCouponIds = investDto.getUserCouponIds();
        if (CollectionUtils.isNotEmpty(userCouponIds)) {
            List<UserCouponModel> notSharedCoupons = Lists.newArrayList();
            for (long userCouponId : userCouponIds) {
                UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                Date usedTime = userCouponModel.getUsedTime();
                logger.info(MessageFormat.format("user({0}) invest (loan = {1} amount = {2}) with user coupon(id = {3} usedTime = {4} status = {5})",
                        investDto.getLoginName(),
                        String.valueOf(loanId),
                        investDto.getAmount(),
                        String.valueOf(userCouponId),
                        usedTime,
                        userCouponModel.getStatus()));
                if ((usedTime != null && new DateTime(usedTime).plusSeconds(couponLockSeconds).isAfter(new DateTime()))
                        || !loginName.equalsIgnoreCase(userCouponModel.getLoginName())
                        || InvestStatus.SUCCESS == userCouponModel.getStatus()
                        || (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON && !UserBirthdayUtil.isBirthMonth(userMapper.findByLoginName(loginName).getIdentityNumber()))
                        || userCouponModel.getEndTime().before(new Date())
                        || !couponModel.getProductTypes().contains(loanModel.getProductType())
                        || (couponModel.getInvestLowerLimit() > 0 && investAmount < couponModel.getInvestLowerLimit())) {
                    logger.warn(MessageFormat.format("user({0}) use user coupon ({1}) is unusable", loginName, String.valueOf(userCouponId)));
                    throw new InvestException(InvestExceptionType.COUPON_IS_UNUSABLE);
                }
                if (!couponModel.isShared()) {
                    notSharedCoupons.add(userCouponModel);
                }
            }

            if (notSharedCoupons.size() > 1) {
                String notSharedUserCouponIds = Joiner.on(",").join(Lists.transform(notSharedCoupons, new Function<UserCouponModel, String>() {
                    @Override
                    public String apply(UserCouponModel input) {
                        return String.valueOf(input.getId());
                    }
                }));
                logger.error(MessageFormat.format("user({0}) used more then one not shared coupons({1})", loginName, notSharedUserCouponIds));
                throw new InvestException(InvestExceptionType.COUPON_IS_UNUSABLE);
            }
        }
    }

    private boolean canInvestNewbieLoan(String loginName) {
        int newbieInvestCount = investMapper.sumSuccessInvestCountByLoginName(loginName);
        return Lists.newArrayList("zr0612", "liangjinhua").contains(loginName.toLowerCase()) || newbieInvestLimit == 0 || newbieInvestCount < newbieInvestLimit;
    }

    @Override
    public long estimateInvestIncome(long loanId, double investFeeRate, String loginName, long amount, Date investTime) {
        LoanModel loanModel = loanMapper.findById(loanId);

        //根据loginName查询出会员的相关信息
        List<Long> expectedInterestList = InterestCalculator.estimateExpectedInterest(loanModel, amount, investTime);

        long expectedInterest = 0L;
        for (Long expectedInterestOfPerPeriod : expectedInterestList) {
            long expectedOfPerPeriodFee = new BigDecimal(expectedInterestOfPerPeriod).multiply(new BigDecimal(investFeeRate)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            expectedInterest += (expectedInterestOfPerPeriod - expectedOfPerPeriodFee);
        }

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);

        long extraRateInterest = 0;
        long extraRateFee = 0;
        if (loanDetailsModel != null && !CollectionUtils.isEmpty(loanDetailsModel.getExtraSource()) && loanDetailsModel.getExtraSource().contains(Source.WEB)) {
            //根据不同的标的状态显示不同的periodDuration
            int periodDuration = LoanPeriodCalculator.calculateDuration(investTime, loanModel.getDeadline());
            extraRateInterest = getExtraRate(loanId, amount, periodDuration);
            extraRateFee = new BigDecimal(extraRateInterest).multiply(new BigDecimal(investFeeRate)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        }

        return loanModel.getProductType() == ProductType.EXPERIENCE ? expectedInterest : expectedInterest + (extraRateInterest - extraRateFee);
    }

    @Override
    public BasePaginationDataDto<InvestorInvestPaginationItemDataDto> getInvestPagination(String loginName, int index, int pageSize, Date startTime, Date endTime, LoanStatus loanStatus) {
        startTime = new DateTime(startTime == null ? 0L : startTime).withTimeAtStartOfDay().toDate();
        if (endTime == null) {
            endTime = CalculateUtil.calculateMaxDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = investMapper.countInvestorInvestPagination(loginName, loanStatus, startTime, endTime);
        int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
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
            autoInvestPlanModel.setId(IdGenerator.generate());
            autoInvestPlanModel.setLoginName(loginName);
            autoInvestPlanModel.setMinInvestAmount(AmountConverter.convertStringToCent(dto.getMinInvestAmount()));
            autoInvestPlanModel.setMaxInvestAmount(AmountConverter.convertStringToCent(dto.getMaxInvestAmount()));
            autoInvestPlanModel.setRetentionAmount(AmountConverter.convertStringToCent(dto.getRetentionAmount()));
            autoInvestPlanModel.setAutoInvestPeriods(dto.getAutoInvestPeriods());
            autoInvestPlanModel.setCreatedTime(new Date());
            autoInvestPlanModel.setEnabled(true);
            autoInvestPlanMapper.create(autoInvestPlanModel);
        }

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, Source.WEB.name(), "", UserOpType.AUTO_INVEST, "Turn On.");
        return true;
    }

    @Override
    public boolean turnOffAutoInvest(String loginName, String ip) {
        AutoInvestPlanModel model = autoInvestPlanMapper.findByLoginName(loginName);
        if (model == null || !model.isEnabled()) {
            return false;
        }
        autoInvestPlanMapper.disable(loginName);

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, Source.WEB.name(), "", UserOpType.AUTO_INVEST, "Turn Off.");
        return true;
    }

    @Override
    public AutoInvestPlanModel findAutoInvestPlan(String loginName) {
        return autoInvestPlanMapper.findByLoginName(loginName);
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
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setNoPasswordInvest(isTurnOn);
        accountMapper.update(accountModel);
        if (isTurnOn) {
            mqWrapperClient.sendMessage(MessageQueue.TurnOnNoPasswordInvest_CompletePointTask, loginName);
        }

        // 发送用户行为日志MQ
        userOpLogService.sendUserOpLogMQ(loginName, ip, Source.WEB.name(), "", UserOpType.INVEST_NO_PASSWORD,
                isTurnOn ? "Turn On" : "Turn Off");
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

    public long calculateMembershipPreference(String loginName, long loanId, List<Long> couponIds, long investAmount, Source source) {
        long preference;
        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);
        LoanModel loanModel = loanMapper.findById(loanId);

        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanId);
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);
        long extraLoanRateExpectedInterest = 0L;
        int periodDuration = LoanPeriodCalculator.calculateDuration(new Date(), loanModel.getDeadline());
        if (CollectionUtils.isNotEmpty(extraLoanRateModels) && !StringUtils.isEmpty(loanDetailsModel) && loanDetailsModel.getExtraSource().contains(source)) {
            for (ExtraLoanRateModel extraLoanRateModel : extraLoanRateModels) {
                if ((extraLoanRateModel.getMinInvestAmount() <= investAmount && investAmount < extraLoanRateModel.getMaxInvestAmount()) ||
                        (extraLoanRateModel.getMaxInvestAmount() == 0 && extraLoanRateModel.getMinInvestAmount() <= investAmount)) {
                    extraLoanRateExpectedInterest = InterestCalculator.calculateExtraLoanRateExpectedInterest(extraLoanRateModel.getRate(), investAmount, periodDuration, investFeeRate);
                }
            }
        }

        long expectedInterest = 0;
        //红包和投资体验券不计算在内
        for (Long couponId : couponIds) {
            CouponModel couponModel = couponMapper.findById(couponId);
            if (couponModel != null) {
                int couponPeriodDuration = couponModel.getPeriod() == null ? periodDuration : couponModel.getPeriod() * InterestCalculator.DAYS_OF_MONTH;
                expectedInterest = (couponModel.getCouponType() == CouponType.INTEREST_COUPON || couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON) ?
                        InterestCalculator.getCouponExpectedInterest(loanModel, couponModel, investAmount, couponPeriodDuration) : 0;
            }
        }

        long interest = 0L;
        List<Long> perPeriodInterestList = InterestCalculator.estimateExpectedInterest(loanModel, investAmount, new Date());
        for (Long perPeriodInterest : perPeriodInterestList) {
            interest += perPeriodInterest;
        }

        long originFee = new BigDecimal(interest).multiply(new BigDecimal(defaultFee)).longValue();
        long membershipFee = new BigDecimal(interest).multiply(new BigDecimal(investFeeRate)).longValue();
        long originCouponFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(defaultFee)).longValue();
        long membershipCouponFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(investFeeRate)).longValue();
        long originExtraLoanRateExpectedInterest = new BigDecimal(extraLoanRateExpectedInterest).multiply(new BigDecimal(defaultFee)).longValue();
        long membershipExtraLoanRateExpectedInterest = new BigDecimal(extraLoanRateExpectedInterest).multiply(new BigDecimal(investFeeRate)).longValue();
        preference = originFee - membershipFee + originCouponFee - membershipCouponFee + originExtraLoanRateExpectedInterest - membershipExtraLoanRateExpectedInterest;
        return preference;
    }

    @Override
    public List<InvestModel> findContractFailInvest(long loanId) {
        return investMapper.findNoContractNoInvest(loanId);
    }


    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.wechat.lottery.startTime}\")}")
    private Date wechatLotteryStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.wechat.lottery.endTime}\")}")
    private Date wechatLotteryEndTime;

    @Override
    public boolean isNewUserForWechatLottery(String loginName) {
        int count = investMapper.countInvestBeforeDate(loginName, wechatLotteryStartTime);
        return count <= 0;
    }

    @Override
    public boolean isFirstInvest(String loginName, Date investTime) {
        int count = investMapper.countInvestBeforeDate(loginName, investTime);
        return count == 1;
    }

}
