package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.*;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Autowired
    private CouponExchangeMapper couponExchangeMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    private static String redisKeyTemplate = "console:{0}:importcouponuser";

    @Override
    @Transactional
    public ExchangeCouponDto createCoupon(String loginName, ExchangeCouponDto exchangeCouponDto) throws CreateCouponException {
        this.checkCoupon(exchangeCouponDto);
        CouponModel couponModel = new CouponModel(exchangeCouponDto);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponMapper.create(couponModel);
        exchangeCouponDto.setId(couponModel.getId());
        if (couponModel.getUserGroup() == UserGroup.IMPORT_USER) {
            redisWrapperClient.hset(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "success", redisWrapperClient.hget(MessageFormat.format(redisKeyTemplate, exchangeCouponDto.getFile()), "success"));
            redisWrapperClient.hset(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "failed", redisWrapperClient.hget(MessageFormat.format(redisKeyTemplate, exchangeCouponDto.getFile()), "failed"));
            redisWrapperClient.del(MessageFormat.format(redisKeyTemplate, exchangeCouponDto.getFile()));
        } else if (Lists.newArrayList(UserGroup.AGENT, UserGroup.CHANNEL).contains(couponModel.getUserGroup())) {
            CouponUserGroupModel couponUserGroupModel = new CouponUserGroupModel();
            couponUserGroupModel.setCouponId(couponModel.getId());
            couponUserGroupModel.setUserGroup(couponModel.getUserGroup());
            couponUserGroupModel.setUserGroupItems(couponModel.getUserGroup() == UserGroup.AGENT ? couponModel.getAgents() : couponModel.getChannels());
            couponUserGroupMapper.create(couponUserGroupModel);
        }
        if (exchangeCouponDto.getExchangePoint() != null && exchangeCouponDto.getExchangePoint() > 0) {
            CouponExchangeModel couponExchangeModel = new CouponExchangeModel();
            couponExchangeModel.setCouponId(couponModel.getId());
            couponExchangeModel.setSeq(exchangeCouponDto.getSeq());
            couponExchangeModel.setExchangePoint(exchangeCouponDto.getExchangePoint());
            couponExchangeMapper.create(couponExchangeModel);
        }
        return exchangeCouponDto;

    }

    private void checkCoupon(CouponDto couponDto) throws CreateCouponException {
        CouponModel couponModel = new CouponModel(couponDto);
        if (!Lists.newArrayList(CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON).contains(couponDto.getCouponType())) {
            long amount = couponModel.getAmount();
            if (amount <= 0) {
                throw new CreateCouponException("投资体验券金额应大于0!");
            }
        }

        if (!Lists.newArrayList(CouponType.RED_ENVELOPE, CouponType.BIRTHDAY_COUPON).contains(couponDto.getCouponType())) {
            long totalCount = couponModel.getTotalCount();
            if (totalCount <= 0) {
                throw new CreateCouponException("发放数量应大于0!");
            }
        }

        if (!Lists.newArrayList(CouponType.BIRTHDAY_COUPON, CouponType.INTEREST_COUPON, CouponType.RED_ENVELOPE).contains(couponDto.getCouponType())) {
            long investLowerLimit = couponModel.getInvestLowerLimit();
            if (investLowerLimit <= 0) {
                throw new CreateCouponException("使用条件金额应大于0!");
            }
        }

        Date startTime = couponModel.getStartTime();
        Date endTime = couponModel.getEndTime();
        if (CouponType.isNewBieCoupon(couponDto.getCouponType())) {
            if (startTime == null) {
                throw new CreateCouponException("活动起期不能为空!");
            }
            if (endTime == null) {
                throw new CreateCouponException("活动止期不能为空!");
            }
            if (endTime.before(startTime)) {
                throw new CreateCouponException("活动止期早于活动起期!");
            }
        }
    }

    @Override
    @Transactional
    public void editCoupon(String loginName, ExchangeCouponDto exchangeCouponDto) throws CreateCouponException {
        this.checkCoupon(exchangeCouponDto);
        CouponModel couponModel = new CouponModel(exchangeCouponDto);
        couponModel.setId(exchangeCouponDto.getId());
        couponModel.setUpdatedBy(loginName);
        couponModel.setUpdatedTime(new Date());
        if (couponModel.getUserGroup() != UserGroup.IMPORT_USER
                && redisWrapperClient.exists(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())))) {
            redisWrapperClient.del(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())));
        }
        if (couponModel.getUserGroup() == UserGroup.IMPORT_USER && StringUtils.isNotEmpty(exchangeCouponDto.getFile())) {
            redisWrapperClient.hset(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "success", redisWrapperClient.hget(MessageFormat.format(redisKeyTemplate, exchangeCouponDto.getFile()), "success"));
            redisWrapperClient.hset(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "failed", redisWrapperClient.hget(MessageFormat.format(redisKeyTemplate, exchangeCouponDto.getFile()), "failed"));
            redisWrapperClient.del(MessageFormat.format(redisKeyTemplate, exchangeCouponDto.getFile()));
        }
        couponMapper.updateCoupon(couponModel);
        if (Lists.newArrayList(UserGroup.AGENT, UserGroup.CHANNEL).contains(couponModel.getUserGroup())) {
            CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(exchangeCouponDto.getId());
            if (couponUserGroupModel != null) {
                couponUserGroupModel.setUserGroup(exchangeCouponDto.getUserGroup());
                couponUserGroupModel.setUserGroupItems(exchangeCouponDto.getUserGroup() == UserGroup.AGENT ? exchangeCouponDto.getAgents() : exchangeCouponDto.getChannels());
                couponUserGroupMapper.update(couponUserGroupModel);
            } else {
                CouponUserGroupModel couponUserGroup = new CouponUserGroupModel();
                couponUserGroup.setCouponId(exchangeCouponDto.getId());
                couponUserGroup.setUserGroup(exchangeCouponDto.getUserGroup());
                couponUserGroup.setUserGroupItems(exchangeCouponDto.getUserGroup() == UserGroup.AGENT ? exchangeCouponDto.getAgents() : exchangeCouponDto.getChannels());
                couponUserGroupMapper.create(couponUserGroup);
            }
        } else {
            CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(exchangeCouponDto.getId());
            if (couponUserGroupModel != null) {
                couponUserGroupMapper.delete(couponUserGroupModel.getId());
            }
        }
        if (exchangeCouponDto.getExchangePoint() != null && exchangeCouponDto.getExchangePoint() > 0) {

            CouponExchangeModel couponExchangeModel = couponExchangeMapper.findByCouponId(exchangeCouponDto.getId());
            couponExchangeModel.setExchangePoint(exchangeCouponDto.getExchangePoint());
            couponExchangeModel.setSeq(exchangeCouponDto.getSeq());
            couponExchangeMapper.update(couponExchangeModel);
        }

    }

    @Override
    public List<CouponDto> findBirthdayCoupons(int index, int pageSize) {
        List<CouponModel> couponModels = couponMapper.findBirthdayCoupons((index - 1) * pageSize, pageSize);
        for (CouponModel couponModel : couponModels) {
            couponModel.setTotalInvestAmount(userCouponMapper.findSumInvestAmountByCouponId(couponModel.getId()));
        }
        return Lists.transform(couponModels, new Function<CouponModel, CouponDto>() {
            @Override
            public CouponDto apply(CouponModel input) {
                return new CouponDto(input);
            }
        });
    }

    @Override
    public int findBirthdayCouponsCount() {
        return couponMapper.findBirthdayCouponsCount();
    }

    @Override
    public List<CouponDto> findRedEnvelopeCoupons(int index, int pageSize) {
        List<CouponModel> couponModels = couponMapper.findRedEnvelopeCoupons((index - 1) * pageSize, pageSize);
        for (CouponModel couponModel : couponModels) {
            couponModel.setTotalInvestAmount(userCouponMapper.findSumInvestAmountByCouponId(couponModel.getId()));
            if (couponModel.getUserGroup() == UserGroup.IMPORT_USER) {
                if (StringUtils.isNotEmpty(redisWrapperClient.hget(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "failed"))) {
                    couponModel.setImportIsRight(false);
                } else {
                    couponModel.setImportIsRight(true);
                }
            }
        }
        return Lists.transform(couponModels, new Function<CouponModel, CouponDto>() {
            @Override
            public CouponDto apply(CouponModel input) {
                return new CouponDto(input);
            }
        });
    }

    @Override
    public int findRedEnvelopeCouponsCount() {
        return couponMapper.findRedEnvelopeCouponsCount();
    }

    @Override
    public List<CouponDto> findInterestCoupons(int index, int pageSize) {
        List<CouponModel> couponModels = couponMapper.findInterestCoupons((index - 1) * pageSize, pageSize);
        for (CouponModel couponModel : couponModels) {
            couponModel.setTotalInvestAmount(userCouponMapper.findSumInvestAmountByCouponId(couponModel.getId()));
            if (couponModel.getUserGroup() == UserGroup.IMPORT_USER) {
                if (StringUtils.isNotEmpty(redisWrapperClient.hget(MessageFormat.format(redisKeyTemplate, String.valueOf(couponModel.getId())), "failed"))) {
                    couponModel.setImportIsRight(false);
                } else {
                    couponModel.setImportIsRight(true);
                }
            }
        }
        return Lists.transform(couponModels, new Function<CouponModel, CouponDto>() {
            @Override
            public CouponDto apply(CouponModel input) {
                return new CouponDto(input);
            }
        });
    }

    @Override
    public int findInterestCouponsCount() {
        return couponMapper.findInterestCouponsCount();
    }

    @Override
    public List<CouponDto> findNewbieAndInvestCoupons(int index, int pageSize) {
        List<CouponModel> couponModels = couponMapper.findNewbieAndInvestCoupons((index - 1) * pageSize, pageSize);
        for (CouponModel couponModel : couponModels) {
            couponModel.setTotalInvestAmount(userCouponMapper.findSumInvestAmountByCouponId(couponModel.getId()));
        }
        return Lists.transform(couponModels, new Function<CouponModel, CouponDto>() {
            @Override
            public CouponDto apply(CouponModel input) {
                return new CouponDto(input);
            }
        });
    }

    @Override
    public int findNewbieAndInvestCouponsCount() {
        return couponMapper.findNewbieAndInvestCouponsCount();
    }


    @Override
    public CouponModel findCouponById(long couponId) {
        return couponMapper.findById(couponId);
    }

    @Override
    public long findEstimatedCount(UserGroup userGroup) {
        switch (userGroup) {
            case ALL_USER:
                return userMapper.findAllUsersByProvinces(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build())).size();
            case INVESTED_USER:
                return investMapper.findInvestorCount();
            case REGISTERED_NOT_INVESTED_USER:
                return investMapper.findRegisteredNotInvestCount();
            case STAFF:
                return userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", Lists.newArrayList()).build())).size();
            case STAFF_RECOMMEND_LEVEL_ONE:
                return userMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build())).size();
            case MEMBERSHIP_V0:
                return userMembershipMapper.countMembershipByLevel(0);
            case MEMBERSHIP_V1:
                return userMembershipMapper.countMembershipByLevel(1);
            case MEMBERSHIP_V2:
                return userMembershipMapper.countMembershipByLevel(2);
            case MEMBERSHIP_V3:
                return userMembershipMapper.countMembershipByLevel(3);
            case MEMBERSHIP_V4:
                return userMembershipMapper.countMembershipByLevel(4);
            case MEMBERSHIP_V5:
                return userMembershipMapper.countMembershipByLevel(5);
            default:
                return 0;
        }
    }

    @Override
    public List<UserCouponModel> findCouponDetail(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime, int index, int pageSize) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponIdAndStatus(couponId, isUsed, loginName, mobile, registerStartTime, registerEndTime, (index - 1) * pageSize, pageSize);
        for (UserCouponModel userCouponModel : userCouponModels) {
            userCouponModel.setLoanName(userCouponModel.getLoanId() != null ? loanMapper.findById(userCouponModel.getLoanId()).getName() : null);
            userCouponModel.setInvestAmount(userCouponModel.getInvestId() != null ? investMapper.findById(userCouponModel.getInvestId()).getAmount() : null);
        }
        return userCouponModels;
    }

    @Override
    public int findCouponDetailCount(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime) {
        return userCouponMapper.findCouponDetailCount(couponId, isUsed, loginName, mobile, registerStartTime, registerEndTime);
    }


    @Override
    @Transactional
    public boolean deleteCoupon(String loginName, long couponId) {
        if (CollectionUtils.isNotEmpty(userCouponMapper.findByCouponId(couponId))) {
            return false;
        }
        CouponModel couponModel = couponMapper.findById(couponId);
        couponModel.setUpdatedBy(loginName);
        couponModel.setUpdatedTime(new Date());
        couponModel.setDeleted(true);
        couponModel.setActive(false);
        couponMapper.updateCoupon(couponModel);
        return true;
    }

    @Override
    public long estimateCouponExpectedInterest(String loginName, long loanId, List<Long> couponIds, long amount) {
        long totalInterest = 0;

        //根据loginNameName查询出当前会员的相关信息,需要判断是否为空,如果为空则安装在费率0.1计算
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        double investFeeRate = membershipModel != null ? membershipModel.getFee() : this.defaultFee;
        LoanModel loanModel = loanMapper.findById(loanId);
        if(loanModel != null && ProductType.EXPERIENCE == loanModel.getProductType()){
            investFeeRate = this.defaultFee;
        }

        for (Long couponId : couponIds) {
            CouponModel couponModel = couponMapper.findById(couponId);
            if (loanModel == null || couponModel == null) {
                continue;
            }
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(amount, loanModel, couponModel);
            long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, amount, investFeeRate);
            totalInterest += expectedInterest - expectedFee;
        }

        return totalInterest;
    }

    @Override
    public List<ExchangeCouponDto> findCouponExchanges(int index, int pageSize) {
        List<CouponModel> couponModels = couponMapper.findCouponExchanges((index - 1) * pageSize, pageSize);
        return Lists.transform(couponModels, new Function<CouponModel, ExchangeCouponDto>() {
            @Override
            public ExchangeCouponDto apply(CouponModel input) {
                ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto(input);
                CouponExchangeModel couponExchangeModel = couponExchangeMapper.findByCouponId(input.getId());
                exchangeCouponDto.setExchangePoint(couponExchangeModel.getExchangePoint());
                exchangeCouponDto.setSeq(couponExchangeModel.getSeq());
                return exchangeCouponDto;
            }
        });
    }

    public int findCouponExchangeCount() {
        return couponMapper.findCouponExchangeCount();
    }

    @Override
    public CouponExchangeModel findCouponExchangeByCouponId(long couponId) {
        return couponExchangeMapper.findByCouponId(couponId);
    }

    @Override
    public long findExperienceInvestAmount(List<InvestModel> investModelList) {
        long amount = 0;
        if (CollectionUtils.isNotEmpty(investModelList)) {
            List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(investModelList.get(0).getId());
            CouponModel couponModel = couponMapper.findById(userCouponModels.get(0).getCouponId());
            amount = new BigDecimal(investModelList.size() % 100).multiply(new BigDecimal(couponModel.getAmount())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        }
        return amount;
    }
}
