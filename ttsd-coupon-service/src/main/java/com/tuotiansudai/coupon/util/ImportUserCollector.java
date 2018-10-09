package com.tuotiansudai.coupon.util;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class ImportUserCollector implements UserCollector {

    private final static String IMPORT_COUPON_USER_REDIS_KEY_TEMPLATE = "console:{0}:importcouponuser";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (couponModel == null || userModel == null) {
            return false;
        }

        String values = redisWrapperClient.hget(MessageFormat.format(IMPORT_COUPON_USER_REDIS_KEY_TEMPLATE, String.valueOf(couponModel.getId())), "success");

        return !Strings.isNullOrEmpty(values) && (values.toLowerCase().contains(userModel.getLoginName()) || values.toLowerCase().contains(userModel.getMobile()));
    }
}
