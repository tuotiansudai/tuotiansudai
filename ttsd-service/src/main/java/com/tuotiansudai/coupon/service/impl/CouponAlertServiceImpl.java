package com.tuotiansudai.coupon.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.dto.CouponAlertDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.dto.SmsCouponNotifyDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CouponType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CouponAlertServiceImpl implements CouponAlertService {

    static Logger logger = Logger.getLogger(CouponAlertServiceImpl.class);

    private static final String COUPON_ALERT_KEY = "web:coupon:alert";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Override
    public CouponAlertDto getCouponAlert(String loginName) {
        if (Strings.isNullOrEmpty(loginName)) {
            return null;
        }
        try {
            if (!redisWrapperClient.hexists(COUPON_ALERT_KEY, loginName)) {
                redisWrapperClient.hset(COUPON_ALERT_KEY, loginName, objectMapper.writeValueAsString(Sets.<Long>newHashSet()));
            }
            String redisValue = redisWrapperClient.hget(COUPON_ALERT_KEY, loginName);
            Set<Long> userCouponIds = objectMapper.readValue(redisValue, new TypeReference<Set<Long>>() {});
            CouponAlertDto newbieCouponAlertDto = new CouponAlertDto();
            newbieCouponAlertDto.setCouponType(CouponType.NEWBIE_COUPON);
            CouponAlertDto redEnvelopeCouponAlertDto = new CouponAlertDto();
            redEnvelopeCouponAlertDto.setCouponType(CouponType.RED_ENVELOPE);

            List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE));
            if (CollectionUtils.isNotEmpty(userCouponModels)) {
                for (UserCouponModel userCouponModel : userCouponModels) {
                    if (!userCouponIds.contains(userCouponModel.getCouponId())) {
                        CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                        if (couponModel.getCouponType() == CouponType.NEWBIE_COUPON) {
                            newbieCouponAlertDto.getCouponIds().add(userCouponModel.getCouponId());
                            newbieCouponAlertDto.setAmount(newbieCouponAlertDto.getAmount() + couponModel.getAmount());
                            if (newbieCouponAlertDto.getExpiredDate() == null) {
                                newbieCouponAlertDto.setExpiredDate(userCouponModel.getEndTime());
                            } else {
                                newbieCouponAlertDto.setExpiredDate(newbieCouponAlertDto.getExpiredDate().after(userCouponModel.getEndTime()) ? userCouponModel.getEndTime() : newbieCouponAlertDto.getExpiredDate());
                            }
                        }

                        if (couponModel.getCouponType() == CouponType.RED_ENVELOPE) {
                            redEnvelopeCouponAlertDto.getCouponIds().add(userCouponModel.getCouponId());
                            redEnvelopeCouponAlertDto.setAmount(redEnvelopeCouponAlertDto.getAmount() + couponModel.getAmount());
                            if (redEnvelopeCouponAlertDto.getExpiredDate() == null) {
                                redEnvelopeCouponAlertDto.setExpiredDate(userCouponModel.getEndTime());
                            } else {
                                redEnvelopeCouponAlertDto.setExpiredDate(redEnvelopeCouponAlertDto.getExpiredDate().after(userCouponModel.getEndTime()) ? userCouponModel.getEndTime() : redEnvelopeCouponAlertDto.getExpiredDate());
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(newbieCouponAlertDto.getCouponIds())) {
                    userCouponIds.addAll(newbieCouponAlertDto.getCouponIds());
                    redisWrapperClient.hset(COUPON_ALERT_KEY, loginName, objectMapper.writeValueAsString(userCouponIds));
                    return newbieCouponAlertDto;
                }

                if (CollectionUtils.isNotEmpty(redEnvelopeCouponAlertDto.getCouponIds())) {
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

    @Override
    public void BirthdayNotify() {

        List<String> userMobileList = userMapper.findUsersBirthdayMobile();
        for (String mobile : userMobileList) {
            SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
            notifyDto.setMobile(mobile.trim());
            smsWrapperClient.sendBirthdayNotify(notifyDto);
        }
    }
}
