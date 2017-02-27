package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanDetailService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoanDetailServiceImpl implements LoanDetailService {

    private final static String INVEST_NO_PASSWORD_REMIND_MAP = "invest_no_password_remind_map";

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private LoanerEnterpriseDetailsMapper loanerEnterpriseDetailsMapper;

    @Autowired
    private PledgeHouseMapper pledgeHouseMapper;

    @Autowired
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Autowired
    private PledgeEnterpriseMapper pledgeEnterpriseMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private RandomUtils randomUtils;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${invest.achievement.start.time}\")}")
    private Date achievementStartTime;

    @Autowired
    private AnxinSignService anxinSignService;

    @Override
    public LoanDetailDto getLoanDetail(String loginName, long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return null;
        }

        LoanDetailDto loanDto = this.convertModelToDto(loanModel, loginName);

        loanDto.setStatus(true);
        return loanDto;
    }

    @Override
    public ExtraLoanRateDto getExtraLoanRate(long loanId) {
        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanIdOrderByRate(loanId);
        return CollectionUtils.isEmpty(extraLoanRateModels) ? null : new ExtraLoanRateDto(extraLoanRateModels);
    }

    @Override
    public BaseDto<BasePaginationDataDto> getInvests(final String loginName, long loanId, int index, int pageSize) {
        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<LoanDetailInvestPaginationItemDto> records = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(investModels)) {
            records = Lists.transform(investModels, new Function<InvestModel, LoanDetailInvestPaginationItemDto>() {
                @Override
                public LoanDetailInvestPaginationItemDto apply(InvestModel input) {
                    LoanDetailInvestPaginationItemDto item = new LoanDetailInvestPaginationItemDto();
                    item.setAmount(AmountConverter.convertCentToString(input.getAmount()));
                    item.setSource(input.getSource());
                    item.setAutoInvest(input.isAutoInvest());
                    item.setMobile(randomUtils.encryptMobile(loginName, input.getLoginName(), input.getId()));


                    long amount = 0;
                    List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(input.getId());
                    for (InvestRepayModel investRepayModel : investRepayModels) {
                        amount += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                    }

                    if (CollectionUtils.isEmpty(investRepayModels)) {
                        amount = investService.estimateInvestIncome(input.getLoanId(), loginName, input.getAmount());
                    }

                    item.setExpectedInterest(AmountConverter.convertCentToString(amount));
                    item.setCreatedTime(input.getTradingTime() == null ? input.getCreatedTime() : input.getTradingTime());
                    item.setAchievements(input.getAchievements());
                    return item;
                }
            });
        }
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<LoanDetailInvestPaginationItemDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);

        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    private boolean isRemindNoPassword(String loginName) {
        return !Strings.isNullOrEmpty(loginName) && redisWrapperClient.hexists(INVEST_NO_PASSWORD_REMIND_MAP, loginName);
    }

    private LoanDetailDto convertModelToDto(LoanModel loanModel, String loginName) {
        long investedAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());

        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        boolean isAuthenticationRequired = anxinSignService.isAuthenticationRequired(loginName);
        boolean isAnxinUser = anxinProp != null && StringUtils.isNotEmpty(anxinProp.getAnxinUserId());

        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        InvestorDto investorDto = accountModel == null ? new InvestorDto() : new InvestorDto(accountModel.getBalance(),
                accountModel.isAutoInvest(),
                accountModel.isNoPasswordInvest(),
                this.isRemindNoPassword(loginName),
                this.calculateMaxAvailableInvestAmount(loginName, loanModel, investedAmount),
                isAuthenticationRequired,
                isAnxinUser);

        LoanDetailDto loanDto = new LoanDetailDto(loanModel,
                loanDetailsMapper.getByLoanId(loanModel.getId()),
                investedAmount,
                loanTitleMapper.findAll(),
                loanTitleRelationMapper.findByLoanId(loanModel.getId()),
                investorDto);

        LoanerDetailsModel loanerDetail = loanerDetailsMapper.getByLoanId(loanModel.getId());
        if (loanerDetail != null) {
            loanDto.setLoanerDetail(ImmutableMap.<String, String>builder()
                    .put("借款人", MessageFormat.format("{0}某", loanerDetail.getUserName().substring(0, 1)))
                    .put("性别", loanerDetail.getGender().getDescription())
                    .put("年龄", String.valueOf(loanerDetail.getAge()))
                    .put("婚姻状况", loanerDetail.getMarriage().getDescription())
                    .put("身份证号", MessageFormat.format("{0}*******", loanerDetail.getIdentityNumber().substring(0, 10)))
                    .put("申请地区", loanerDetail.getRegion())
                    .put("收入水平", loanerDetail.getIncome())
                    .put("就业情况", loanerDetail.getEmploymentStatus())
                    .put("借款用途", Strings.isNullOrEmpty(loanerDetail.getPurpose()) ? "" : loanerDetail.getPurpose())
                    .put("逾期率", MessageFormat.format("{0}%", new BigDecimal(loanRepayMapper.calculateOverdueRate(loanModel.getAgentLoginName()) * 100).setScale(0, BigDecimal.ROUND_DOWN).toString()))
                    .build());
        }

        PledgeHouseModel pledgeHouseDetail = pledgeHouseMapper.getByLoanId(loanModel.getId());
        if (pledgeHouseDetail != null) {
            loanDto.setPledgeHouseDetail(ImmutableMap.<String, String>builder()
                    .put("抵押物所在地", pledgeHouseDetail.getPledgeLocation())
                    .put("抵押物估值", pledgeHouseDetail.getEstimateAmount())
                    .put("房屋面积", pledgeHouseDetail.getSquare())
                    .put("房产证编号", pledgeHouseDetail.getPropertyCardId())
                    .put("不动产登记证明", pledgeHouseDetail.getEstateRegisterId())
                    .put("公证书编号", pledgeHouseDetail.getAuthenticAct())
                    .put("抵押物借款金额", pledgeHouseDetail.getLoanAmount())
                    .build());
        }

        PledgeVehicleModel pledgeVehicleModel = pledgeVehicleMapper.getByLoanId(loanModel.getId());
        if (pledgeVehicleModel != null) {
            loanDto.setPledgeVehicleDetail(ImmutableMap.<String, String>builder()
                    .put("抵押物所在地", pledgeVehicleModel.getPledgeLocation())
                    .put("车辆品牌", pledgeVehicleModel.getBrand())
                    .put("车辆型号", pledgeVehicleModel.getModel())
                    .put("抵押物估值", pledgeVehicleModel.getEstimateAmount())
                    .put("抵押物借款金额", pledgeVehicleModel.getLoanAmount())
                    .build());
        }

        PledgeEnterpriseModel pledgeEnterpriseModel = pledgeEnterpriseMapper.getByLoanId(loanModel.getId());
        LoanerEnterpriseDetailsModel loanerEnterpriseDetailsModel = loanerEnterpriseDetailsMapper.getByLoanId(loanModel.getId());
        if (pledgeEnterpriseModel != null && loanerEnterpriseDetailsModel != null) {
            loanDto.setPledgeEnterpriseDetail(ImmutableMap.<String, String>builder()
                    .put("公司法人", MessageFormat.format("{0}某", loanerEnterpriseDetailsModel.getJuristicPerson().substring(0, 1)))
                    .put("公司最高持股人", MessageFormat.format("{0}某", loanerEnterpriseDetailsModel.getShareholder().substring(0, 1)))
                    .put("公司所在地", loanerEnterpriseDetailsModel.getAddress())
                    .put("担保方式", pledgeEnterpriseModel.getGuarantee())
                    .put("抵押物估值", pledgeEnterpriseModel.getEstimateAmount())
                    .put("抵押物所在地", pledgeEnterpriseModel.getPledgeLocation())
                    .build());
            loanDto.setBasicInfo(loanerEnterpriseDetailsModel.getPurpose());
        }

        if (loanModel.getActivityType() == ActivityType.NEWBIE) {
            double newbieInterestCouponRate = 0;
            final List<CouponModel> allActiveCoupons = couponMapper.findAllActiveCoupons();
            for (CouponModel activeCoupon : allActiveCoupons) {
                if (activeCoupon.getCouponType() == CouponType.INTEREST_COUPON
                        && activeCoupon.getUserGroup() == UserGroup.NEW_REGISTERED_USER
                        && activeCoupon.getProductTypes().contains(ProductType._30)
                        && activeCoupon.getRate() > newbieInterestCouponRate) {
                    newbieInterestCouponRate = activeCoupon.getRate();
                }
            }
            loanDto.setNewbieInterestCouponRate(new BigDecimal(String.valueOf(newbieInterestCouponRate)).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        }

        if (loanModel.getActivityType() != ActivityType.NEWBIE && loanModel.getFundraisingStartTime().after(achievementStartTime)) {
            LoanInvestAchievementDto achievementDto = new LoanInvestAchievementDto();
            if (loanModel.getFirstInvestAchievementId() != null) {
                InvestModel firstInvest = investMapper.findById(loanModel.getFirstInvestAchievementId());
                achievementDto.setFirstInvestAchievementDate(firstInvest.getTradingTime());
                achievementDto.setFirstInvestAchievementMobile(randomUtils.encryptMobile(loginName, firstInvest.getLoginName()));
            }
            if (loanModel.getMaxAmountAchievementId() != null) {
                InvestModel maxInvest = investMapper.findById(loanModel.getMaxAmountAchievementId());
                long amount = investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), maxInvest.getLoginName(),true);
                achievementDto.setMaxAmountAchievementAmount(AmountConverter.convertCentToString(amount));
                achievementDto.setMaxAmountAchievementMobile(randomUtils.encryptMobile(loginName, maxInvest.getLoginName()));
            }
            if (loanModel.getLastInvestAchievementId() != null) {
                InvestModel lastInvest = investMapper.findById(loanModel.getLastInvestAchievementId());
                achievementDto.setLastInvestAchievementDate(lastInvest.getTradingTime());
                achievementDto.setLastInvestAchievementMobile(randomUtils.encryptMobile(loginName, lastInvest.getLoginName()));
            }
            loanDto.setAchievement(achievementDto);
        }

        return loanDto;
    }

    private long calculateMaxAvailableInvestAmount(String loginName, LoanModel loanModel, long investedAmount) {
        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), loginName,true);
        long balance = Strings.isNullOrEmpty(loginName) || accountMapper.findByLoginName(loginName) == null ? 0 : accountMapper.findByLoginName(loginName).getBalance();

        long maxAvailableInvestAmount = NumberUtils.min(balance, loanModel.getLoanAmount() - investedAmount, loanModel.getMaxInvestAmount() - sumSuccessInvestAmount);

        if (maxAvailableInvestAmount < loanModel.getMinInvestAmount()) {
            return 0L;
        }
        return maxAvailableInvestAmount - (maxAvailableInvestAmount - loanModel.getMinInvestAmount()) % loanModel.getInvestIncreasingAmount();
    }
}
