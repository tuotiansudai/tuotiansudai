package coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.ProductType;
import coupon.dto.UserCouponDto;
import coupon.repository.mapper.CouponMapper;
import coupon.repository.mapper.UserCouponMapper;
import coupon.repository.model.CouponModel;
import coupon.repository.model.UserCouponModel;
import coupon.repository.model.UserCouponView;
import coupon.service.UserCouponService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    static Logger logger = Logger.getLogger(UserCouponServiceImpl.class);

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Value(value = "${web.coupon.lock.seconds}")
    private int couponLockSeconds;

    public UserCouponModel findById(long id) {
        return userCouponMapper.findById(id);
    }

    @Override
    public List<UserCouponView> getUnusedUserCoupons(String loginName) {
        List<UserCouponView> unusedCoupons = userCouponMapper.findUnusedCoupons(loginName);

        for (int i = unusedCoupons.size() - 1; i >= 0; i--) {
            if (unusedCoupons.get(i).getCouponType() == CouponType.BIRTHDAY_COUPON) {
                unusedCoupons.remove(i);
            }
        }
        Collections.sort(unusedCoupons);
        return unusedCoupons;
    }

    @Override
    public List<UserCouponView> findUseRecords(String loginName) {
        return userCouponMapper.findUseRecords(loginName);
    }

    @Override
    public List<UserCouponView> getExpiredUserCoupons(String loginName) {
        List<UserCouponView> expiredCoupons = userCouponMapper.findExpiredCoupons(loginName);

        for (int i = expiredCoupons.size() - 1; i >= 0; i--) {
            if (expiredCoupons.get(i).getCouponType() == CouponType.BIRTHDAY_COUPON) {
                expiredCoupons.remove(i);
            }
        }
        Collections.sort(expiredCoupons);
        return expiredCoupons;
    }

    @Override
    public List<UserCouponDto> getInvestUserCoupons(String loginName, long loanId) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);

        List<UserCouponDto> dtoList = Lists.transform(userCouponModels, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCouponModel) {
                return new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel, couponLockSeconds);
            }
        });

        return Lists.newArrayList(Iterators.filter(dtoList.iterator(), new Predicate<UserCouponDto>() {
            @Override
            public boolean apply(UserCouponDto dto) {
                return dto.isUnused();
            }
        }));
    }

    @Override
    public UserCouponDto getExperienceInvestUserCoupon(String loginName) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.NEWBIE_COUPON));

        Optional<UserCouponModel> userCouponModelOptional = Iterators.tryFind(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                return couponModel.getProductTypes().contains(ProductType.EXPERIENCE)
                        && userCouponModel.getStatus() != InvestStatus.SUCCESS
                        && userCouponModel.getEndTime().after(new Date());
            }
        });

        if (userCouponModelOptional.isPresent()) {
            UserCouponModel userCouponModel = userCouponModelOptional.get();
            return new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel, 0);
        }

        return null;
    }

    @Override
    public boolean isUsableUserCouponExist(String loginName) {
        final List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
        return Iterators.tryFind(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel input) {
                return InvestStatus.SUCCESS != input.getStatus() && input.getEndTime().after(new Date());
            }
        }).isPresent();
    }

    @Override
    public long findSumBirthdayAndInterestByLoginName(String loginName) {
        return userCouponMapper.findSumBirthdayAndInterestByLoginName(loginName);
    }

    @Override
    public long findSumRedEnvelopeByLoginName(String loginName) {
        return userCouponMapper.findSumRedEnvelopeByLoginName(loginName);
    }

    @Override
    public List<UserCouponModel> findBirthdaySuccessByLoginNameAndInvestId(String loginName, long investId) {
        return userCouponMapper.findBirthdaySuccessByLoginNameAndInvestId(loginName, investId);
    }

    @Override
    public List<UserCouponModel> findByInvestId(long investId) {
        return userCouponMapper.findByInvestId(investId);
    }

    @Override
    public List<UserCouponModel> findByLoginName(String loginName, List<CouponType> couponTypes) {
        return userCouponMapper.findByLoginName(loginName, couponTypes);
    }

    @Override
    public List<UserCouponModel> findUserCouponSuccessAndCouponTypeByInvestId(long investId, List<CouponType> couponTypeList) {
        return userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(investId, couponTypeList);
    }

    @Override
    public List<UserCouponModel> findUserCouponSuccessByInvestId(long investId) {
        return userCouponMapper.findUserCouponSuccessByInvestId(investId);
    }

    @Override
    public void update(UserCouponModel userCouponModel) {
        userCouponMapper.update(userCouponModel);
    }

    @Override
    public List<UserCouponModel> findByLoginNameAndCouponId(String loginName, Long couponId) {
        return userCouponMapper.findByLoginNameAndCouponId(loginName, couponId);
    }
}