package com.tuotiansudai.console.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.dto.CouponDetailsDto;
import com.tuotiansudai.dto.CouponDto;
import com.tuotiansudai.dto.ExchangeCouponDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class ConsoleCouponService {

    static Logger logger = Logger.getLogger(ConsoleCouponService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMapperConsole userMapperConsole;

    @Autowired
    private UserRecommendationMapper userRecommendationMapper;

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    private static String redisKeyTemplate = "console:{0}:importcouponuser";

    public CouponModel findCouponById(long couponId) {
        return couponMapper.findById(couponId);
    }

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
        return exchangeCouponDto;

    }

    private void checkCoupon(CouponDto couponDto) throws CreateCouponException {
        CouponModel couponModel = new CouponModel(couponDto);
        if (!Lists.newArrayList(CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON).contains(couponDto.getCouponType())) {
            long amount = couponModel.getAmount();
            if (amount <= 0) {
                throw new CreateCouponException("出借体验券金额应大于0!");
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
    }

    public long findEstimatedCount(UserGroup userGroup) {
        switch (userGroup) {
            case ALL_USER:
                return userMapper.findUsersCount();
            case INVESTED_USER:
                return investMapper.findInvestorCount();
            case REGISTERED_NOT_INVESTED_USER:
                return investMapper.findRegisteredNotInvestCount();
            case STAFF:
                return userRoleMapper.findCountByRole(Role.ZC_STAFF) + userRoleMapper.findCountByRole(Role.SD_STAFF);
            case STAFF_RECOMMEND_LEVEL_ONE:
                return userRecommendationMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build())).size();
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

    public List<CouponDetailsDto> findCouponDetail(long couponId, Boolean isUsed, String loginName, String mobile, Date createdTime, Date registerStartTime, Date registerEndTime, Date usedStartTime, Date usedEndTime, int index, int pageSize) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponIdAndStatus(couponId, isUsed, loginName, mobile, createdTime, registerStartTime, registerEndTime, usedStartTime, usedEndTime, (index - 1) * pageSize, pageSize);
        List<CouponDetailsDto> couponDetailsDtoList = Lists.newArrayList();
        for (UserCouponModel userCouponModel : userCouponModels) {
            LoanModel loanModel = userCouponModel.getLoanId() != null ? loanMapper.findById(userCouponModel.getLoanId()) : null;
            InvestModel investModel = investMapper.findById(userCouponModel.getInvestId());
            userCouponModel.setInvestAmount(userCouponModel.getInvestId() != null ? investModel.getAmount() : null);
            long interest = 0;

            if (userCouponModel.getStatus() == InvestStatus.SUCCESS && loanModel != null) {
                interest = investService.estimateInvestIncome(loanModel.getId(), investModel.getInvestFeeRate(), loginName, userCouponModel.getInvestAmount(), investModel.getInvestTime());
                couponDetailsDtoList.add(new CouponDetailsDto(userCouponModel.getLoginName(), userCouponModel.getUsedTime(), userCouponModel.getInvestAmount(),
                        userCouponModel.getLoanId(), loanModel.getName(), loanModel.getProductType(), interest, userCouponModel.getEndTime()));
                continue;
            }
            couponDetailsDtoList.add(new CouponDetailsDto(userCouponModel.getLoginName(), null, userCouponModel.getInvestAmount(),
                    userCouponModel.getLoanId(), loanModel != null ? loanModel.getName() : "", loanModel != null ? loanModel.getProductType() : null, interest, userCouponModel.getEndTime()));
        }
        return couponDetailsDtoList;
    }

    public int findCouponDetailCount(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime, Date usedStartTime, Date usedEndTime) {
        return userCouponMapper.findCouponDetailCount(couponId, isUsed, loginName, mobile, registerStartTime, registerEndTime, usedStartTime, usedEndTime);
    }


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

    public int findCouponsCountByTypeRedAndMoney(String couponType, float amount, String couponSource) {
        return couponMapper.findCouponsCountByTypeRedAndMoney(couponType, couponSource, (int) (amount * 100));
    }

    public List<CouponDto> findCouponsByTypeRedAndMoney(int index, int pageSize, String couponType, float amount, String couponSource) {
        List<CouponModel> couponModels = couponMapper.findCouponsByTypeRedAndMoney(couponType, couponSource, (int) (amount * 100), (index - 1) * pageSize, pageSize);
        for (CouponModel couponModel : couponModels) {
            couponModel.setTotalInvestAmount(userCouponMapper.findSumInvestAmountByCouponId(couponModel.getId()));
            if ((CouponType.RED_ENVELOPE.getName().equals(couponType) || CouponType.INTEREST_COUPON.getName().equals(couponType)) && couponModel.getUserGroup() == UserGroup.IMPORT_USER) {
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
}
