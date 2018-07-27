package com.tuotiansudai.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.BankLoanRepayInvestDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.umpdto.UmpCouponRepayDto;
import com.tuotiansudai.fudian.umpdto.UmpExtraRateRepayDto;
import com.tuotiansudai.fudian.umpdto.UmpInvestRepayDto;
import com.tuotiansudai.fudian.umpdto.UmpLoanRepayDto;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepayServiceImpl implements RepayService {

    private static Logger logger = LoggerFactory.getLogger(RepayServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    private BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final static String INVEST_COUPON_MESSAGE = "您使用了{0}元体验券";

    private final static String INTEREST_COUPON_MESSAGE = "您使用了{0}%加息券";

    private final static String BIRTHDAY_COUPON_MESSAGE = "您已享受生日福利";

    private final static Map<Integer, String> membershipMessage = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(0, "平台收取收益和奖励的10%作为服务费")
            .put(1, "平台收取收益和奖励的10%作为服务费")
            .put(2, "平台收取收益和奖励的10%作为服务费,v2会员享受服务费9折优惠")
            .put(3, "平台收取收益和奖励的10%作为服务费,v3会员享受服务费8折优惠")
            .put(4, "平台收取收益和奖励的10%作为服务费,v4会员享受服务费8折优惠")
            .put(5, "平台收取收益和奖励的10%作为服务费,v5会员享受服务费7折优惠")
            .build());

    @Override
    public BankAsyncMessage normalRepay(RepayDto repayDto) {
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findEnabledLoanRepayByLoanId(repayDto.getLoanId());

        if (enabledLoanRepay == null || enabledLoanRepay.getStatus() == RepayStatus.WAIT_PAY) {
            logger.error("[Normal Repay] There is no enabled loan repay (loanId = {})", repayDto.getLoanId());
            return new BankAsyncMessage(null, null, false, "该标的今天没有待还款，或还款等待支付，请半小时后重试");
        }

        LoanModel loanModel = loanMapper.findById(repayDto.getLoanId());
        UserModel userModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loanModel.getAgentLoginName(), Role.LOANER);

        List<BankLoanRepayInvestDataView> bankLoanRepayInvestData = investRepayMapper.queryBankInvestRepayData(enabledLoanRepay.getLoanId(), enabledLoanRepay.getPeriod());
        List<BankLoanRepayInvestDto> bankLoanRepayInvests = bankLoanRepayInvestData.stream().map(data -> new BankLoanRepayInvestDto(data.getLoginName(),
                data.getMobile(),
                data.getBankUserName(),
                data.getBankAccountNo(),
                data.getInvestId(),
                data.getInvestRepayId(),
                data.getLoanTxNo(),
                data.getCorpus(),
                data.getExpectedInterest(),
                data.getDefaultInterest(),
                data.getExpectedFee(),
                data.getInvestBankOrderNo(),
                data.getInvestBankOrderDate())).collect(Collectors.toList());

        BankLoanRepayDto bankLoanRepayDto = new BankLoanRepayDto(userModel.getLoginName(),
                userModel.getMobile(),
                bankAccountModel.getBankUserName(),
                bankAccountModel.getBankAccountNo(),
                enabledLoanRepay.getLoanId(),
                enabledLoanRepay.getId(),
                loanModel.getLoanTxNo(),
                true,
                bankLoanRepayInvests);

        BankAsyncMessage bankAsyncMessage = bankWrapperClient.loanRepay(bankLoanRepayDto);

        if (bankAsyncMessage.isStatus()) {
            enabledLoanRepay.setActualInterest(bankLoanRepayDto.getInterest());
            enabledLoanRepay.setActualRepayDate(new Date());
            enabledLoanRepay.setRepayAmount(bankLoanRepayDto.getCapital() + bankLoanRepayDto.getInterest());
            enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
            loanRepayMapper.update(enabledLoanRepay);
        }
        return bankAsyncMessage;
    }

    @Override
    public BankAsyncMessage advancedRepay(RepayDto repayDto) {
        LoanModel loanModel = loanMapper.findById(repayDto.getLoanId());

        if (loanModel.getStatus() != LoanStatus.REPAYING) {
            logger.error("[Advance Repay] loan({}) status({}) is not REPAYING", loanModel.getStatus(), loanModel.getStatus().name());
            return new BankAsyncMessage(null, null, false, "该标的当前不支持提前还款");
        }

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());

        Optional<LoanRepayModel> enabledLoanRepayOptional = loanRepayModels.stream().filter(loanRepayModel -> loanRepayModel.getStatus() == RepayStatus.REPAYING).findFirst();

        if (!enabledLoanRepayOptional.isPresent()) {
            logger.error("[Advance Repay] There is no enabled loan repay (loanId = {})", loanModel.getId());
            return new BankAsyncMessage(null, null, false, "该标的当前不支持提前还款");
        }

        LoanRepayModel enabledLoanRepay = enabledLoanRepayOptional.get();

        DateTime currentRepayDate = new DateTime();
        DateTime lastSuccessRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels);
        List<BankLoanRepayInvestDataView> bankLoanRepayInvestData = investRepayMapper.queryBankInvestRepayData(enabledLoanRepay.getLoanId(), enabledLoanRepay.getPeriod());
        List<BankLoanRepayInvestDto> bankLoanRepayInvests = bankLoanRepayInvestData
                .stream()
                .map(data -> {
                    InvestModel transferInvestModel = data.getTransferInvestId() != null ? investMapper.findById(data.getTransferInvestId()) : null;
                    long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, data.getInvestAmount(),
                            transferInvestModel == null ? data.getTradingTime() : transferInvestModel.getTradingTime(), lastSuccessRepayDate, currentRepayDate);
                    long actualFee = new BigDecimal(actualInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(data.getInvestFeeRate())).longValue();
                    return new BankLoanRepayInvestDto(data.getLoginName(),
                            data.getMobile(),
                            data.getBankUserName(),
                            data.getBankAccountNo(),
                            data.getInvestId(),
                            data.getInvestRepayId(),
                            data.getLoanTxNo(),
                            data.getInvestAmount(),
                            actualInterest,
                            0,
                            actualFee,
                            data.getInvestBankOrderNo(),
                            data.getInvestBankOrderDate());
                })
                .collect(Collectors.toList());

        UserModel userModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loanModel.getAgentLoginName(), Role.LOANER);
        BankLoanRepayDto bankLoanRepayDto = new BankLoanRepayDto(userModel.getLoginName(),
                userModel.getMobile(),
                bankAccountModel.getBankUserName(),
                bankAccountModel.getBankAccountNo(),
                enabledLoanRepay.getLoanId(),
                enabledLoanRepay.getId(),
                loanModel.getLoanTxNo(),
                false,
                bankLoanRepayInvests);

        BankAsyncMessage bankAsyncMessage = bankWrapperClient.loanRepay(bankLoanRepayDto);

        logger.info("[Advance Repay] generate repay loanId: {}, corpus:{}, interest: {}", bankLoanRepayDto.getLoanId(), bankLoanRepayDto.getCapital(), bankLoanRepayDto.getInterest());

        if (bankAsyncMessage.isStatus()) {
            enabledLoanRepay.setActualInterest(bankLoanRepayDto.getInterest());
            enabledLoanRepay.setRepayAmount(bankLoanRepayDto.getCapital() + bankLoanRepayDto.getInterest());
            enabledLoanRepay.setActualRepayDate(currentRepayDate.toDate());
            enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
            loanRepayMapper.update(enabledLoanRepay);
        }

        logger.info(MessageFormat.format("[Advance Repay {0}] generate repay form data to update loan repay status to WAIT_PAY", String.valueOf(enabledLoanRepay.getId())));

        return bankAsyncMessage;
    }

    @Override
    public BaseDto<LoanerLoanRepayDataDto> getLoanRepay(String loginName, long loanId) {
        BaseDto<LoanerLoanRepayDataDto> baseDto = new BaseDto<>();
        LoanerLoanRepayDataDto dataDto = new LoanerLoanRepayDataDto();
        baseDto.setData(dataDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByAgentAndLoanId(loginName, loanId);
        if (loanRepayModels.isEmpty()) {
            logger.error("login user({}) is not agent({}) of loan({})", loginName, loanModel.getAgentLoginName(), loanId);
            return baseDto;
        }

        LoanRepayModel enabledLoanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);
        boolean isWaitPayLoanRepayExist = loanRepayModels.stream().anyMatch(loanRepayModel -> loanRepayModel.getStatus() == RepayStatus.WAIT_PAY);
        boolean isRepayingLoanRepayExist = loanRepayModels.stream().anyMatch(loanRepayModel -> loanRepayModel.getStatus() == RepayStatus.REPAYING);

        dataDto.setLoanId(loanId);
        if (loanModel.getIsBankPlatform()){
            dataDto.setLoanerBalance(AmountConverter.convertCentToString(bankAccountMapper.findByLoginNameAndRole(loginName, Role.LOANER).getBalance()));
        }else {
            dataDto.setLoanerBalance(AmountConverter.convertCentToString(accountMapper.findByLoginName(loginName).getBalance()));
        }

        if (enabledLoanRepayModel != null && !isWaitPayLoanRepayExist) {
            dataDto.setNormalRepayEnabled(true);
            long defaultInterest = loanRepayModels.stream().mapToLong(LoanRepayModel::getDefaultInterest).sum();
            dataDto.setNormalRepayAmount(AmountConverter.convertCentToString(enabledLoanRepayModel.getCorpus() + enabledLoanRepayModel.getExpectedInterest() + defaultInterest));
        }

        if (loanModel.getStatus() != LoanStatus.OVERDUE && isRepayingLoanRepayExist && !isWaitPayLoanRepayExist) {
            dataDto.setAdvanceRepayEnabled(true);
            DateTime now = new DateTime();
            DateTime lastSuccessRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels);
            List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);
            long advanceRepayInterest = 0;
            for (InvestModel investModel : successInvests) {
                //实际利息
                InvestModel transferInvestModel = investModel.getTransferInvestId() != null ? investMapper.findById(investModel.getTransferInvestId()) : null;
                long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel.getAmount(),
                        transferInvestModel == null ? investModel.getTradingTime() : transferInvestModel.getTradingTime(), lastSuccessRepayDate, now);
                advanceRepayInterest += actualInterest;
            }

            long corpus = loanRepayMapper.findLastLoanRepay(loanId).getCorpus();
            dataDto.setAdvanceRepayAmount(AmountConverter.convertCentToString(corpus + advanceRepayInterest));
        }

        List<LoanerLoanRepayDataItemDto> records = loanRepayModels.stream().map(loanRepayModel -> {
            boolean isEnabledLoanRepay = enabledLoanRepayModel != null && loanRepayModel.getId() == enabledLoanRepayModel.getId();
            return new LoanerLoanRepayDataItemDto(loanRepayModel, isEnabledLoanRepay);
        }).collect(Collectors.toList());

        dataDto.setStatus(true);
        dataDto.setRecords(records);
        return baseDto;
    }

    @Override
    public BaseDto<InvestRepayDataDto> findInvestorInvestRepay(String loginName, long investId) {
        InvestRepayDataDto dataDto = new InvestRepayDataDto();
        dataDto.setStatus(true);
        BaseDto<InvestRepayDataDto> baseDto = new BaseDto<>(dataDto);

        final InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        final List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndInvestId(loginName, investId);

        int lastPeriod = investRepayModels.size();
        List<InvestRepayDataItemDto> records = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(investRepayModels)) {
            long sumActualInterest = 0L; //已收回款总额
            long sumExpectedInterest = 0L; //待收回款总额

            for (InvestRepayModel investRepayModel : investRepayModels) {
                InvestRepayDataItemDto investRepayDataItemDto = new InvestRepayDataItemDto(investRepayModel);

                long expectedTotalAmount = investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee(); //当期应收回款
                long expectedTotalFee = investRepayModel.getExpectedFee(); // 当期应缴服务费
                long actualTotalAmount = investRepayModel.getRepayAmount(); // 当期实收回款
                long actualTotalFee = investRepayModel.getActualFee(); // 当期实缴服务费
                long couponExpectedInterest = 0L; // 当期应收奖励
                long couponActualInterest = 0L; // 当期实收奖励

                CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponByInvestIdAndPeriod(investRepayDataItemDto.getInvestId(), investRepayDataItemDto.getPeriod());

                if (couponRepayModel != null) {
                    expectedTotalAmount += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                    expectedTotalFee += couponRepayModel.getExpectedFee();
                    actualTotalAmount += couponRepayModel.getRepayAmount();
                    actualTotalFee += couponRepayModel.getActualFee();
                    couponExpectedInterest += couponRepayModel.getExpectedInterest();
                    couponActualInterest += couponRepayModel.getActualInterest();
                }

                if (investRepayModel.getPeriod() == lastPeriod) {
                    InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investRepayModel.getInvestId());
                    if (investExtraRateModel != null && !investExtraRateModel.isTransfer()) {
                        expectedTotalAmount += investExtraRateModel.getExpectedInterest() - investExtraRateModel.getExpectedFee();
                        expectedTotalFee += investExtraRateModel.getExpectedFee();
                        actualTotalAmount += investExtraRateModel.getRepayAmount();
                        actualTotalFee += investExtraRateModel.getActualFee();
                        couponExpectedInterest += investExtraRateModel.getExpectedInterest();
                        couponActualInterest += investExtraRateModel.getActualInterest();
                    }
                }
                investRepayDataItemDto.setAmount(AmountConverter.convertCentToString(expectedTotalAmount));
                investRepayDataItemDto.setExpectedFee(AmountConverter.convertCentToString(expectedTotalFee));
                investRepayDataItemDto.setCouponExpectedInterest(AmountConverter.convertCentToString(couponExpectedInterest));

                if (RepayStatus.COMPLETE == investRepayModel.getStatus()) {
                    sumActualInterest += actualTotalAmount;
                    investRepayDataItemDto.setActualAmount(AmountConverter.convertCentToString(actualTotalAmount));
                    investRepayDataItemDto.setActualFee(AmountConverter.convertCentToString(actualTotalFee));
                    investRepayDataItemDto.setCouponActualInterest(AmountConverter.convertCentToString(couponActualInterest));
                } else {
                    sumExpectedInterest += expectedTotalAmount;
                }

                if (loanModel.getProductType() == ProductType.EXPERIENCE) {
                    investRepayDataItemDto.setLoan(loanModel);
                    investRepayDataItemDto.setInvestExperienceAmount(AmountConverter.convertCentToString(investModel.getAmount()));
                }
                records.add(investRepayDataItemDto);
            }
            dataDto.setSumActualInterest(AmountConverter.convertCentToString(sumActualInterest));
            dataDto.setSumExpectedInterest(AmountConverter.convertCentToString(sumExpectedInterest));
            dataDto.setRecords(records);
        }

        List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(investId, Lists.newArrayList(CouponType.RED_ENVELOPE));
        dataDto.setRedInterest(AmountConverter.convertCentToString(CollectionUtils.isNotEmpty(userCouponModels) ?
                userCouponModels.stream().mapToLong(UserCouponModel::getActualInterest).sum() : 0L));

        userCouponModels = userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(investId, Lists.newArrayList(CouponType.INTEREST_COUPON, CouponType.INVEST_COUPON, CouponType.BIRTHDAY_COUPON));
        for (UserCouponModel userCouponModel : userCouponModels) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            switch (couponModel.getCouponType()) {
                case INVEST_COUPON:
                    dataDto.setCouponMessage(MessageFormat.format(INVEST_COUPON_MESSAGE, AmountConverter.convertCentToString(couponModel.getAmount())));
                    break;
                case INTEREST_COUPON:
                    dataDto.setCouponMessage(MessageFormat.format(INTEREST_COUPON_MESSAGE, covertRate(String.format("%.2f", couponModel.getRate() * 100))));
                    break;
                case BIRTHDAY_COUPON:
                    dataDto.setCouponMessage(BIRTHDAY_COUPON_MESSAGE);
                    break;
            }
        }

        MembershipModel membershipModel = userMembershipEvaluator.evaluateSpecifiedDate(investModel.getLoginName(), investModel.getCreatedTime());
        dataDto.setLevelMessage(membershipMessage.get(membershipModel.getLevel()));
        return baseDto;
    }

    @Override
    public UmpAsyncMessage umpNormalRepay(RepayDto repayDto) {
        long loanId = repayDto.getLoanId();
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);
        if (enabledLoanRepay == null || enabledLoanRepay.getStatus() == RepayStatus.WAIT_PAY) {
            logger.error("[Normal Repay] There is no enabled loan repay (loanId = {})", repayDto.getLoanId());
            return new UmpAsyncMessage(false, null, null, "该标的今天没有待还款，或还款等待支付，请半小时后重试");
        }

        LoanModel loanModel = loanMapper.findById(loanId);

        long actualInterest = this.calculateLoanRepayActualInterest(loanId, enabledLoanRepay);
        long repayAmount = enabledLoanRepay.getCorpus() + actualInterest;

        AccountModel accountModel = accountMapper.findByLoginName(loanModel.getAgentLoginName());

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);
        List<String> investorLoginNames = successInvests.stream().map(InvestModel::getLoginName).collect(Collectors.toList());

        List<AccountModel> investorAccounts = accountMapper.findByLoginNames(investorLoginNames);
        Map<String, String> umpAccountMap = Maps.newHashMap();
        for (AccountModel investorAccount : investorAccounts) {
            umpAccountMap.put(investorAccount.getLoginName(), investorAccount.getPayAccountId());
        }

        List<InvestRepayModel> investRepays = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanId, enabledLoanRepay.getPeriod());

        List<UmpInvestRepayDto> umpInvestRepayDtoList = Lists.newArrayList();
        for (InvestRepayModel investRepay : investRepays) {
            InvestModel investModel = successInvests.stream().filter(successInvest -> successInvest.getId() == investRepay.getInvestId()).findFirst().orElse(null);
            if (investModel == null) {
                return new UmpAsyncMessage(false, null, null, "回款计划数据验证失败");
            }
            umpInvestRepayDtoList.add(new UmpInvestRepayDto(investModel.getLoginName(), umpAccountMap.get(investModel.getLoginName()), loanId, investRepay.getInvestId(), investRepay.getId(), investRepay.getCorpus(), investRepay.getExpectedInterest(), investRepay.getExpectedFee(), investRepay.getDefaultInterest(), true));
        }

        List<CouponRepayModel> couponRepays = couponRepayMapper.findCouponRepayByLoanIdAndPeriod(loanId, enabledLoanRepay.getPeriod());
        List<UmpCouponRepayDto> umpCouponRepayDtoList = couponRepays.stream().map(couponRepay -> new UmpCouponRepayDto(couponRepay.getLoginName(),
                umpAccountMap.get(couponRepay.getLoginName()),
                couponRepay.getId(),
                couponRepay.getExpectedInterest(),
                couponRepay.getExpectedFee(),
                true
        )).collect(Collectors.toList());

        List<UmpExtraRateRepayDto> umpExtraRateRepayDtoList = loanModel.getPeriods() != enabledLoanRepay.getPeriod() ? Lists.newArrayList() :
                investExtraRateMapper.findByLoanId(loanId).stream().map(investExtraRateModel -> new UmpExtraRateRepayDto(investExtraRateModel.getLoginName(),
                        umpAccountMap.get(investExtraRateModel.getLoginName()),
                        investExtraRateModel.getId(),
                        investExtraRateModel.getExpectedInterest(),
                        investExtraRateModel.getExpectedFee())).collect(Collectors.toList());

        UmpAsyncMessage umpAsyncMessage = bankWrapperClient.umpLoanRepay(new UmpLoanRepayDto(loanModel.getAgentLoginName(),
                accountModel.getPayUserId(),
                loanId,
                enabledLoanRepay.getId(),
                repayAmount,
                true,
                umpInvestRepayDtoList,
                umpCouponRepayDtoList,
                umpExtraRateRepayDtoList));

        if (umpAsyncMessage.isStatus()) {
            enabledLoanRepay.setActualInterest(actualInterest);
            enabledLoanRepay.setActualRepayDate(new Date());
            enabledLoanRepay.setRepayAmount(repayAmount);
            enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
            loanRepayMapper.update(enabledLoanRepay);
            logger.info(MessageFormat.format("[Normal Repay {0}] generate repay form data to update loan repay status to WAIT_PAY", String.valueOf(enabledLoanRepay.getId())));
        }

        return umpAsyncMessage;
    }

    @Override
    public UmpAsyncMessage umpAdvancedRepay(RepayDto repayDto) {
        DateTime currentRepayDate = new DateTime();
        long loanId = repayDto.getLoanId();

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel.getStatus() != LoanStatus.REPAYING) {
            logger.error(MessageFormat.format("[Advance Repay] loan({0}) status({1}) is not REPAYING", String.valueOf(loanId), loanModel.getStatus().name()));
            return new UmpAsyncMessage(false, null, null, "该标的无法提前还款");
        }

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        LoanRepayModel enabledLoanRepay = loanRepayModels.stream().filter(loanRepayModel -> loanRepayModel.getStatus() == RepayStatus.REPAYING).findFirst().orElse(null);

        if (enabledLoanRepay == null) {
            logger.error(MessageFormat.format("[Advance Repay] There is no enabled loan repay (loanId = {0})", String.valueOf(loanId)));
            return new UmpAsyncMessage(false, null, null, "该标的无法提前还款");
        }


        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels);
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);

        List<AccountModel> investorAccounts = accountMapper.findByLoginNames(successInvests.stream().map(InvestModel::getLoginName).collect(Collectors.toList()));
        Map<String, String> umpAccountMap = investorAccounts.stream().collect(Collectors.toMap(AccountModel::getLoginName, AccountModel::getPayUserId));

        List<UmpInvestRepayDto> umpInvestRepayDtoList = Lists.newArrayList();
        List<InvestRepayModel> investRepays = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanId, enabledLoanRepay.getPeriod());
        for (InvestModel investModel : successInvests) {
            //实际利息
            InvestModel transferInvestModel = investModel.getTransferInvestId() != null ? investMapper.findById(investModel.getTransferInvestId()) : null;
            long investActualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel.getAmount(),
                    transferInvestModel == null ? investModel.getTradingTime() : transferInvestModel.getTradingTime(), lastRepayDate, currentRepayDate);
            //实际手续费
            long investActualFee = new BigDecimal(investActualInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investModel.getInvestFeeRate())).longValue();

            InvestRepayModel investRepayModel = investRepays.stream().filter(investRepay -> investRepay.getInvestId() == investModel.getId()).findFirst().orElse(null);
            if (investRepayModel == null) {
                return new UmpAsyncMessage(false, null, null, "回款计划数据验证失败");
            }
            umpInvestRepayDtoList.add(new UmpInvestRepayDto(investModel.getLoginName(),
                    umpAccountMap.get(investModel.getLoginName()),
                    loanId,
                    investModel.getId(),
                    investRepayModel.getId(),
                    investModel.getAmount(),
                    investActualInterest,
                    investActualFee,
                    0,
                    false));
        }

        List<UmpCouponRepayDto> umpCouponRepayDtoList = Lists.newArrayList();
        List<CouponRepayModel> couponRepays = couponRepayMapper.findCouponRepayByLoanIdAndPeriod(loanId, enabledLoanRepay.getPeriod());
        for (CouponRepayModel couponRepayModel : couponRepays) {
            CouponModel couponModel = this.couponMapper.findById(couponRepayModel.getCouponId());

            InvestModel investModel = successInvests.stream().filter(invest -> invest.getId() == couponRepayModel.getInvestId()).findFirst().orElse(null);
            if (investModel == null || investModel.getStatus() != InvestStatus.SUCCESS || investModel.getTransferStatus() == TransferStatus.SUCCESS) {
                return new UmpAsyncMessage(false, null, null, "回款计划数据验证失败");
            }

            long actualInterest = InterestCalculator.calculateCouponActualInterest(investModel.getAmount(), couponModel.getRate(), investModel.getTradingTime(), loanModel, enabledLoanRepay, loanRepayModels);

            umpCouponRepayDtoList.add(new UmpCouponRepayDto(couponRepayModel.getLoginName(),
                    umpAccountMap.get(couponRepayModel.getLoginName()),
                    couponRepayModel.getId(),
                    actualInterest,
                    (long) (actualInterest * investModel.getInvestFeeRate()),
                    false));
        }

        List<UmpExtraRateRepayDto> umpExtraRateRepayDtoList = Lists.newArrayList();
        List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId).stream().filter(investExtraRateModel -> investExtraRateModel.getStatus() == RepayStatus.REPAYING).collect(Collectors.toList());
        for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
            InvestModel investModel = successInvests.stream().filter(successInvest -> successInvest.getId() == investExtraRateModel.getInvestId()).findFirst().orElse(null);
            if (investModel == null) {
                return new UmpAsyncMessage(false, null, null, "回款计划数据验证失败");
            }
            long actualInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, investExtraRateModel.getExtraRate(), investModel, new Date());
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(investModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();

            umpExtraRateRepayDtoList.add(new UmpExtraRateRepayDto(investExtraRateModel.getLoginName(),
                    umpAccountMap.get(investExtraRateModel.getLoginName()),
                    investExtraRateModel.getId(),
                    actualInterest,
                    actualFee));
        }

        long corpus = loanRepayMapper.findLastLoanRepay(loanId).getCorpus();
        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvests, lastRepayDate, currentRepayDate);

        UmpAsyncMessage umpAsyncMessage = bankWrapperClient.umpLoanRepay(new UmpLoanRepayDto(loanModel.getAgentLoginName(),
                accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                loanId,
                enabledLoanRepay.getId(),
                corpus + actualInterest,
                false,
                umpInvestRepayDtoList,
                umpCouponRepayDtoList,
                umpExtraRateRepayDtoList));

        if (umpAsyncMessage.isStatus()) {
            enabledLoanRepay.setActualInterest(actualInterest);
            enabledLoanRepay.setActualRepayDate(currentRepayDate.toDate());
            enabledLoanRepay.setRepayAmount(corpus + actualInterest);
            enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
            loanRepayMapper.update(enabledLoanRepay);
            logger.info(MessageFormat.format("[advance Repay {0}] generate repay form data to update loan repay status to WAIT_PAY", String.valueOf(enabledLoanRepay.getId())));
        }

        return umpAsyncMessage;
    }


    private static String covertRate(String rate) {
        return rate.contains(".00") ? rate.replaceAll("\\.00", "") : String.valueOf(Double.parseDouble(rate));
    }

    private long calculateLoanRepayActualInterest(long loanId, LoanRepayModel enabledLoanRepay) {
        if (enabledLoanRepay.getStatus() == RepayStatus.REPAYING) {
            return enabledLoanRepay.getExpectedInterest();
        }

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        return loanRepayModels.stream().mapToLong(loanRepayModel -> loanRepayModel.getStatus() == RepayStatus.OVERDUE ? loanRepayModel.getExpectedInterest() + loanRepayModel.getDefaultInterest() : 0).sum();
    }
}
