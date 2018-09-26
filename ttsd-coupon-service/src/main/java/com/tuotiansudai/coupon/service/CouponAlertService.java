package com.tuotiansudai.coupon.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuotiansudai.dto.CouponAlertDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class CouponAlertService {

    static Logger logger = Logger.getLogger(CouponAlertService.class);

    private static final String COUPON_ALERT_KEY = "web:coupon:alert";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    public CouponAlertDto getCouponAlert(String loginName, List<CouponType> couponTypes) {
        if (Strings.isNullOrEmpty(loginName)) {
            return null;
        }
        loginName = loginName.toLowerCase();
        try {
            if (!redisWrapperClient.hexists(COUPON_ALERT_KEY, loginName)) {
                redisWrapperClient.hset(COUPON_ALERT_KEY, loginName, objectMapper.writeValueAsString(Sets.<Long>newHashSet()));
            }
            String redisValue = redisWrapperClient.hget(COUPON_ALERT_KEY, loginName);
            Set<Long> userCouponIds = objectMapper.readValue(redisValue, new TypeReference<Set<Long>>() {
            });
            CouponAlertDto newbieCouponAlertDto = new CouponAlertDto();
            newbieCouponAlertDto.setCouponType(CouponType.NEWBIE_COUPON);
            CouponAlertDto redEnvelopeCouponAlertDto = new CouponAlertDto();
            redEnvelopeCouponAlertDto.setCouponType(CouponType.RED_ENVELOPE);

            List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE));
            if (CollectionUtils.isNotEmpty(userCouponModels)) {
                for (UserCouponModel userCouponModel : userCouponModels) {
                    if (!userCouponIds.contains(userCouponModel.getCouponId())) {
                        CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());

                        if (couponModel.getCouponType() == CouponType.NEWBIE_COUPON && couponModel.getUserGroup() != UserGroup.EXPERIENCE_INVEST_SUCCESS) {
                            newbieCouponAlertDto.getCouponIds().add(userCouponModel.getCouponId());
                            newbieCouponAlertDto.setAmount(newbieCouponAlertDto.getAmount() + couponModel.getAmount());
                            newbieCouponAlertDto.setExpiredDate(userCouponModel.getEndTime());
                        }

                        if (couponModel.getCouponType() == CouponType.RED_ENVELOPE && couponModel.getUserGroup() == UserGroup.EXPERIENCE_INVEST_SUCCESS) {
                            redEnvelopeCouponAlertDto.getCouponIds().add(userCouponModel.getCouponId());
                            redEnvelopeCouponAlertDto.setAmount(redEnvelopeCouponAlertDto.getAmount() + couponModel.getAmount());
                            redEnvelopeCouponAlertDto.setExpiredDate(userCouponModel.getEndTime());
                        }
                    }
                }

                if (couponTypes.contains(CouponType.NEWBIE_COUPON) && CollectionUtils.isNotEmpty(newbieCouponAlertDto.getCouponIds())) {
                    userCouponIds.addAll(newbieCouponAlertDto.getCouponIds());
                    redisWrapperClient.hset(COUPON_ALERT_KEY, loginName, objectMapper.writeValueAsString(userCouponIds));
                    return newbieCouponAlertDto;
                }

                if (couponTypes.contains(CouponType.RED_ENVELOPE) && CollectionUtils.isNotEmpty(redEnvelopeCouponAlertDto.getCouponIds())) {
                    userCouponIds.addAll(redEnvelopeCouponAlertDto.getCouponIds());
                    redisWrapperClient.hset(COUPON_ALERT_KEY, loginName, objectMapper.writeValueAsString(userCouponIds));
                    return redEnvelopeCouponAlertDto;
                }
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }
}
